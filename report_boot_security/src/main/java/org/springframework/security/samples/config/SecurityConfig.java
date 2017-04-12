package org.springframework.security.samples.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dmds = new DriverManagerDataSource();
		dmds.setDriverClassName("com.mysql.jdbc.Driver");
		dmds.setUrl("jdbc:mysql://127.0.0.1:3306/report");
		dmds.setUsername("root");
		dmds.setPassword("root");
		return dmds;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/css/**", "/index").permitAll();
//		http.authorizeRequests().antMatchers("/user/**").hasRole("USER");
		http.authorizeRequests().antMatchers("/user/**").hasAuthority("USER_AUTHORITY");
		http.formLogin().loginPage("/login").failureUrl("/login-error");
	}
	
	@Bean
	public JdbcUserDetailsManager jdbcUserDetailsManager() {
		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
		jdbcUserDetailsManager.setDataSource(dataSource());
		jdbcUserDetailsManager.setEnableGroups(true);
		jdbcUserDetailsManager.setEnableAuthorities(false);
		return jdbcUserDetailsManager;
	} 
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)
			throws Exception {
//		auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");
		auth.userDetailsService(jdbcUserDetailsManager()); // Enable Group
		
		//auth.jdbcAuthentication().dataSource(dataSource());
	}
}