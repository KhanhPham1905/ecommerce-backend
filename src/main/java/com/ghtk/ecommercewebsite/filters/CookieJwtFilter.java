//package com.ghtk.ecommercewebsite.filters;
//
//import com.ghtk.ecommercewebsite.security.TokenBlackList;
//import com.ghtk.ecommercewebsite.services.JwtService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.lang.NonNull;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//import org.springframework.web.servlet.HandlerExceptionResolver;
//
//import java.io.IOException;
//import java.util.Arrays;
//
//@Component
//@RequiredArgsConstructor
//public class CookieJwtFilter extends OncePerRequestFilter {
//
//    private final HandlerExceptionResolver handlerExceptionResolver;
//    private final JwtService jwtService;
//    private final UserDetailsService userDetailsService;
//    private final TokenBlackList tokenBlackList;
//
//    @Override
//    protected void doFilterInternal(@NonNull HttpServletRequest request,
//                                    @NonNull HttpServletResponse response,
//                                    @NonNull  FilterChain filterChain) throws ServletException, IOException {
//        try {
//            final Cookie[] cookies = request.getCookies();
//            String jwt = null;
//            if (cookies != null) {
//                jwt = Arrays.stream(cookies)
//                        .filter(cookie -> "JWT_TOKEN".equals(cookie.getName()))
//                        .map(Cookie::getValue)
//                        .findFirst()
//                        .orElse(null);
//            }
//
//            if (tokenBlackList.isBlacklisted(jwt)) {
//                filterChain.doFilter(request, response);
//                return;
//            }
//            final String userEmail = jwtService.extractUsername(jwt);
//
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            if (userEmail != null && authentication == null) {
//                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
//                if (jwtService.isTokenValid(jwt, userDetails)) {
//                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                            userDetails,
//                            null,
//                            userDetails.getAuthorities()
//                    );
//                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    SecurityContextHolder.getContext().setAuthentication(authToken);
//                }
//            }
//            filterChain.doFilter(request, response);
//        } catch (Exception exception) {
//            handlerExceptionResolver.resolveException(request, response, null, exception);
//        }
//    }
//}
