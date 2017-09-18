package org.iodsp.uaa.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("reader")
                .password("reader")
                .authorities("ROLE_READER")
                .and()
                .withUser("guest")
                .password("guest")
                .authorities("ROLE_GUEST");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.formLogin().loginPage("/login").permitAll()
                .and().httpBasic()
                .and().csrf().ignoringAntMatchers("/client/**", "/h2-console/**")
                .and().headers().frameOptions().sameOrigin()
                .and()
                .authorizeRequests()
                .antMatchers("/static/**", "/fonts/**", "/js/**", "/css/**", "/h2-console/**").permitAll()
                .anyRequest().authenticated();
    }
}
