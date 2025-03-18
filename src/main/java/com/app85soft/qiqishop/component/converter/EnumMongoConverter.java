package com.app85soft.qiqishop.component.converter;

import com.app85soft.qiqishop.dto.constant.BaseEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import java.util.Set;

public class EnumMongoConverter {
    private static Set<Class<? extends BaseEnum>> allEnums;

    @WritingConverter
    public static class EnumToConverter implements Converter<BaseEnum<?>, Object> {

        @Override
        public Object convert(BaseEnum<?> source) {
            return source.toValue();
        }
    }

    @ReadingConverter
    public static class ToEnumConverter<T extends BaseEnum> implements Converter<Object, T> {
        Class<T> clazz;

        public ToEnumConverter(Class<T> clazz) {
            this.clazz = clazz;
        }

        @Override
        public T convert(Object source) {
            try {
                for (T enumConstant : clazz.getEnumConstants()) {
                    if (String.valueOf(source).equals(String.valueOf(enumConstant.toValue()))) {
                        return enumConstant;
                    }
                }
            } catch (Exception ignored) {
            }
            return null;
        }
    }
}