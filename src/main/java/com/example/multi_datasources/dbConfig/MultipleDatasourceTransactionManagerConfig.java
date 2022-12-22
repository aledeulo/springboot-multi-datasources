package com.example.multi_datasources.dbConfig;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class MultipleDatasourceTransactionManagerConfig {
    @Bean(name = "chainedTransactionManager")
    public ChainedTransactionManager chainedTransactionManager(
            @Qualifier("publicUserTransactionManager")
            PlatformTransactionManager publicUserTransactionManager,
            @Qualifier("secondaryUserTransactionManager")
            PlatformTransactionManager secondaryUserTransactionManager ){
        return new ChainedTransactionManager(publicUserTransactionManager,secondaryUserTransactionManager);
    }
}
