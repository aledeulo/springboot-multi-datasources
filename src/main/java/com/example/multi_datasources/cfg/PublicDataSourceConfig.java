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
        basePackages = "com.example.multi_datasources.repo.publicUser",
        entityManagerFactoryRef = "publicUserEntityManagerFactory"
)
public class PublicDataSourceConfig {
    @Bean(name = "publicUserDSProps")
    @ConfigurationProperties("spring.datasource.public")
    public DataSourceProperties publicUserDSProps() {
        return new DataSourceProperties();
    }

    @Bean(name = "publicUserDataSource")
    public DataSource publicUserDataSource(@Qualifier("publicUserDSProps") DataSourceProperties publicUserDSProps) {
        PGXADataSource ds = new PGXADataSource();
        ds.setUrl(publicUserDSProps.getUrl());
        ds.setUser(publicUserDSProps.getUsername());
        ds.setPassword(publicUserDSProps.getPassword());

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(ds);
        xaDataSource.setUniqueResourceName("xa_public");
        return xaDataSource;
    }

    @Bean(name = "publicUserEntityManager")
    @DependsOn("transactionManager")
    public LocalContainerEntityManagerFactoryBean accountEntityManager(
            EntityManagerFactoryBuilder builder,
            @Qualifier("publicUserDataSource") DataSource dataSource) {
        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
        properties.put("javax.persistence.transactionType", "JTA");

        return builder
                .dataSource(dataSource)
                .packages("com.example.multi_datasources.model")
                .persistenceUnit("publicUserPU")
                .jta(true)
                .properties(properties)
                .build();
    }
}
