package org.springframework.security.samples.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

@EnableWebSecurity
@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private DataSource dataSource;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/csrf").permitAll();
		http.authorizeRequests().antMatchers("/css/**").permitAll();
		http.authorizeRequests().antMatchers("/js/**").permitAll();
		http.authorizeRequests().antMatchers("/asserts/**").permitAll();
//		http.authorizeRequests().antMatchers("/user/**").hasRole("USER");
		http.authorizeRequests().antMatchers("/user/**").hasAuthority("USER_AUTHORITY");
		http.authorizeRequests().antMatchers("/gs-guide-websocket").hasAuthority("USER_AUTHORITY");
		http.authorizeRequests().antMatchers("/gs-guide-websocket/**").hasAuthority("USER_AUTHORITY");
		http.authorizeRequests().antMatchers("/topic/**").hasAuthority("USER_AUTHORITY"); // /topic/greetings
		http.authorizeRequests().antMatchers("/app/**").hasAuthority("USER_AUTHORITY"); // /app/hello
		http.authorizeRequests().antMatchers("/ws/**").hasAuthority("USER_AUTHORITY");
		http.authorizeRequests().antMatchers("/welcome").hasAuthority("USER_AUTHORITY");
		http.authorizeRequests().antMatchers("/welcome").hasAuthority("USER_AUTHORITY");
		
		http.formLogin().loginPage("/login").failureUrl("/login-error");
		System.out.println("SecurityConfig configure");
		
	}
	
	@Bean
	public JdbcUserDetailsManager jdbcUserDetailsManager() {
		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
		jdbcUserDetailsManager.setDataSource(dataSource);
		jdbcUserDetailsManager.setEnableGroups(true);
		jdbcUserDetailsManager.setEnableAuthorities(false);
		return jdbcUserDetailsManager;
	} 
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)
			throws Exception {
//		auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");
		auth.userDetailsService(jdbcUserDetailsManager()); // Enable Group
//		auth.jdbcAuthentication().dataSource(dataSource);
	}
}