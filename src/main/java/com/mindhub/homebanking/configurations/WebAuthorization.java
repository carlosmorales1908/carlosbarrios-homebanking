package com.mindhub.homebanking.configurations;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;

@EnableWebSecurity
@Configuration
public class WebAuthorization {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .requestMatchers("/web/public/**","/web/css/**","/web/img/**","/web/js/**").permitAll()
                .requestMatchers(HttpMethod.POST,"/api/login","/api/logout").permitAll()
                .requestMatchers("/manager.html").hasAuthority("ADMIN")
                .requestMatchers("/web/accounts.html").hasAuthority("USER")
                .requestMatchers("/web/**").hasAnyAuthority("ADMIN","USER");
                //.requestMatchers("/**").hasAuthority("USER")


        //http.securityMatcher("/api/**")
        //     .authorizeHttpRequests(authorize -> authorize
        //        .requestMatchers("/user/**").hasRole("USER")
        //        .requestMatchers("/admin/**").hasRole("ADMIN")
        //        .anyRequest().authenticated());

        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");

        http.logout().logoutUrl("/api/logout");

        // turn off checking for CSRF tokens

        http.csrf().disable();



        //disabling frameOptions so h2-console can be accessed

        http.headers().frameOptions().disable();

        // if user is not authenticated, just send an authentication failure response

        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication

        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response

        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response

        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());



        return http.build();
    }


    /* EJEMPLOS SACADOS DE LA DOCUMENTACION OFICIAL DE SPRING Y TAMPOCO FUNCIONARON
    https://docs.spring.io/spring-security/reference/6.1-SNAPSHOT/servlet/authorization/authorize-http-requests.html


    @Bean
    SecurityFilterChain web(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/endpoint").hasAuthority("USER")
                        .anyRequest().authenticated()
                );
        return http.build();
    }
    */
    /*
    @Bean
    SecurityFilterChain appEndpoints(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http.authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(mvc.pattern("/api/web/**")).hasAuthority("USER")
                        .anyRequest().authenticated()
                );

        return http.build();
    }
     */

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}
