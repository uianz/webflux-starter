package com.uianz.common.r2dbc;

import com.infobip.spring.data.r2dbc.R2dbcConfiguration;
import com.querydsl.sql.PostgreSQLTemplates;
import com.querydsl.sql.SQLTemplates;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.r2dbc.R2dbcRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

/**
 * @author uianz
 * @date 2021/6/25
 */
@Import({R2dbcConfiguration.class, QuerydslR2dbcRepositoriesAutoConfigureRegistrar.class})
@AutoConfigureBefore(R2dbcRepositoriesAutoConfiguration.class)
@Configuration
public class QuerydslR2dbcRepositoriesAutoConfiguration {

    @Autowired
    private Environment env;

    @Bean
    public SQLTemplates sqlTemplates(){
        return PostgreSQLTemplates.DEFAULT;
    }

    @Bean(initMethod = "migrate")
    public Flyway flyway() {
        return new Flyway(Flyway.configure()
                .baselineOnMigrate(true)
                .dataSource(
                        env.getRequiredProperty("spring.flyway.url"),
                        env.getRequiredProperty("spring.flyway.user"),
                        env.getRequiredProperty("spring.flyway.password"))
        );
    }
}
