package com.zhacky.ninjapos.config;

import com.zhacky.ninjapos.config.filter.JwtAuthorizationTokenFilter;
import com.zhacky.ninjapos.service.JwtUserDetailsService;
import com.zhacky.ninjapos.config.handler.SuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    JwtAuthorizationTokenFilter authorizationTokenFilter;

    SuccessHandler successHandler;
    SimpleUrlAuthenticationFailureHandler failureHandler;

    @Autowired
    JwtUserDetailsService jwtUserDetailsService;

    /**
     * http security configuration
     * @param http  HttpSecurity object to be configured
     * @see HttpSecurity
     * @throws Exception If an error occurs when configuring headers/cors
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        successHandler = new SuccessHandler();
        failureHandler = new SimpleUrlAuthenticationFailureHandler();
        http
                .headers()
                .xssProtection().disable();// API does not require XSS protection
        http        // allow cross origin
                .cors()
                .and()// we don't need CSRF
                .csrf().disable()// if there's any exception, respond with UNAUTHORIZED
                .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()// don't create session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests() // intercept urls
                .antMatchers("/auth").permitAll()
                .antMatchers("/auth/**").permitAll()
//                .antMatchers("/products/**").permitAll()
                .anyRequest().authenticated();

        http
                .addFilterBefore(authorizationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        //http//disable page caching
//                .frameOptions().and().cacheControl()
//                .and()
    }

    /**
     * web security configuration
     * @param web WebSecurity object to b configured
     * @see WebSecurity
     */
    @Override
    public void configure(WebSecurity web) {

        web
                .ignoring()
                .antMatchers(
                        HttpMethod.POST,
                        "/auth"
                )
                .and()
                .ignoring()
                .antMatchers(
                        "/auth",
                        "/register",
                        "/*.html",
                        "/*.ico",
                        "csrf",
                        "/webjars/springfox-swagger-ui/**","/v2/**","/swagger-resources/**"
                );
    }

    /**
     * auth manager configuration
     * @param auth AuthenticationManagerBuilder object to be configured globally
     * @throws Exception If an error occurs when adding the UserDetailsService based authentication
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(jwtUserDetailsService)
                .passwordEncoder(encoder());
    }

    // region Beans
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    // endregion
}
