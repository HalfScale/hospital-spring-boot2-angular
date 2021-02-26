package com.springboot.hospital.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.springboot.hospital.controller.UserRestController;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	Logger logger = LoggerFactory.getLogger(UserRestController.class);
	
	@Autowired
	private JwtProvider jwtProvider;
	
	@Autowired
	private UserDetailsService userDetailsSerivce;

	@Override
	protected void doFilterInternal(HttpServletRequest request, 
			HttpServletResponse response, 
			FilterChain filterChain)
			throws ServletException, IOException {

		//retrieve the username from the JWT Token
		String jwt = getJwtFromRequest(request);
		
		if(StringUtils.hasText(jwt) && jwtProvider.validateToken(jwt)) {
			String username = jwtProvider.getUsernameFromtJwt(jwt);
			
			
			UserDetails userDetails = userDetailsSerivce.loadUserByUsername(username);
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
					null, userDetails.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		filterChain.doFilter(request, response);
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			logger.info("Has bearer token: {}", bearerToken);
			return bearerToken.substring(7);
		}
		
		logger.info("No bearer token: {}", bearerToken);
		return bearerToken;
	}

}