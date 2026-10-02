package com.library.librarysystem.security;

import java.io.IOException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.library.librarysystem.security.JwtUtl;
import com.library.librarysystem.service.CustomUserDetailsService;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtUtl jwtUtl;
	
	@Autowired
	private UserDetailsService userDetailsService ;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request,
	                                HttpServletResponse response,
	                                FilterChain filterChain)
	        throws ServletException, IOException {

	    if (request.getRequestURI().contains("/api/auth")) {
	        filterChain.doFilter(request, response);
	        return;
	    }

	    final String authHeader = request.getHeader("Authorization");

	    String email = null;
	    String token = null;

	    if (authHeader != null && authHeader.startsWith("Bearer ")) {

	        token = authHeader.substring(7);
	        email = jwtUtl.extractEmail(token);
	    }

	    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

	        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

	        if (jwtUtl.validateToken(token, userDetails.getUsername())) {

	            UsernamePasswordAuthenticationToken authToken =
	                    new UsernamePasswordAuthenticationToken(
	                            userDetails,
	                            null,
	                            userDetails.getAuthorities()
	                    );

	            authToken.setDetails(
	                    new WebAuthenticationDetailsSource().buildDetails(request)
	            );

	            SecurityContextHolder.getContext().setAuthentication(authToken);

	            System.out.println("Logged in user: " + userDetails.getUsername());
	            System.out.println("Authorities: " + userDetails.getAuthorities());
	        }
	    }

	    filterChain.doFilter(request, response);
	}	

}
