package com.example.multi_datasources.cfg;

import org.postgresql.xa.PGXADataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@DependsOn("transactionManager")
@EnableJpaRepositories(
        basePackages = "com.example.multi_datasources.repo.publicUser",
        entityManagerFactoryRef = "publicUserEntityManager"
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
            JpaVendorAdapter jpaVendorAdapter,
            @Qualifier("publicUserDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setJtaDataSource(dataSource);
        em.setJpaVendorAdapter(jpaVendorAdapter);
        em.setPackagesToScan("com.example.multi_datasources.model");
        em.setPersistenceUnitName("publicUserPU");
        em.setJpaPropertyMap(Map.of(
                "hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName(),
                "javax.persistence.transactionType", "JTA"
        ));
        return em;
    }
}
