package com.library.libraryprojectapi.configuration;

import com.library.libraryprojectapi.authentication.service.JwtService;
import com.library.libraryprojectapi.authentication.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final MyUserDetailsService myUserDetailsService;

    @Autowired
    public JwtFilter(JwtService jwtService, MyUserDetailsService myUserDetailsService){
        this.jwtService = jwtService;
        this.myUserDetailsService = myUserDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        String authenticator = null;

        if(authorizationHeader != null && !authorizationHeader.startsWith("Bearer ")){
            token = authorizationHeader;
        }else if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }

        if(token != null){
            authenticator = jwtService.extractAuthenticator(token);
        }

        if(authenticator !=  null && SecurityContextHolder.getContext().getAuthentication() == null){
            try {
                UserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticator);
                if(jwtService.validateToken(token)){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(userDetails);

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized: Invalid or expired token");
                response.getWriter().flush();
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
