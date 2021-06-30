package com.uianz.common.r2dbc;

import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepositoryFactoryBean;
import org.springframework.data.r2dbc.repository.config.R2dbcRepositoryConfigurationExtension;

/**
 * @author uianz
 * @date 2021/6/25
 */
public class QuerydslR2dbcRepositoryConfigurationExtension extends R2dbcRepositoryConfigurationExtension {

    @Override
    public String getRepositoryFactoryBeanClassName() {
        return QuerydslR2dbcRepositoryFactoryBean.class.getName();
    }
}