package com.app85soft.qiqishop.component.converter;

import com.app85soft.qiqishop.dto.constant.BaseEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

@Component
@Converter
public abstract class EnumEntityConverter<T extends BaseEnum<U>, U> implements AttributeConverter<T, U> {
    private final Class<T> clazz;

    public EnumEntityConverter(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public U convertToDatabaseColumn(T t) {
        if (t == null) {
            return null;
        }
        return t.toValue();
    }

    @Override
    public T convertToEntityAttribute(U s) {
        try {
            for (T enumConstant : clazz.getEnumConstants()) {
                if (String.valueOf(s).equals(String.valueOf(enumConstant.toValue()))) {
                    return enumConstant;
                }
            }
        } catch (Exception ignored){
        }
        return null;
    }
}