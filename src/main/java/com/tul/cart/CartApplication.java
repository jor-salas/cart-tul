package com.tul.cart;

import org.modelmapper.AbstractConverter;
import org.modelmapper.AbstractProvider;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

@SpringBootApplication
public class CartApplication {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        Provider<LocalDate> localDateProvider = new AbstractProvider<LocalDate>() {
            @Override
            public LocalDate get() {
                return LocalDate.now();
            }
        };

        Converter<Instant, LocalDate> toStringDate = new AbstractConverter<Instant, LocalDate>() {
            @Override
            protected LocalDate convert(Instant source) {
                return source.atZone(ZoneOffset.UTC).toLocalDate();
            }
        };

        modelMapper.createTypeMap(Instant.class, LocalDate.class);
        modelMapper.addConverter(toStringDate);
        modelMapper.getTypeMap(Instant.class, LocalDate.class).setProvider(localDateProvider);

        return modelMapper;
    }

    public static void main(String[] args) {
        SpringApplication.run(CartApplication.class, args);
    }

}
