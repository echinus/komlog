package com.twock.komlog;

import java.util.Properties;
import javax.inject.Inject;
import javax.sql.DataSource;

import org.apache.commons.dbcp.cpdsadapter.DriverAdapterCPDS;
import org.apache.commons.dbcp.datasources.SharedPoolDataSource;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.*;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Chris Pearson
 */
@Configuration
@PropertySource("classpath:/komlog.properties")
@ComponentScan(basePackages = "com.twock.komlog")
@EnableTransactionManagement
public class ProxyConfig {
  @Inject
  private org.springframework.core.env.Environment env;

  @Bean(autowire = Autowire.BY_NAME)
  public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
    LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
    factoryBean.setDataSource(proxydb());
    factoryBean.setPackagesToScan("com.twock.komlog.map");
    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    vendorAdapter.setDatabasePlatform(env.getProperty("hibernate.dialect"));
//    vendorAdapter.setShowSql(true);
    factoryBean.setJpaVendorAdapter(vendorAdapter);
    Properties properties = new Properties();
    properties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
//    properties.setProperty("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory");
//    properties.setProperty("javax.persistence.sharedCache.mode", "ALL");
//    properties.setProperty("hibernate.cache.use_second_level_cache", "true");
//    properties.setProperty("hibernate.cache.use_query_cache", "true");
    factoryBean.setJpaProperties(properties);
    return factoryBean;
  }

  @Bean(autowire = Autowire.BY_NAME)
  public PlatformTransactionManager transactionManager() {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setDataSource(proxydb());
    return transactionManager;
  }

  @Bean
  public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
    return new PersistenceExceptionTranslationPostProcessor();
  }

  @Bean(destroyMethod = "close")
  public DataSource proxydb() {
    return createPool(env.getProperty("map.jdbc.driver"), env.getProperty("map.jdbc.url"), env.getProperty("map.jdbc.user"), env.getProperty("map.jdbc.password"));
  }

  private DataSource createPool(String driver, String url, String user, String pass) {
    DriverAdapterCPDS cpds = new DriverAdapterCPDS();
    try {
      cpds.setDriver(driver);
    } catch(ClassNotFoundException e) {
      throw new RuntimeException("Failed to find driver class " + driver, e);
    }
    cpds.setUrl(url);
    cpds.setUser(user);
    cpds.setPassword(pass);

    SharedPoolDataSource tds = new SharedPoolDataSource();
    tds.setConnectionPoolDataSource(cpds);
    tds.setMaxActive(10);
    tds.setMaxWait(50);
    return tds;
  }
}
