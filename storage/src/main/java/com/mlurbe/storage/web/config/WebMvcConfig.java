package com.mlurbe.storage.web.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@EnableWebMvc
@ComponentScan({ "com.mlurbe.storage.service", "com.mlurbe.storage.persistence", "com.mlurbe.storage.models", "com.mlurbe.storage.web" })
@Configuration
@EnableTransactionManagement
@PropertySource("classpath:application.propierties")
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("classpath:schema.sql")
    private Resource schemaSql;

    @Value("${datasource.password}")
    private String DB_PASS;

    @Value("${datasource.username}")
    private String DB_USER;

    @Value("${datasource.url}")
    private String DB_URL;

    private static final Logger LOGGER = LoggerFactory.getLogger(WebMvcConfig.class);

    @Bean
    public DataSource dataSource () {
        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriverClass(com.mysql.cj.jdbc.Driver.class);

        String url = DB_URL;
        String pass = DB_PASS;
        String user = DB_USER;
        LOGGER.info("DB USER: " + user + " PASS: " + pass + " URL: " + url);
        ds.setUrl(url);
        ds.setUsername(user);
        ds.setPassword(pass);
        LOGGER.info("Se inicia el dataSource en " + ds.getUrl());
        return ds;
    }

    @Bean
    @Autowired
    public PlatformTransactionManager transactionManager (final EntityManagerFactory em) {
        return new JpaTransactionManager(em);
    }

    private DatabasePopulator databasePopulator () {
        final ResourceDatabasePopulator dbp = new ResourceDatabasePopulator();
        dbp.addScript(schemaSql);
        LOGGER.info("Se inicia el DatabasePopulator");
        return dbp;
    }

    @Bean
    public DataSourceInitializer dataSourceInitializer (final DataSource ds) {
        final DataSourceInitializer dsi = new DataSourceInitializer();
        dsi.setDataSource(ds);
        dsi.setDatabasePopulator(databasePopulator());
        LOGGER.info("Se inicia el DataSourceInitializer");
        return dsi;
    }

    @Bean(name = "entityManagerFactory")
    @Autowired
    public LocalContainerEntityManagerFactoryBean entityManagerFactory () {
        final LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setPackagesToScan("com.mlurbe.storage.models");// , "com.mlurbe.storage.service", "com.mlurbe.storage.persistence", "com.mlurbe.storage.web");
        factoryBean.setDataSource(dataSource());
        final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        factoryBean.setJpaVendorAdapter(vendorAdapter);
        final Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        // Si ponen esto en prod, hay tabla!!!
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("format_sql", "true");
        factoryBean.setJpaProperties(properties);
        return factoryBean;
    }

    @Override
    public void addResourceHandlers (ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("/webjars/");
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");
        LOGGER.debug("Se inicia el addResourceHandlers");
    }

}
