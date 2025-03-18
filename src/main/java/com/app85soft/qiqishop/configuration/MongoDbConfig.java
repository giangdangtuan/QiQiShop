package com.app85soft.qiqishop.configuration;

import com.app85soft.qiqishop.component.converter.EnumMongoConverter;
import com.app85soft.qiqishop.dto.constant.BaseEnum;
import com.app85soft.qiqishop.util.EnumHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class MongoDbConfig {
    @Bean
    public MongoCustomConversions customConversions() {
        List<Object> converters = new ArrayList<>();

        // Lấy tất cả Enum kế thừa BaseEnum
        Set<Class<? extends BaseEnum>> enumClasses = EnumHelper.getAllEnums();

        converters.add(new EnumMongoConverter.EnumToConverter());
        for (Class<? extends BaseEnum> enumClass : enumClasses) {
            converters.add(new EnumMongoConverter.ToEnumConverter(enumClass));
        }
        return new MongoCustomConversions(converters);
    }
}
