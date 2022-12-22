package com.example.multi_datasources.dbConfig;

import com.example.multi_datasources.model.PublicUser;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.example.multi_datasources.repo.publicUser",
        entityManagerFactoryRef = "publicUserEntityManagerFactory",
        transactionManagerRef= "publicUserTransactionManager"
)
public class PublicDataSourceConfig {
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.public")
    public DataSourceProperties publicUserDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource publicUserDataSource() {
        return publicUserDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Primary
    @Bean(name = "publicUserEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean publicUserEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(publicUserDataSource())
                .packages(PublicUser.class)
                .build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager publicUserTransactionManager(
            final @Qualifier("publicUserEntityManagerFactory") LocalContainerEntityManagerFactoryBean publicUserEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(publicUserEntityManagerFactory.getObject()));
    }
}
