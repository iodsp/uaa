package org.iodsp.uaa.configure;

import org.iodsp.uaa.service.UcUserSerivce;
import org.iodsp.uaa.service.UcUserSerivceOauth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// @Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Autowired
    UcUserSerivceOauth ucUserSerivce;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(ucUserSerivce).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.formLogin().loginPage("/login").permitAll()
                .and().httpBasic()
                .and().csrf().ignoringAntMatchers("/client/**", "/h2-console/**", "/user/**", "/resource/**", "/scope/**", "/role/**"
                    , "/authority/**", "/user/**")
                .and().headers().frameOptions().sameOrigin()
                .and()
                .authorizeRequests()
                .antMatchers("/static/**", "/fonts/**", "/js/**", "/css/**", "/h2-console/**", "/client/**",
                        "/role/**", "/authority/**", "/resource/**", "/scope/**", "/user/**")
                .permitAll()
                .anyRequest().authenticated();
    }
}
