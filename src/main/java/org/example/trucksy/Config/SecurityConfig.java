package org.example.trucksy.Config;

import lombok.RequiredArgsConstructor;
import org.example.trucksy.Service.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final MyUserDetailsService myUserDetailsService;

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(myUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(daoAuthenticationProvider())
                .authorizeHttpRequests()

                // =========  (Sign up) =========
                .requestMatchers("/api/v1/client/add").permitAll()
                .requestMatchers("/api/v1/owner/add").permitAll()


                // قراشيع مدفوعات حسان خليتها للكل
                .requestMatchers("/api/v1/order/callback/**").permitAll()
                .requestMatchers("/api/v1/owner/callback/**").permitAll()

                // Swagger
                .requestMatchers("/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html").permitAll()

                // ========= ADMIN =========
                .requestMatchers("/api/v1/auth/get").hasAuthority("ADMIN")
                .requestMatchers("/api/v1/auth/delete/**").hasAuthority("ADMIN")
                .requestMatchers("/api/v1/auth/get-all-owners").hasAuthority("ADMIN")
                .requestMatchers("/api/v1/auth/get-all-clients").hasAuthority("ADMIN")
                .requestMatchers("/api/v1/auth/delete-foodTruck/**").hasAuthority("ADMIN")

                // ========= CLIENT =========
                .requestMatchers("/api/v1/client/update/**").hasAuthority("CLIENT")
                .requestMatchers("/api/v1/client/delete/**").hasAuthority("CLIENT")
                .requestMatchers("/api/v1/client/update-client-location/**").hasAuthority("CLIENT")
                .requestMatchers("/api/v1/order/add/**").hasAuthority("CLIENT")
                .requestMatchers("/api/v1/order/client/**").hasAuthority("CLIENT")
                .requestMatchers("/api/v1/review/add/**").hasAuthority("CLIENT")
                .requestMatchers("/api/v1/review/get-reviews-by-client/**").hasAuthority("CLIENT")
                .requestMatchers("/api/v1/foodTruck/get-foodTrucks-by-category/**").hasAuthority("CLIENT")
                .requestMatchers("/api/v1/foodTruck/get-nearest/**").hasAuthority("CLIENT")
                .requestMatchers("/api/v1/item/filterByPrice/**").hasAuthority("CLIENT")

                // ========= OWNER =========
                .requestMatchers("/api/v1/dashboard/**").hasAuthority("OWNER")
                .requestMatchers("/api/v1/foodTruck/add/**").hasAuthority("OWNER")
                .requestMatchers("/api/v1/foodTruck/update/**").hasAuthority("OWNER")
                .requestMatchers("/api/v1/foodTruck/delete/**").hasAuthority("OWNER")
                .requestMatchers("/api/v1/foodTruck/get-all-trucks-by-owner_id/**").hasAuthority("OWNER")
                .requestMatchers("/api/v1/foodTruck/update-food-truck-location/**").hasAuthority("OWNER")
                .requestMatchers("/api/v1/foodTruck/open-foodTruck/**").hasAuthority("OWNER")
                .requestMatchers("/api/v1/foodTruck/close-foodTruck/**").hasAuthority("OWNER")
                ///api/v1/dashboard/analyze-dashboard
                .requestMatchers("/api/v1/dashboard/analyze-dashboard/**").hasAuthority("OWNER")


                // ========= OWNER =========
                .requestMatchers("/api/v1/item/**").hasAuthority("OWNER")
                .requestMatchers("/api/v1/discount/**").hasAuthority("OWNER")


                .requestMatchers("/api/v1/order/status/**").hasAuthority("OWNER")
                .requestMatchers("/api/v1/order/foodtruck/**").hasAuthority("OWNER")

                // reviews for all
                .requestMatchers(HttpMethod.GET, "/api/v1/review/get-reviews-by-truck/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/review/get-truck-rating/**").permitAll()


                .anyRequest().authenticated()
                .and()
                .logout().logoutUrl("/api/v1/auth/logout")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .and()
                .httpBasic();

        return http.build();
    }


}
