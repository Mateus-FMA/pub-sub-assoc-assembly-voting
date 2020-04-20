package com.mateusfma.assemblyvoting.db;

import com.mateusfma.assemblyvoting.repository.converter.VoteReadConverter;
import com.mateusfma.assemblyvoting.repository.converter.VoteWriteConverter;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.r2dbc.spi.ConnectionFactoryOptions.DATABASE;
import static io.r2dbc.spi.ConnectionFactoryOptions.DRIVER;
import static io.r2dbc.spi.ConnectionFactoryOptions.HOST;
import static io.r2dbc.spi.ConnectionFactoryOptions.PASSWORD;
import static io.r2dbc.spi.ConnectionFactoryOptions.PORT;
import static io.r2dbc.spi.ConnectionFactoryOptions.USER;

@Configuration
@EnableR2dbcRepositories
public class DatabaseConfiguration extends AbstractR2dbcConfiguration {

    private static final Pattern URL_PATTERN =
            Pattern.compile("^jdbc:postgresql://(([\\w]+.)*[\\w]+):([\\d]+)/([\\w]+)$");

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public ConnectionFactory connectionFactory() {
        Matcher m = URL_PATTERN.matcher(dbUrl);
        if (!m.find())
            throw new RuntimeException("Database URL configuration is invalid.");

        return ConnectionFactories.get(
                ConnectionFactoryOptions.builder()
                        .option(DRIVER, "postgresql")
                        .option(USER, username)
                        .option(PASSWORD, password)
                        .option(HOST, m.group(1))
                        .option(PORT, Integer.valueOf(m.group(3)))
                        .option(DATABASE, m.group(4))
                        .build());
    }

}