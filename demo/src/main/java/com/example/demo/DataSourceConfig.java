package com.example.demo;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

	@Value("${database.url}")
	private String dbUrl;
	
	@Value("${database.username}")
	private String username;
	
	@Value("${database.password}")
	private String password;
	
    @Bean
    @Primary
    public DataSource dataSource() {

        HikariConfig hikariConfig = new HikariConfig();

        // ---- JDBC basics ----
        hikariConfig.setDriverClassName("org.postgresql.Driver");
        hikariConfig.setJdbcUrl(dbUrl);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);

        // ---- HikariCP tuning (Neon-safe) ----
        hikariConfig.setMaximumPoolSize(5);
        hikariConfig.setMinimumIdle(1);
        hikariConfig.setIdleTimeout(30_000);
        hikariConfig.setMaxLifetime(600_000);
        hikariConfig.setConnectionTimeout(10_000);
        hikariConfig.setPoolName("NeonHikariPool");

        // ---- PostgreSQL-specific optimizations ----
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        return new HikariDataSource(hikariConfig);
    }
}
