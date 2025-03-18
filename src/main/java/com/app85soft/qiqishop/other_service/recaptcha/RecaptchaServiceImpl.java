package com.app85soft.qiqishop.other_service.recaptcha;

import com.app85soft.qiqishop.dto.response.RecaptchaResponse;
import com.app85soft.qiqishop.exceptions.BusinessException;
import com.app85soft.qiqishop.services.BaseService;
import com.app85soft.qiqishop.util.Util;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@Service
class RecaptchaServiceImpl extends BaseService implements RecaptchaService {

    @Value("${recaptcha.secret_key}")
    private String SECRET_KEY;
    @Value("${system.enironment.dev-mode}")
    private boolean isDeveloping;

    public void verifyToken(String token) {
        if (isDeveloping) {
            return;
        }
        final String URL = "https://www.google.com/recaptcha/api/siteverify";
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("response", token)
                .addFormDataPart("secret", SECRET_KEY)
                .build();
        Request request = new Request.Builder()
                .addHeader("Content-Type", "application/json")
                .url(Objects.requireNonNull(HttpUrl.parse(URL)).newBuilder().build().toString())
                .post(body)
                .build();
        String json = null;
        try (Response response = new OkHttpClient().newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                json = responseBody.string();
            }
        } catch (IOException e) {
            throw new BusinessException(e.getMessage());
        }
        RecaptchaResponse recaptchaResponse = Util.stringToObject(RecaptchaResponse.class, json);
        if (recaptchaResponse == null || !recaptchaResponse.getSuccess()) {
            throw new BusinessException("Recaptcha error");
        }
    }
}
