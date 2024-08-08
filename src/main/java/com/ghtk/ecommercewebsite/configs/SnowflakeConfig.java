package com.ghtk.ecommercewebsite.configs;

import de.mkammerer.snowflakeid.SnowflakeIdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SnowflakeConfig {
    @Bean
    public SnowflakeIdGenerator snowflakeIdGenerator() {
        int generatorId = 0;
        return SnowflakeIdGenerator.createDefault(generatorId);
    }
}
