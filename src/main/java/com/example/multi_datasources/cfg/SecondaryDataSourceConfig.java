package com.example.multi_datasources.cfg;

import org.hibernate.engine.transaction.jta.platform.internal.AtomikosJtaPlatform;
import org.postgresql.xa.PGXADataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@DependsOn("transactionManager")
@EnableJpaRepositories(
        basePackages = "com.example.multi_datasources.repo.secondaryUser",
        entityManagerFactoryRef = "secondaryUserEntityManagerFactory"
)
public class SecondaryDataSourceConfig {
    @Bean(name = "secondaryUserDSProps")
    @ConfigurationProperties("spring.datasource.secondary")
    public DataSourceProperties secondaryUserDSProps() {
        return new DataSourceProperties();
    }

    @Bean(name = "secondaryUserDataSource")
    public DataSource secondaryUserDataSource(@Qualifier("secondaryUserDSProps") DataSourceProperties secondaryUserDSProps) {
        PGXADataSource ds = new PGXADataSource();
        ds.setUrl(secondaryUserDSProps.getUrl());
        ds.setUser(secondaryUserDSProps.getUsername());
        ds.setPassword(secondaryUserDSProps.getPassword());

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(ds);
        xaDataSource.setUniqueResourceName("xa_secondary");
        return xaDataSource;
    }

    @Bean(name = "secondaryUserEntityManager")
    @DependsOn("transactionManager")
    public LocalContainerEntityManagerFactoryBean accountEntityManager(
            EntityManagerFactoryBuilder builder,
            @Qualifier("secondaryUserDataSource") DataSource dataSource) {
        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
        properties.put("javax.persistence.transactionType", "JTA");

        return builder
                .dataSource(dataSource)
                .packages("com.example.multi_datasources.model.secondary")
                .persistenceUnit("secondaryUserPU")
                .jta(true)
                .properties(properties)
                .build();
    }
}
