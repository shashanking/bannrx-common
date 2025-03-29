package com.bannrx.common.securityfilters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.bannrx.common.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import rklab.utility.services.JWTService;
import rklab.utility.services.JwtConfiguration;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final JWTService jwtService;
    private final JwtConfiguration jwtConfiguration;


    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final int TOKEN_START_INDEX = 7;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var authHeader = request.getHeader(AUTHORIZATION);
        String token = null;
        if (Objects.nonNull(authHeader) && authHeader.startsWith(BEARER)){
            token = authHeader.substring(TOKEN_START_INDEX);
        }
        if (Objects.nonNull(token) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())){
            var userId = jwtService.extractUsername(token, jwtConfiguration);
            var userMyBe = userService.getById(userId);
            userMyBe.ifPresent(user -> setSecurityContextHolder(request, user));
        }
        filterChain.doFilter(request, response);
    }

    private void setSecurityContextHolder(HttpServletRequest request, UserDetails userDetails){
        var authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

}

