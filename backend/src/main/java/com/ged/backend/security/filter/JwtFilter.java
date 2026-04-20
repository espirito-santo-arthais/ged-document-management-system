package com.ged.backend.security.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ged.backend.security.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final JwtService jwtService;

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain)
			throws ServletException, IOException {

		try {
			String authHeader = request.getHeader("Authorization");

			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				filterChain.doFilter(request, response);
				return;
			}

			String token = authHeader.substring(7);

			if (!jwtService.isTokenValid(token)) {
				filterChain.doFilter(request, response);
				return;
			}

			String username = jwtService.extractUsername(token);
			String role = jwtService.extractRole(token);

			var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

			var authentication = new UsernamePasswordAuthenticationToken(
					username,
					null,
					authorities);

			if (SecurityContextHolder.getContext().getAuthentication() == null) {
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}

			log.info("Usuário autenticado via JWT: {}", username);
		} catch (Exception ex) {
			log.warn("Erro ao processar JWT", ex);
		}

		filterChain.doFilter(request, response);
	}
}