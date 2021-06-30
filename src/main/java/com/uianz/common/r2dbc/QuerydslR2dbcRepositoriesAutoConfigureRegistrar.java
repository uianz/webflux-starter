package com.uianz.common.r2dbc;

import com.infobip.spring.data.r2dbc.EnableQuerydslR2dbcRepositories;
import org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

import java.lang.annotation.Annotation;

/**
 * @author uianz
 * @date 2021/6/25
 */
class QuerydslR2dbcRepositoriesAutoConfigureRegistrar extends AbstractRepositoryConfigurationSourceSupport {

    @Override
    protected Class<? extends Annotation> getAnnotation() {
        return EnableR2dbcRepositories.class;
    }

    @Override
    protected Class<?> getConfiguration() {
        return EnableR2dbcRepositoriesConfiguration.class;
    }

    @Override
    protected RepositoryConfigurationExtension getRepositoryConfigurationExtension() {
        return new QuerydslR2dbcRepositoryConfigurationExtension();
    }

    @EnableQuerydslR2dbcRepositories
    private static class EnableR2dbcRepositoriesConfiguration {

    }

}
