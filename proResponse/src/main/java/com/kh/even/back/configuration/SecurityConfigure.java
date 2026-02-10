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

import jakarta.servlet.http.HttpServletResponse;
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

		return httpSecurity
				.formLogin(AbstractHttpConfigurer::disable)
				.csrf(AbstractHttpConfigurer::disable)
				.cors(Customizer.withDefaults())
				.authorizeHttpRequests(requests -> {

					/* ================= Swagger ================= */
					requests.requestMatchers(
							"/ui.html",
							"/swagger-ui/**",
							"/v3/api-docs/**",
							"/swagger-resources/**",
							"/webjars/**",
							"/favicon.ico"
					).permitAll();

					/* ================= CORS Preflight ================= */
					requests.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();

					/* ================= 관리자 전용 ================= */
					requests.requestMatchers("/api/admin/**")
							.authenticated();
				    requests.requestMatchers("/api/admin/**")// 먼저 인증 체크 (미로그인 시 401)
							.hasAnyAuthority("ROLE_ADMIN", "ROLE_ROOT");  // 그 다음 권한 체크 (403)

					/* ================= 비로그인 허용 (GET) ================= */
					requests.requestMatchers(
							HttpMethod.GET,
							"/api/categories/**",
							"/api/experts/search",
							"/api/experts/map",
							"/api/experts/*",
							"/api/reviews/expert/*",
							"/api/reviews/tags",
							"/api/ws/chat/**",
							"/api/main",
							"/api/geo/*",
							"/api/experts/{expertNo}/categories"
					).permitAll();

					/* ================= 인증 관련 ================= */
					requests.requestMatchers(
							HttpMethod.POST,
							"/api/auth/login",
							"/api/members",
							"/api/emails/verification-requests",
							"/api/emails/verifications",
							"/api/emails/temporary-password",
							"/api/emails/sendcode/password"
					).permitAll();

					/* ================= 로그인 필요 (GET) ================= */
					requests.requestMatchers(
							HttpMethod.GET,
							"/api/rooms/*/messages",
							"/api/reviews/**",
							"/api/reports/**",
							"/api/experts/registration",
							"/api/experts/matches",
							"/api/experts/likes",
							"/api/experts/*/categories",
							"/api/estimate",
							"/api/estimate/**",
							"/api/experts/me",
							"/api/cash/me",
							"/api/members/me",
							"/api/experts/checkExist"
					).authenticated();

					/* ================= 로그인 필요 (PUT / PATCH) ================= */
					requests.requestMatchers(
							HttpMethod.PUT,
							"/api/members/me/**",
							"/api/experts/me",
							"/api/estimate/**",
							"/api/reviews/**",
							"/api/experts/switch/**"
					).authenticated();

					requests.requestMatchers(
							HttpMethod.PATCH,
							"/api/members/me/**"
					).authenticated();

					/* ================= 로그인 필요 (POST) ================= */
					requests.requestMatchers(
							HttpMethod.POST,
							"/api/reports",
							"/api/rooms/**",
							"/api/reviews/**",
							"/api/likes/**",
							"/api/payments/**",
							"/api/estimate",
							"/api/estimate/**",
							"/api/experts/**",
							"/api/experts/registration",
							"/api/auth/logout"
					).authenticated();

					/* ================= 로그인 필요 (DELETE) ================= */
					requests.requestMatchers(
							HttpMethod.DELETE,
							"/api/members",
							"/api/payments/**",
							"/api/estimate",
							"/api/estimate/**",
							"/api/experts/**",
							"/api/reviews/**"
					).authenticated();
				})
				.exceptionHandling(exception ->
						exception.authenticationEntryPoint(
								(request, response, authException) -> {
									response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
								}
						)
				)
				.sessionManagement(manager ->
						manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList(instance));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
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
