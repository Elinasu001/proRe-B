package com.kh.even.back.configuration;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.kh.even.back.configuration.filter.JwtFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfigure {

	private final JwtFilter jwtFilter;

	@Value("${instance.url}")
	private String instance;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

		return httpSecurity.formLogin(AbstractHttpConfigurer::disable).csrf(AbstractHttpConfigurer::disable)
				.cors(Customizer.withDefaults()).authorizeHttpRequests(requests -> {

					// Swagger 허용
					requests.requestMatchers("/ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**",
							"/webjars/**").permitAll();

					// 1. GET - 비로그인 허용 (목록 / 검색)
					requests.requestMatchers(HttpMethod.GET, "/api/categories/**", "/api/experts/search",
							"/api/experts/map", "/api/experts/{expertNo}").permitAll();

					// 2. POST - 비로그인 허용 (기존 유지)
					requests.requestMatchers(HttpMethod.POST).permitAll();
					requests.requestMatchers(HttpMethod.DELETE).permitAll();
					requests.requestMatchers(HttpMethod.PUT).permitAll();

					// 3. GET - 로그인 필요 
					requests.requestMatchers(HttpMethod.GET, "/api/rooms/*/messages", "/api/reviews/**",
							"/api/reports/**",
							"/api/experts/registration", "/api/experts/matches", "/api/experts/likes",
							"/api/experts/*/categories", "/api/cash/*").authenticated();

					// 4. PUT - 로그인 필요 (기존)
					requests.requestMatchers(HttpMethod.PUT, "/api/admin/**", "/api/members/me/**").authenticated();

					requests.requestMatchers(HttpMethod.PATCH, "/api/members/me/**").authenticated();

					// 6. POST - 로그인 필요 (기존)
					requests.requestMatchers(HttpMethod.POST, "/api/reports", "/api/reviews/**", "/api/likes/**")
							.authenticated();

				}).sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class).build();

	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList(instance));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-type"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}
	
}