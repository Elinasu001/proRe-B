@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

	return httpSecurity
			.formLogin(AbstractHttpConfigurer::disable)
			.csrf(AbstractHttpConfigurer::disable)
			.cors(Customizer.withDefaults())
			.authorizeHttpRequests(requests -> {

				// Swagger 허용
				requests.requestMatchers(
						"/ui.html",
						"/swagger-ui/**",
						"/v3/api-docs/**",
						"/swagger-resources/**",
						"/webjars/**"
				).permitAll();

				// 1. GET - 비로그인 허용 (목록 / 검색)
				requests.requestMatchers(HttpMethod.GET,
						"/api/categories/**",
						"/api/experts/search",
						"/api/experts/map",
						"/api/experts/{expertNo}"
				).permitAll();

				// 2. POST - 비로그인 허용 (기존 유지)
				requests.requestMatchers(HttpMethod.POST).permitAll();
				requests.requestMatchers(HttpMethod.DELETE).permitAll();
				requests.requestMatchers(HttpMethod.PUT).permitAll();

				// 3. GET - 로그인 필요 (기존 + Expert 추가)
				requests.requestMatchers(HttpMethod.GET,
						"/api/rooms/*/messages",
						"/api/reviews/**",
						"/api/reports/**",

						// 🔽 여기만 추가
						"/api/experts/registration",
						"/api/experts/matches",
						"/api/experts/likes",
						"/api/experts/*/categories"
				).authenticated();

				// 4. PUT - 로그인 필요 (기존)
				requests.requestMatchers(HttpMethod.PUT,
						"/api/admin/**",
						"/api/members/me/**"
				).authenticated();

				requests.requestMatchers(HttpMethod.PATCH,
						"/api/members/me/**"
				).authenticated();

				// 6. POST - 로그인 필요 (기존)
				requests.requestMatchers(HttpMethod.POST,
						"/api/reports",
						"/api/reviews/**",
						"/api/likes/**"
				).authenticated();

			})
			.sessionManagement(manager ->
					manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
			.build();
}
