package com.mlurbe.storage.web.config.security;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.mlurbe.storage.service.CustomUserDetailsService;
import com.mlurbe.storage.web.config.security.auth.RestAuthenticationEntryPoint;
import com.mlurbe.storage.web.config.security.auth.TokenAuthenticationFilter;

@Configuration
@EnableWebSecurity
@ComponentScan("com.mlurbe.storage.web.config")
// @EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Value("classpath:rememberme.key")
    private Resource rememberMeKey;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    TokenHelper tokenHelper;

    @Autowired
    private CorsFilter corsFilter;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Override
    protected void configure (HttpSecurity http) throws Exception {
        
        /*
         * http.sessionManagement() .sessionCreationPolicy(SessionCreationPolicy.STATELESS) .and() .addFilterBefore(corsFilter, ChannelProcessingFilter.class) .csrf()
         * .disable() // .and() .exceptionHandling() .authenticationEntryPoint(restAuthenticationEntryPoint) .and() .authorizeRequests() .antMatchers("/api/register")
         * .permitAll() .antMatchers("/api/auth/**") .permitAll() .antMatchers(HttpMethod.POST, "/register") .permitAll() // .antMatchers(HttpMethod.POST, "/users") //
         * .hasAnyRole("USER", "ADMIN") .anyRequest() // .permitAll() .authenticated() // TODO: añdir autenticacion. .and() .formLogin() .usernameParameter("username")
         * .passwordParameter("password") .and() .addFilterBefore(new TokenAuthenticationFilter(tokenHelper, userDetailsService), BasicAuthenticationFilter.class);
         */
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(corsFilter, ChannelProcessingFilter.class)
                .csrf()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers("/api/auth")
                .permitAll()
                .antMatchers("/api/auth/**")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/api/home")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/api/auth/login")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/api/register")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/api/documentos/upload")
                .permitAll()
                .antMatchers("/api/*")
                .permitAll()
                /*
                 * .antMatchers("/api/users") .authenticated() .antMatchers("/api/users/**") .authenticated() .antMatchers("/api/documentos") .authenticated()
                 * .antMatchers("/api/documentos/**") .authenticated() .antMatchers("/api/admin") .hasAnyRole(User.ROLE_ADMIN) .antMatchers("/api/admin/**")
                 * .hasAnyRole(User.ROLE_ADMIN)
                 */
                .and()
                .logout()
                .logoutUrl("/api/logout")
                .logoutSuccessUrl("/api/home")
                .permitAll();

        http.addFilterBefore(new TokenAuthenticationFilter(tokenHelper, userDetailsService), BasicAuthenticationFilter.class);
        
        http.csrf()
                .disable();
         
    }


    /*
     * @Override public void configure (final WebSecurity web) throws Exception { web.ignoring() .antMatchers("/resources/**", "/resources/**", "/resources/**",
     * "/resources/img/**", "/resources/**", "/favicon.ico"); }
     */

    @Bean
    public PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }

    /*
     * @Override protected void configure (AuthenticationManagerBuilder auth) throws Exception { auth.authenticationProvider(authProvider()); }
     */
    @Override
    public void configure (WebSecurity web) {
        // TokenAuthenticationFilter will ignore the below paths
        web.ignoring()
                .antMatchers(
                        HttpMethod.POST,
                        "/api/auth/login");

    }

    @Bean
    public DaoAuthenticationProvider authProvider () {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public String tokenSigningKey () {
        final StringWriter stringWriter = new StringWriter();
        try (Reader reader = new InputStreamReader(rememberMeKey.getInputStream())) {
            final char[] buf = new char[2048];
            int length;
            while ((length = reader.read(buf)) != -1) {
                stringWriter.write(buf, 0, length);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stringWriter.toString();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean () throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureGlobal (AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    ///////////////////////////////////////////////

    // @Bean
    // public PasswordEncoder passwordEncoder () {
    // return new BCryptPasswordEncoder();
    // }
    //
    // @Autowired
    // private CustomUserDetailsService jwtUserDetailsService;
    //
    // @Autowired
    // private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    //
    // @Autowired
    // TokenHelper tokenHelper;
    //
    // @Bean
    // @Override
    // public AuthenticationManager authenticationManagerBean () throws Exception {
    // return super.authenticationManagerBean();
    // }
    //
    // @Autowired
    // public void configureGlobal (AuthenticationManagerBuilder auth) throws Exception {
    // auth.userDetailsService(jwtUserDetailsService)
    // .passwordEncoder(passwordEncoder());
    // }
    //
    // @Override
    // protected void configure (HttpSecurity http) throws Exception {
    // http
    // .sessionManagement()
    // .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    // .and()
    // .exceptionHandling()
    // .authenticationEntryPoint(restAuthenticationEntryPoint)
    // .and()
    // .authorizeRequests()
    // .antMatchers("/auth/**")
    // .permitAll()
    // // .antMatchers(HttpMethod.POST, "/register")
    // // .permitAll()
    // // .antMatchers(HttpMethod.POST, "/users")
    // // .hasAnyRole("USER", "ADMIN")
    // .anyRequest()
    // .authenticated()
    // .and()
    // .addFilterBefore(new TokenAuthenticationFilter(tokenHelper, jwtUserDetailsService), BasicAuthenticationFilter.class);
    //
    // http.csrf()
    // .disable();
    // }
    //
    // @Override
    // public void configure (WebSecurity web) {
    // // TokenAuthenticationFilter will ignore the below paths
    // web.ignoring()
    // .antMatchers(
    // HttpMethod.POST,
    // "/auth/login");
    //
    // }
}
