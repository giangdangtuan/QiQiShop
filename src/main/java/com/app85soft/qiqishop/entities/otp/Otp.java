package com.app85soft.qiqishop.entities.otp;


import com.app85soft.qiqishop.dto.constant.SendType;
import com.app85soft.qiqishop.dto.constant.VerifyStatus;
import com.app85soft.qiqishop.entities.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "otp")
@Entity
public class Otp extends BaseEntity {
    String otp;
    String phone;
    String email;

    @Column(name = "attempt_count")
    int attemptCount = 3;

    @Column(name = "send_type", columnDefinition = "INT")
    SendType sendType;

    @Column(name = "status", columnDefinition = "INT")
    VerifyStatus status;

    boolean deleted;
}
