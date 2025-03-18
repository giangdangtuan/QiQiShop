package com.app85soft.qiqishop.component.converter;

import com.app85soft.qiqishop.util.Aes;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@Converter
public class DataSensitiveConverter implements AttributeConverter<String, String> {

    @Value("${app.data-sensitive.secret-key}")
    private String secretKey;

    @Override
    public String convertToDatabaseColumn(String s) {
        try {
            return Aes.encrypt(s, secretKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String convertToEntityAttribute(String encrypt) {
        try {
            return Aes.decrypt(encrypt, secretKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
