package io.github.paulushcgcj.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
    entityManagerFactoryRef = "externalEntityMgrFactory",
    transactionManagerRef = "externalTransactionMgr",
    basePackages = { "io.github.paulushcgcj.repositories.users" }
)
@EnableTransactionManagement
@Profile("multidb")
public class ExternalDataConfiguration {

  @Bean(name = "externalEntityMgrFactory")
  public LocalContainerEntityManagerFactoryBean externalEntityMgrFactory(
      final EntityManagerFactoryBuilder builder,
      @Qualifier("externalDatasource") final DataSource dataSource
  ) {
    return builder
        .dataSource(dataSource)
        .properties(new HashMap<>())
        .packages("io.github.paulushcgcj.entities.users")
        .persistenceUnit("company")
        .build();
  }

  @Bean(name = "externalDatasource")
  @ConfigurationProperties(prefix = "io.github.paulushcgcj.database.external")
  public DataSource externalDatasource() { return DataSourceBuilder.create().build(); }

  @Bean(name = "externalTransactionMgr")
  public PlatformTransactionManager externalTransactionMgr(
      @Qualifier("externalEntityMgrFactory") final EntityManagerFactory entityManagerFactory
  ) {
    return new JpaTransactionManager(entityManagerFactory);
  }

}
