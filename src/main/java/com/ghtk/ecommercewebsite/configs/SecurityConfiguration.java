package com.ghtk.ecommercewebsite.configs;

//import com.ghtk.ecommercewebsite.filters.CookieJwtFilter;
//import com.ghtk.ecommercewebsite.filters.CustomLogoutFilter;
import com.ghtk.ecommercewebsite.filters.JwtAuthenticationFilter;
import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.services.blacklisttoken.BlacklistTokenService;
import com.ghtk.ecommercewebsite.services.user.UserService;
import com.ghtk.ecommercewebsite.utils.WhitelistUrls;
import com.ghtk.ecommercewebsite.filters.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;
import java.util.Objects;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@EnableWebMvc
public class SecurityConfiguration {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(jwtAuthenticationFilter, LogoutFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .csrf(AbstractHttpConfigurer::disable)
//                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/auth/**",
                                "/api/v1/user/login",
                                "/api/v1/user/signup",
                                "/api/v1/seller/signup",
                                "/api/v1/seller/login",
                                "/api/v1/admin/login",
                                "/categories/**"
                                )
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessHandler(logoutSuccessHandler())
//                        .logoutSuccessUrl("/")
                        .deleteCookies("JWT_TOKEN")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll());
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (HttpServletRequest request,
                HttpServletResponse response,
                Authentication authentication) -> {
//            String redirectUrl;
//            System.out.println("qua lowps logoutttt");
//            Authentication a = SecurityContextHolder.getContext().getAuthentication();
//            if (authentication != null) {
//                String userRole = authentication.getAuthorities().stream()
//                        .map(Objects::toString)
//                        .findFirst()
//                        .orElse("");
//                redirectUrl = switch (userRole) {
//                    case "ROLE_ADMIN" -> "/admin/login";
//                    case "ROLE_SELLER" -> "/seller/login";
//                    default -> "/login";
//                };
//            } else {
//                redirectUrl = "/loginAgain";
//            }
//
            String redirectUrl = request.getParameter("redirectUrl");
            redirectUrl = switch (redirectUrl) {
                    case "/login-admin" -> "/logout-admin";
                    case "/login-seller" -> "/logout-seller";
                    default -> "/logout-user";
            };
//            response.setStatus(HttpServletResponse.SC_OK); // Đặt mã trạng thái HTTP 200 OK
//            response.setHeader("Location", redirectUrl); // Đặt tiêu đề Location cho redirect
            response.sendRedirect(redirectUrl);
        };
    }
}
