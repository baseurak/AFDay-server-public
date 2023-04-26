package com.baseurak.AFDay.common;
import com.baseurak.AFDay.user.CustomAuthenticationProvider;
import com.baseurak.AFDay.user.CustomOAuth2UserService;
import com.baseurak.AFDay.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * 스프링 시큐리티 설정입니다.
 * @author Ru, Uju
 */
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) { // 4
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/").permitAll()
                //회원
                .antMatchers(HttpMethod.GET,"/user**").permitAll()
                .antMatchers(HttpMethod.POST,"/user**").permitAll()
                .antMatchers(HttpMethod.DELETE, "/user").hasAnyAuthority("USER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/login**").permitAll()
                //퀴즈
                .antMatchers(HttpMethod.GET,"/quiz**").permitAll()
                .antMatchers(HttpMethod.POST,"/quiz**").hasAnyAuthority("USER", "ADMIN")
                .antMatchers(HttpMethod.PUT,"/quiz**").hasAnyAuthority("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE,"/quiz**").hasAnyAuthority("USER", "ADMIN")
                //퀴즈그룹
                .antMatchers(HttpMethod.GET,"/quiz-group**").permitAll()
                .antMatchers(HttpMethod.POST,"/quiz-group**").permitAll()
                .antMatchers(HttpMethod.PUT,"/quiz-group**").hasAnyAuthority("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE,"/quiz-group**").hasAnyAuthority("USER", "ADMIN")
                //제출
                .antMatchers(HttpMethod.GET,"/score**").permitAll()
                .antMatchers(HttpMethod.POST,"/score**").hasAnyAuthority("USER", "ADMIN")
                .antMatchers(HttpMethod.GET,"/record**").hasAnyAuthority("USER", "ADMIN")
                .antMatchers(HttpMethod.GET,"/result**").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable();
        http
                .cors().configurationSource(corsConfigurationSource());
        http
                .formLogin()
                .loginPage("/login")
                .permitAll();
        http
                .logout()
                .deleteCookies("JSESSIONID")
                .permitAll();

        http
                .oauth2Login()
                .defaultSuccessUrl("https://www.afday.kro.kr", true)
                .userInfoEndpoint()
                .userService(customOAuth2UserService);
//        http
//                .rememberMe()
//                .tokenValiditySeconds(604800);
        http
                .sessionManagement().maximumSessions(10);

    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://www.afday.kro.kr"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
}
