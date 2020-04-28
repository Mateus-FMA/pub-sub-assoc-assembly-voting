package com.mateusfma.assemblyvoting.configuration;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

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

    @NotNull
    @Bean
    public ConnectionFactory connectionFactory() {
        int index = dbUrl.indexOf('?');
        String url = index == -1 ? dbUrl : dbUrl.substring(0, index);
        Matcher m = URL_PATTERN.matcher(url);
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
