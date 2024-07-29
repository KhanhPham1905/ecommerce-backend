//package com.ghtk.ecommercewebsite.filters;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.lang.NonNull;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//@RequiredArgsConstructor
//public class CustomLogoutFilter extends OncePerRequestFilter {
//
//    @Override
//    protected void doFilterInternal(
//            @NonNull HttpServletRequest request,
//            @NonNull HttpServletResponse response,
//            @NonNull FilterChain filterChain) throws ServletException, IOException {
//        if ("/logout".equals(request.getRequestURI())) {
//            final String jwt = authHeader.substring(7);
//            if (blacklistTokenService.isBlacklisted(jwt)) {
//                filterChain.doFilter(request, response);
//                return;
//            }
//            final String userEmail = jwtService.extractUsername(jwt);
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            System.out.println("qua lop filtter customer" + authentication);
////            if (userEmai != null && authentication == null) {
////                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
////                if (jwtService.isTokenValid(jwt, userDetails)) {
////                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
////                            userDetails,
////                            null,
////                            userDetails.getAuthorities()
////                    );
////                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
////                    SecurityContextHolder.getContext().setAuthentication(authToken);
////                }
////            }
//            // Tiếp tục chuỗi filter
//            filterChain.doFilter(request, response);
//        } else {
//            // Nếu không phải yêu cầu logout, chỉ cần tiếp tục chuỗi filter
//            filterChain.doFilter(request, response);
//        }
//    }
//}
