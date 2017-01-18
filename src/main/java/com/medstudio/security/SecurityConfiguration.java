package com.medstudio.security;

import com.medstudio.MedStudioApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import javax.sql.DataSource;

/**
 * Created by Savek on 2016-11-17.
 */

@Configuration
@ComponentScan(basePackageClasses = MedStudioApplication.class)
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {

        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
        db.setDataSource(dataSource);
        return db;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder())
                .usersByUsernameQuery("select login as username, password, enabled FROM user where login = ?")
                .authoritiesByUsernameQuery("select u.login as username, r.role as role_name from role r" +
                                " join user_roles ur on r.id = ur.roles_id " +
                                " join user u on ur.user_id = u.id" +
                        " where u.login = ?");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .httpBasic()
            .and()
                .authorizeRequests()
                .antMatchers("/getPatients/**").hasRole("DOC")
                .antMatchers("/index.html", "/home.html", "/login.html","/js/*", "/js/*/*", "/", "/css/*", "/img/*", "/fonts/*", "/templates/*").permitAll()
                .anyRequest().fullyAuthenticated()
            .and()
                .formLogin()
                .usernameParameter("username")
                .passwordParameter("password")
            .and()
                .logout()
            .and()
                .csrf().csrfTokenRepository(csrfTokenRepository())
                //.csrf().disable();
            .and()
                .addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class);

    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }
}