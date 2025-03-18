package com.app85soft.qiqishop.dto.response.zns;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZnsResponse {
    private String id;
    @JsonProperty("shop_id")
    private String shopId;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("oa_id")
    private String oaId;
    @JsonProperty("zl_user_id")
    private String zlUserId;

    private String phone;

    @JsonProperty("msg_id")
    private String msgId;

    private String type;

    @JsonProperty("campaign_id")
    private String campaignId;
    @JsonProperty("template_id")
    private int templateId;
    @JsonProperty("template_data")
    private TemplateData templateData;
    @JsonProperty("journey_id")
    private String journeyId;
    @JsonProperty("tracking_id")
    private String trackingId;
    @JsonProperty("fee_main")
    private int feeMain;
    @JsonProperty("fee_token")
    private int feeToken;

    private int timeout;

    @JsonProperty("sent_time")
    private String sentTime;
    @JsonProperty("delivery_time")
    private String deliveryTime;
    @JsonProperty("delivery_status")
    private String deliveryStatus;
    @JsonProperty("is_charged")
    private boolean isCharged;

    private String status;
    private int rate;
    private String note;
    private List<String> feedback;

    @JsonProperty("submit_time")
    private String submitTime;
    @JsonProperty("error_code")
    private int errorCode;
    @JsonProperty("error_message")
    private String errorMessage;
    @JsonProperty("enable_sms_on_zns_failure")
    private boolean enableSmsOnZnsFailure;

    private Object sms;

    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("created_at")
    private String createdAt;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TemplateData {
        private String otp;
    }
}
