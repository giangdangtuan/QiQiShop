CREATE TABLE `upload_files`
(
    `id`               int unsigned NOT NULL AUTO_INCREMENT,
    `origin_url`       varchar(500)          DEFAULT NULL COMMENT 'Lưu trữ URL gốc của tệp tin, nếu tệp được lấy từ một nguồn trực tuyến',
    `origin_file_path` varchar(500)          DEFAULT NULL COMMENT 'Đường dẫn tệp gốc trên hệ thống lưu trữ',
    `thumb_url`        varchar(500)          DEFAULT NULL COMMENT 'URL của thumbnail',
    `thumb_file_path`  varchar(500)          DEFAULT NULL COMMENT 'Đường dẫn đến thumbnail trên hệ thống lưu trữ',
    `type`             int          NOT NULL COMMENT 'Loại tệp tin: `0`: Hình ảnh, `1`: Video, `2`:PDF',
    `width`            int                   DEFAULT NULL COMMENT 'Chiều rộng của tập tin (Áp dụng cho hình ảnh hoặc video) - Pixel',
    `height`           int                   DEFAULT NULL COMMENT 'Chiều cao của tập tin (Áp dụng cho hình ảnh hoặc video) - Pixel',
    `duration`         int                   DEFAULT NULL COMMENT 'Dung lượng của tệp',
    `size`             bigint                DEFAULT NULL COMMENT 'Kích thước tập tin - tính bằng byte',
    `deleted`          bit(1)       NOT NULL DEFAULT b'0' COMMENT 'Đánh dấu trạng thái xóa của bản ghi: `0`: Chưa xóa, `1`: Đã xóa',
    `created_at`       datetime     NOT NULL,
    `updated_at`       datetime     NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `category`
(
    `id`          int unsigned NOT NULL AUTO_INCREMENT,
    `name`        varchar(255) NOT NULL,
    `status`      int          NOT NULL COMMENT 'Trạng thái hoạt động: `0`: Không hoạt động, 1: Hoạt động',
    `deleted`     bit          NOT NULL DEFAULT 0 COMMENT 'Đánh dấu trạng thái xóa của bản ghi: `0`: Chưa xóa, `1`: Đã xóa',
    `created_at`  datetime     NOT NULL,
    `updated_at`  datetime     NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT 'Ngành hàng'
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `product`
(
    `id`                     int unsigned NOT NULL AUTO_INCREMENT,
    `category_id`            int unsigned NOT NULL COMMENT 'Khóa ngoại tham chiếu đến ngành hàng',
    `cover_image`            int unsigned          DEFAULT NULL COMMENT 'Khóa ngoại tham chiếu đến ảnh bìa sản phẩm',
    `name`                   varchar(255) NOT NULL,
    `description`            TEXT                  DEFAULT NULL,
    `status`                 int          NOT NULL COMMENT 'Trạng thái hoạt động: `0`: Không hoạt động, 1: Hoạt động',
    `deleted`                bit          NOT NULL DEFAULT 0 COMMENT 'Đánh dấu trạng thái xóa của bản ghi: `0`: Chưa xóa, `1`: Đã xóa',
    `created_at`             datetime     NOT NULL,
    `updated_at`             datetime     NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`category_id`) REFERENCES `category` (`id`),
    FOREIGN KEY (`cover_image`) REFERENCES `upload_files` (`id`)
) ENGINE = InnoDB COMMENT 'Sản phẩm'
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `model`
(
    `id`                     int unsigned    NOT NULL AUTO_INCREMENT,
    `product_id`             int unsigned    NOT NULL COMMENT 'Khóa ngoại tham chiếu đến ngành hàng',
    `cover_image`            int unsigned    NOT NULL COMMENT 'Khóa ngoại tham chiếu đến ảnh bìa sản phẩm',
    `name`                   varchar(255)    NOT NULL,
    `has_discount`           bit             NOT NULL DEFAULT 0 COMMENT '`0`: Không khuyến mại, `1`: Có khuyến mại',
    `discount_percentage`    int                      DEFAULT NULL,
    `price`                  DECIMAL(10, 2)  NOT NULL COMMENT 'Giá sản phẩm',
    `stock`                  int             NOT NULL COMMENT 'Số lượng hàng tồn',
    `deleted`                bit             NOT NULL DEFAULT 0 COMMENT 'Đánh dấu trạng thái xóa của bản ghi: `0`: Chưa xóa, `1`: Đã xóa',
    `created_at`             datetime        NOT NULL,
    `updated_at`             datetime        NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
    FOREIGN KEY (`cover_image`) REFERENCES `upload_files` (`id`)
) ENGINE = InnoDB COMMENT 'Sản phẩm biến thể'
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- CREATE TABLE `promotion`
-- (
--     `id`                     int unsigned    NOT NULL AUTO_INCREMENT,
--     `name`                   varchar(255)    NOT NULL,
--     `start_time`             BIGINT          NOT NULL COMMENT 'Ngày bắt đầu khuyển mãi',
--     `end_time`               BIGINT          NOT NULL COMMENT 'Ngày kết thúc khuyến mãi',
--     `deleted`                bit             NOT NULL DEFAULT 0 COMMENT 'Đánh dấu trạng thái xóa của bản ghi: `0`: Chưa xóa, `1`: Đã xóa',
--     `created_at`             datetime        NOT NULL,
--     `updated_at`             datetime        NOT NULL,
--     PRIMARY KEY (`id`)
-- ) ENGINE = InnoDB COMMENT 'Khuyến mãi'
--   DEFAULT CHARSET = utf8mb4
--   COLLATE = utf8mb4_0900_ai_ci;
--
-- CREATE TABLE `promotion_model`
-- (
--     `id`                  int unsigned NOT NULL AUTO_INCREMENT,
--     `promotion_id`        int unsigned NOT NULL COMMENT 'Khóa ngoại tham chiếu đến khuyến mãi',
--     `model_id`            int unsigned NOT NULL COMMENT 'Khóa ngoại tham chiếu đến sản phẩm biến thể',
--     `discount_percentage` int          DEFAULT NULL COMMENT 'Phần trăm giảm giá riêng cho model này (nếu có)',
--     `deleted`             bit          NOT NULL DEFAULT 0 COMMENT 'Đánh dấu trạng thái xóa của bản ghi: `0`: Chưa xóa, `1`: Đã xóa',
--     `created_at`          datetime     NOT NULL,
--     `updated_at`          datetime     NOT NULL,
--     PRIMARY KEY (`id`),
--     FOREIGN KEY (`promotion_id`) REFERENCES `promotion` (`id`),
--     FOREIGN KEY (`model_id`) REFERENCES `model` (`id`)
-- ) ENGINE = InnoDB COMMENT 'Khuyến mãi áp dụng cho sản phẩm biến thể'
--   DEFAULT CHARSET = utf8mb4
--   COLLATE = utf8mb4_0900_ai_ci;


-- CREATE TABLE `merchant_branches`
-- (
--     `id`          int unsigned NOT NULL AUTO_INCREMENT,
--     `merchant_id` int unsigned NOT NULL,
--     `address`     varchar(255)          DEFAULT NULL COMMENT 'Địa chỉ',
--     `description` TEXT                  DEFAULT NULL,
--     `deleted`     bit          NOT NULL DEFAULT 0,
--     `created_at`  datetime     NOT NULL,
--     `updated_at`  datetime     NOT NULL,
--     PRIMARY KEY (`id`),
--     FOREIGN KEY (`merchant_id`) REFERENCES `merchants` (`id`)
-- ) ENGINE = InnoDB COMMENT 'Quản lý cửa hàng'
--   DEFAULT CHARSET = utf8mb4
--   COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `permissions`
(
    `id`                int unsigned NOT NULL AUTO_INCREMENT,
    `title`             varchar(255)          DEFAULT NULL COMMENT 'Lưu trữ tiêu đề hoặc tên của quyền, giúp mô tả ngắn gọn về quyền đó',
    `permission`        varchar(255) NOT NULL COMMENT 'Lưu trữ tên quyền, dùng để xác định quyền trong hệ thống. Đây là một giá trị duy nhất trong bảng, đảm bảo không có hai quyền trùng nhau.',
    `parent_permission` varchar(255)          DEFAULT NULL COMMENT 'Lưu trữ quyền cha (parent permission), nếu quyền này thuộc một quyền cấp cao hơn nó',
    `can_view`          bit                   DEFAULT NULL COMMENT '`0`: là không có quyền xem, `1`: là có quyền xem',
    `can_write`         bit                   DEFAULT NULL COMMENT '`0`: là không có quyền ghi, `1`: là có quyền ghi',
    `can_approval`      bit                   DEFAULT NULL COMMENT '`0`: là không có quyền duyệt, `1`: là có quyền duyệt',
    `can_decision`      bit                   DEFAULT NULL COMMENT '`0`: là không có quyền xóa, `1`: là có quyền xóa',
    `type`              int          NOT NULL COMMENT '0 - SUPER_ADMIN, 1 - USER, 2 - ACCOUNT_ADMIN, 3 - PRODUCT_ADMIN, 4 - POST_ADMIN',
    `status`            int          NOT NULL DEFAULT '1' COMMENT 'Trạng thái của quyền. `1`: Đang hoạt động, `0`: Không hoạt động',
    `deleted`           bit          NOT NULL DEFAULT 0,
    `created_at`        timestamp    NULL NOT NULL,
    `updated_at`        timestamp    NULL NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `permission` (`permission`, `type`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `roles`
(
    `id`         int unsigned NOT NULL AUTO_INCREMENT,
    `object_id`  int unsigned NOT NULL COMMENT 'Khóa ngoại tham chiếu đến nhà cung cấp nội dung/ thiết bị/ bản quyền/ cửa hàng tương ứng với trường type',
    `name`       varchar(255) NOT NULL COMMENT 'Tên của vai trò',
    `note`       TEXT                  DEFAULT NULL COMMENT 'Ghi chú bổ sung về vai trò',
    `type`       int          NOT NULL COMMENT '0 - SUPER_ADMIN, 1 - USER, 2 - ACCOUNT_ADMIN, 3 - PRODUCT_ADMIN, 4 - POST_ADMIN',
    `status`     int          NOT NULL DEFAULT '1' COMMENT 'Trạng thái hoạt động: `0`: Không hoạt động, 1: Hoạt động',
    `deleted`    bit          NOT NULL DEFAULT b'0',
    `created_at` timestamp    NULL     DEFAULT NULL,
    `updated_at` timestamp    NULL     DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `role_permission`
(
    `id`            int unsigned NOT NULL AUTO_INCREMENT,
    `role_id`       int unsigned NOT NULL COMMENT 'Khóa ngoại tham chiếu đến vai trò',
    `permission_id` int unsigned NOT NULL COMMENT 'Khóa ngoại tham chiếu đến quyền hạn',
    `can_view`      bit                   DEFAULT NULL COMMENT 'Quyền xem: 0 - Không có quyền, 1 - Có quyền',
    `can_write`     bit                   DEFAULT NULL COMMENT 'Quyền viết: 0 - Không có quyền, 1 - Có quyền',
    `can_approval`  bit                   DEFAULT NULL COMMENT 'Quyền duyệt: 0 - Không có quyền, 1 - Có quyền',
    `can_decision`  bit                   DEFAULT NULL COMMENT 'Quyền xóa: 0 - Không có quyền, 1 - Có quyền',
    `deleted`       bit          NOT NULL DEFAULT 0,
    `created_at`    timestamp    NULL     DEFAULT NULL,
    `updated_at`    timestamp    NULL     DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY (`role_id`, `permission_id`),
    FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
    FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `users`
(
    `id`         int unsigned NOT NULL AUTO_INCREMENT,
    `code`       varchar(50)  NOT NULL COMMENT 'Mã người dùng duy nhất',
    `phone`      varchar(20)  NOT NULL,
    `email`      varchar(255)          DEFAULT NULL,
    `name`       varchar(255)          DEFAULT NULL,
    `password`   varchar(255)          DEFAULT NULL COMMENT 'Mật khẩu của người dùng',
    `birthday`   date                  DEFAULT NULL,
    `gender`     int                   DEFAULT NULL,
    `role_id`    int unsigned NOT NULL COMMENT 'Khóa ngoại tham chiếu đến vai trò của người dùng',
    `avatar_id`  int unsigned          DEFAULT NULL COMMENT 'Khóa ngoại tham chiếu đến hình đại diện của người dùng',
    `status`     int          NOT NULL COMMENT 'Trạng thái hoạt động của người dùng (0: Không hoạt động, 1: Hoạt động)',
    `deleted`    bit(1)       NOT NULL DEFAULT 0,
    `created_at` datetime     NOT NULL,
    `updated_at` datetime     NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY (`code`),
    UNIQUE KEY (`phone`),
    FOREIGN KEY (`avatar_id`) REFERENCES `upload_files` (`id`),
    FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE = InnoDB COMMENT ='Quan ly tai khoan nguoi dung truy cap dich vu'
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `address`
(
    `id`                     int unsigned    NOT NULL AUTO_INCREMENT,
    `user_id`                int unsigned    NOT NULL COMMENT 'Khóa ngoại tham chiếu đến người dùng',
    `consignee`              varchar(255)    NOT NULL,
    `phone`                  varchar(20)     NOT NULL,
    `address`                varchar(255)             DEFAULT NULL,
    `type`                   bit             NOT NULL DEFAULT 0 COMMENT 'Loại địa chỉ: `0`: Địa chỉ thường, `1`: Địa chỉ mặc định',
    `deleted`                bit             NOT NULL DEFAULT 0 COMMENT 'Đánh dấu trạng thái xóa của bản ghi: `0`: Chưa xóa, `1`: Đã xóa',
    `created_at`             datetime        NOT NULL,
    `updated_at`             datetime        NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE = InnoDB COMMENT 'Địa chỉ'
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- CREATE TABLE `devices`
-- (
--     `id`                 int unsigned NOT NULL AUTO_INCREMENT,
--     `code`               varchar(50)  NOT NULL COMMENT 'Mã thiết bị duy nhất',
--     `name`               varchar(255)          DEFAULT NULL,
--     `model`              varchar(255)          DEFAULT NULL,
--     `serial`             varchar(255)          DEFAULT NULL,
--     `provider_device_id` int unsigned NOT NULL,
--     `status`             int          NOT NULL COMMENT 'Trạng thái hoạt động của người dùng (0: Không hoạt động, 1: Hoạt động)',
--     `deleted`            bit(1)       NOT NULL DEFAULT 0,
--     `created_at`         datetime     NOT NULL,
--     `updated_at`         datetime     NOT NULL,
--     PRIMARY KEY (`id`),
--     UNIQUE KEY (`code`),
--     FOREIGN KEY (`provider_device_id`) REFERENCES `provider_devices` (`id`)
-- ) ENGINE = InnoDB
--   DEFAULT CHARSET = utf8mb4
--   COLLATE = utf8mb4_0900_ai_ci;

-- CREATE TABLE `songs`
-- (
--     `id`                  int unsigned NOT NULL AUTO_INCREMENT,
--     `code`                varchar(50)  NOT NULL COMMENT 'Mã bài hát duy nhất',
--     `provider_content_id` int unsigned NOT NULL,
--     `name`                varchar(255)          DEFAULT NULL,
--     `singer`              varchar(255)          DEFAULT NULL,
--     `author`              varchar(255)          DEFAULT NULL,
--     `created_by`          int unsigned NOT NULL,
--     `updated_by`          int unsigned NOT NULL,
--     `status`              int          NOT NULL,
--     `deleted`             bit(1)       NOT NULL DEFAULT 0,
--     `created_at`          datetime     NOT NULL,
--     `updated_at`          datetime     NOT NULL,
--     PRIMARY KEY (`id`),
--     UNIQUE KEY (`code`),
--     FOREIGN KEY (`provider_content_id`) REFERENCES `provider_contents` (`id`),
--     FOREIGN KEY (`created_by`) REFERENCES `users` (`id`),
--     FOREIGN KEY (`updated_by`) REFERENCES `users` (`id`)
-- ) ENGINE = InnoDB
--   DEFAULT CHARSET = utf8mb4
--   COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `transactions`
(
    `id`              int unsigned NOT NULL AUTO_INCREMENT,
    `code`            varchar(50)  NOT NULL,
    `reference_code`  varchar(255)          DEFAULT NULL,
    `user_id`         int unsigned NOT NULL,
    `amount`          bigint       NOT NULL DEFAULT 0,
    `payment_gateway` int          NOT NULL,
    `note`            text,
    `pay_date`        datetime              DEFAULT NULL,
    `description`     text,
    `type`            int                   DEFAULT '0',
    `status`          int          NOT NULL,
    `deleted`         bit(1)       NOT NULL DEFAULT b'0',
    `created_at`      datetime     NOT NULL,
    `updated_at`      datetime     NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY (`code`),
    UNIQUE KEY (`reference_code`, `payment_gateway`),
            FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;



CREATE TABLE `otp`
(
    id            int unsigned NOT NULL AUTO_INCREMENT COMMENT 'Id phien xac thuc',
    phone         varchar(20)           DEFAULT NULL COMMENT 'So dien thoai nhan Otp',
    email         varchar(255)          DEFAULT NULL COMMENT 'Email nhan Otp',
    otp           varchar(10)  NOT NULL COMMENT 'Ma OTP',
    attempt_count int          NOT NULL DEFAULT 3 COMMENT 'So lan xac thuc cua phien, toi da 3 lan',
    send_type     int          NOT NULL DEFAULT 0 COMMENT 'Kenh gui Otp: 0 - ZNS, 1 - Email, 2 - SMS ',
    status        int          NOT NULL DEFAULT 0 COMMENT 'Trang thai phien xac thuc: 0 - verify pending, 1 - verified, 2 - failed, 3 - expired, 4 - other',
    deleted       bit          NOT NULL DEFAULT 0,
    created_at    timestamp    NOT NULL,
    updated_at    timestamp    NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB COMMENT ='Quan ly gui Otp'
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE qrtz_scheduler_job_info
(
    id              int unsigned NOT NULL AUTO_INCREMENT,
    job_name        varchar(255)          DEFAULT NULL,
    job_group       varchar(255)          DEFAULT NULL,
    job_class       varchar(255)          DEFAULT NULL,
    cron_expression varchar(255)          DEFAULT NULL,
    repeat_time     bigint                DEFAULT NULL,
    cron_job        BOOLEAN               DEFAULT NULL,
    deleted         bit          NOT NULL DEFAULT 0,
    created_at      timestamp    NOT NULL,
    updated_at      timestamp    NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE QRTZ_CALENDARS
(
    SCHED_NAME    VARCHAR(120) NOT NULL,
    CALENDAR_NAME VARCHAR(200) NOT NULL,
    CALENDAR      BLOB         NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE QRTZ_CRON_TRIGGERS
(
    SCHED_NAME      VARCHAR(120) NOT NULL,
    TRIGGER_NAME    VARCHAR(200) NOT NULL,
    TRIGGER_GROUP   VARCHAR(200) NOT NULL,
    CRON_EXPRESSION VARCHAR(120) NOT NULL,
    TIME_ZONE_ID    VARCHAR(80)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE QRTZ_FIRED_TRIGGERS
(
    SCHED_NAME        VARCHAR(120) NOT NULL,
    ENTRY_ID          VARCHAR(95)  NOT NULL,
    TRIGGER_NAME      VARCHAR(200) NOT NULL,
    TRIGGER_GROUP     VARCHAR(200) NOT NULL,
    INSTANCE_NAME     VARCHAR(200) NOT NULL,
    FIRED_TIME        BIGINT       NOT NULL,
    SCHED_TIME        BIGINT       NOT NULL,
    PRIORITY          INTEGER      NOT NULL,
    STATE             VARCHAR(16)  NOT NULL,
    JOB_NAME          VARCHAR(200) NULL,
    JOB_GROUP         VARCHAR(200) NULL,
    IS_NONCONCURRENT  BOOLEAN      NULL,
    REQUESTS_RECOVERY BOOLEAN      NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS
(
    SCHED_NAME    VARCHAR(120) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE QRTZ_SCHEDULER_STATE
(
    SCHED_NAME        VARCHAR(120) NOT NULL,
    INSTANCE_NAME     VARCHAR(200) NOT NULL,
    LAST_CHECKIN_TIME BIGINT       NOT NULL,
    CHECKIN_INTERVAL  BIGINT       NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE QRTZ_LOCKS
(
    SCHED_NAME VARCHAR(120) NOT NULL,
    LOCK_NAME  VARCHAR(40)  NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE QRTZ_JOB_DETAILS
(
    SCHED_NAME        VARCHAR(120) NOT NULL,
    JOB_NAME          VARCHAR(200) NOT NULL,
    JOB_GROUP         VARCHAR(200) NOT NULL,
    DESCRIPTION       VARCHAR(250) NULL,
    JOB_CLASS_NAME    VARCHAR(250) NOT NULL,
    IS_DURABLE        BOOLEAN      NOT NULL,
    IS_NONCONCURRENT  BOOLEAN      NOT NULL,
    IS_UPDATE_DATA    BOOLEAN      NOT NULL,
    REQUESTS_RECOVERY BOOLEAN      NOT NULL,
    JOB_DATA          BLOB         NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE QRTZ_SIMPLE_TRIGGERS
(
    SCHED_NAME      VARCHAR(120) NOT NULL,
    TRIGGER_NAME    VARCHAR(200) NOT NULL,
    TRIGGER_GROUP   VARCHAR(200) NOT NULL,
    REPEAT_COUNT    BIGINT       NOT NULL,
    REPEAT_INTERVAL BIGINT       NOT NULL,
    TIMES_TRIGGERED BIGINT       NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE QRTZ_SIMPROP_TRIGGERS
(
    SCHED_NAME    VARCHAR(120)   NOT NULL,
    TRIGGER_NAME  VARCHAR(200)   NOT NULL,
    TRIGGER_GROUP VARCHAR(200)   NOT NULL,
    STR_PROP_1    VARCHAR(512)   NULL,
    STR_PROP_2    VARCHAR(512)   NULL,
    STR_PROP_3    VARCHAR(512)   NULL,
    INT_PROP_1    INTEGER        NULL,
    INT_PROP_2    INTEGER        NULL,
    LONG_PROP_1   BIGINT         NULL,
    LONG_PROP_2   BIGINT         NULL,
    DEC_PROP_1    NUMERIC(13, 4) NULL,
    DEC_PROP_2    NUMERIC(13, 4) NULL,
    BOOL_PROP_1   BOOLEAN        NULL,
    BOOL_PROP_2   BOOLEAN        NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE QRTZ_BLOB_TRIGGERS
(
    SCHED_NAME    VARCHAR(120) NOT NULL,
    TRIGGER_NAME  VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    BLOB_DATA     BLOB         NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE QRTZ_TRIGGERS
(
    SCHED_NAME     VARCHAR(120) NOT NULL,
    TRIGGER_NAME   VARCHAR(200) NOT NULL,
    TRIGGER_GROUP  VARCHAR(200) NOT NULL,
    JOB_NAME       VARCHAR(200) NOT NULL,
    JOB_GROUP      VARCHAR(200) NOT NULL,
    DESCRIPTION    VARCHAR(250) NULL,
    NEXT_FIRE_TIME BIGINT       NULL,
    PREV_FIRE_TIME BIGINT       NULL,
    PRIORITY       INTEGER      NULL,
    TRIGGER_STATE  VARCHAR(16)  NOT NULL,
    TRIGGER_TYPE   VARCHAR(8)   NOT NULL,
    START_TIME     BIGINT       NOT NULL,
    END_TIME       BIGINT       NULL,
    CALENDAR_NAME  VARCHAR(200) NULL,
    MISFIRE_INSTR  SMALLINT     NULL,
    JOB_DATA       BLOB         NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

ALTER TABLE QRTZ_CALENDARS
    ADD PRIMARY KEY (SCHED_NAME, CALENDAR_NAME);

ALTER TABLE QRTZ_CRON_TRIGGERS
    ADD PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);

ALTER TABLE QRTZ_FIRED_TRIGGERS
    ADD PRIMARY KEY (SCHED_NAME, ENTRY_ID);

ALTER TABLE QRTZ_PAUSED_TRIGGER_GRPS
    ADD PRIMARY KEY (SCHED_NAME, TRIGGER_GROUP);

ALTER TABLE QRTZ_SCHEDULER_STATE
    ADD PRIMARY KEY (SCHED_NAME, INSTANCE_NAME);

ALTER TABLE QRTZ_LOCKS
    ADD PRIMARY KEY (SCHED_NAME, LOCK_NAME);

ALTER TABLE QRTZ_JOB_DETAILS
    ADD PRIMARY KEY (SCHED_NAME, JOB_NAME, JOB_GROUP);

ALTER TABLE QRTZ_SIMPLE_TRIGGERS
    ADD PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);

ALTER TABLE QRTZ_SIMPROP_TRIGGERS
    ADD PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);

ALTER TABLE QRTZ_TRIGGERS
    ADD PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);

ALTER TABLE QRTZ_CRON_TRIGGERS
    ADD FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) REFERENCES QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) ON DELETE CASCADE;

ALTER TABLE QRTZ_SIMPLE_TRIGGERS
    ADD FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) REFERENCES QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) ON DELETE CASCADE;

ALTER TABLE QRTZ_SIMPROP_TRIGGERS
    ADD FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) REFERENCES QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) ON DELETE CASCADE;

ALTER TABLE QRTZ_TRIGGERS
    ADD FOREIGN KEY (SCHED_NAME, JOB_NAME, JOB_GROUP) REFERENCES QRTZ_JOB_DETAILS (SCHED_NAME, JOB_NAME, JOB_GROUP);

INSERT INTO permissions (`id`, `title`, `permission`, `parent_permission`, `can_view`, `can_write`, `can_approval`,
                         `can_decision`, `type`, `status`, `created_at`, `updated_at`)
VALUES (1, 'Tổng quan', 'DASHBOARD', 'STATISTIC', true, null, null, null, 0, 1, now(), now()),
       (2, 'Quản lý tài khoản', 'ACCOUNT', 'CONFIG', true, true, true, true, 0, 1, now(), now()),
       (3, 'Quản lý phân quyền', 'ROLE', 'CONFIG', true, true, true, true, 0, 1, now(), now()),
       (4, 'Quản lý sản phẩm', 'PRODUCT', 'FEATURE', true, true, true, true, 0, 1, now(), now()),
       (5, 'Quản lý bài đăng', 'POST', 'FEATURE', true, true, true, true, 0, 1, now(), now()),
       (6, 'Quản lý giao dịch', 'TRANSACTION', 'FEATURE', true, true, true, true, 0, 1, now(), now()),

--        (1, 'Tổng quan', 'DASHBOARD', 'STATISTIC', true, null, null, null, 1, 1, now(), now()),
--        (2, 'Quản lý tài khoản', 'ACCOUNT', 'CONFIG', true, true, true, true, 1, 1, now(), now()),
--        (3, 'Quản lý phân quyền', 'ROLE', 'CONFIG', true, true, true, true, 1, 1, now(), now()),
--        (4, 'Quản lý sản phẩm', 'PRODUCT', 'FEATURE', true, true, true, true, 1, 1, now(), now()),
--        (5, 'Quản lý bài đăng', 'POST', 'FEATURE', true, true, true, true, 1, 1, now(), now()),
--        (5, 'Quản lý giao dịch', 'TRANSACTION', 'FEATURE', true, true, true, true, 1, 1, now(), now()),

       (7, 'Tổng quan', 'DASHBOARD', 'STATISTIC', true, null, null, null, 2, 1, now(), now()),
       (8, 'Quản lý tài khoản', 'ACCOUNT', 'CONFIG', true, true, true, true, 2, 1, now(), now()),
       (9, 'Quản lý phân quyền', 'ROLE', 'CONFIG', true, true, true, true, 2, 1, now(), now()),
       (10, 'Quản lý giao dịch', 'TRANSACTION', 'FEATURE', true, true, true, true, 2, 1, now(), now()),

       (11, 'Tổng quan', 'DASHBOARD', 'STATISTIC', true, null, null, null, 3, 1, now(), now()),
       (12, 'Quản lý sản phẩm', 'PRODUCT', 'FEATURE', true, true, true, true, 3, 1, now(), now()),
       (13, 'Quản lý giao dịch', 'TRANSACTION', 'FEATURE', true, true, true, true, 3, 1, now(), now()),

       (14, 'Tổng quan', 'DASHBOARD', 'STATISTIC', true, null, null, null, 4, 1, now(), now()),
       (15, 'Quản lý bài đăng', 'POST', 'FEATURE', true, true, true, true, 4, 1, now(), now()),
       (16, 'Quản lý giao dịch', 'TRANSACTION', 'FEATURE', true, true, true, true, 4, 1, now(), now());

INSERT INTO roles (`id`, `name`, object_id, `type`, `status`, `created_at`, `updated_at`)
VALUES (1, 'Admin', 0, 0, 1, now(), now());

INSERT INTO role_permission (`role_id`, `permission_id`, `can_view`, `can_write`, `can_approval`, `can_decision`,
                             `created_at`, `updated_at`)
VALUES (1, 1, 1, null, null, null, now(), now()),
       (1, 2, 1, 1, 1, 1, now(), now()),
       (1, 3, 1, 1, 1, 1, now(), now()),
       (1, 4, 1, 1, 1, 1, now(), now()),
       (1, 5, 1, 1, 1, 1, now(), now()),
       (1, 6, 1, 1, 1, 1, now(), now());

INSERT INTO users (`id`, `code`, `name`, `phone`, `email`, `password`, `role_id`, `status`, `created_at`, `updated_at`)
VALUES (1, 'ABCDEFGH', 'Admin', '0365517544', 'admin@gmail.com',
        '$2a$10$fSP7.73InP1cNoVOdqt7P.mb/pzn93gPrKLOixxenooOP3D77hGF.', 1, 1,
        now(), now());

