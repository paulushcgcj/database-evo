package io.github.paulushcgcj.configuration;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
    entityManagerFactoryRef = "centralEntityMgrFactory",
    transactionManagerRef = "centralTransactionMgr",
    basePackages = { "io.github.paulushcgcj.repositories.companies" }
)
@EnableTransactionManagement
@Profile("multidb")
public class CentralDataConfiguration {

  @Bean(name = "centralEntityMgrFactory")
  @Primary
  public LocalContainerEntityManagerFactoryBean centralEntityMgrFactory(
      final EntityManagerFactoryBuilder builder,
      @Qualifier("centralDatasource") final DataSource dataSource
  ) {
    return builder
        .dataSource(dataSource)
        .properties(new HashMap<>())
        .packages("io.github.paulushcgcj.entities.companies")
        .persistenceUnit("company")
        .build();
  }

  @Bean(name = "centralDatasource")
  @ConfigurationProperties(prefix = "io.github.paulushcgcj.database.central")
  @Primary
  public DataSource centralDatasource() { return DataSourceBuilder.create().build(); }

  @Bean(name = "centralTransactionMgr")
  @Primary
  public PlatformTransactionManager centralTransactionMgr(
      @Qualifier("centralEntityMgrFactory") final EntityManagerFactory entityManagerFactory
  ) {
    return new JpaTransactionManager(entityManagerFactory);
  }

}
