package com.taldate.backend.auth.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Component
@AllArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;

    /**
     * Called before passing the request to controllers
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        Optional<String> jwt = getToken(request);
        if (jwt.isPresent()) {
            Claims tokenBody = jwtUtils.parseToken(jwt.get());

            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(buildAuthToken(tokenBody));
        }

        filterChain.doFilter(request, response);
    }

    private Optional<String> getToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return Optional.empty();
        }
        return Optional.of(header.substring("Bearer ".length()));
    }

    private UsernamePasswordAuthenticationToken buildAuthToken(Claims claims) {
        // set info (can be on dto) of logged in user
        return new UsernamePasswordAuthenticationToken(
                claims.get("userId")/*info of logged in user, can be DTO*/,
                null,
                List.of(/*new SimpleGrantedAuthority("USER")*/)
        );
    }
}