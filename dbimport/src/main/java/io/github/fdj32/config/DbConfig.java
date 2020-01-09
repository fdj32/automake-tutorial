package io.github.fdj32.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DbConfig {

    @Bean(name = "odsDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.ods")
    public DataSource odsDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "odsJdbcTemplate")
    public JdbcTemplate odsJdbcTemplate(@Qualifier("odsDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "mysqlDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.mysql")
    public DataSource mysqlDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "mysqlJdbcTemplate")
    public JdbcTemplate mysqlJdbcTemplate(@Qualifier("mysqlDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "pgsqlDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.pgsql")
    public DataSource pgsqlDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "pgsqlJdbcTemplate")
    public JdbcTemplate pgsqlJdbcTemplate(@Qualifier("pgsqlDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
