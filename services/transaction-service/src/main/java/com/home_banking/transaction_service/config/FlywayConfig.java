package com.home_banking.transaction_service.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
public class FlywayConfig {

    @Bean
    public Flyway flyway(DataSource dataSource) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .load();
        flyway.migrate();
        return flyway;
    }

    @Component
    static class EntityManagerFactoryDependsOnFlyway implements BeanDefinitionRegistryPostProcessor {

        @Override
        public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
            String emfBean = "entityManagerFactory";
            if (registry.containsBeanDefinition(emfBean)) {
                BeanDefinition bd = registry.getBeanDefinition(emfBean);
                String[] existing = bd.getDependsOn();
                if (existing == null) {
                    bd.setDependsOn("flyway");
                } else {
                    String[] updated = Arrays.copyOf(existing, existing.length + 1);
                    updated[existing.length] = "flyway";
                    bd.setDependsOn(updated);
                }
            }
        }

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        }
    }
}
