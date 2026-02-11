package com.kh.even.back.configuration.filter;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.exception.CustomAuthenticationException;
import com.kh.even.back.token.model.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String uri = request.getRequestURI();

		String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (authorization == null || uri.equals("/api/auth/login")) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = authorization.split(" ")[1];

		log.info("토큰 값 : {}", token);

		try {
			Claims claims = jwtUtil.parseJwt(token);
			String username = claims.getSubject();

			CustomUserDetails user = (CustomUserDetails) userDetailsService.loadUserByUsername(username);

			if(user.getPenaltyStatus().equals("Y")) {
				throw new CustomAuthenticationException("정지된 계정입니다.");
			}
			
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null,
					user.getAuthorities());

			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

			SecurityContextHolder.getContext().setAuthentication(authentication);

		} catch (ExpiredJwtException e) {
			log.info("토큰의 유효기간 만료");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().write("토큰 만료");
			/*
			 * axios.post(url, body, tokens) .then(result => { result어쩌고저쩌구 }).catch(e => {
			 * e == 토큰만료 axios.post(/auth/refresh, 리프레시토큰값) .then(result => { 새 토큰 저장소에 저장;
			 * useEffect의 의존성 요소를 변환시켜 useEffect를 다시 수행; }) .catch(e => {
			 * alert("니 로그인 다시해야댐"); useNavi("/login"); }) })
			 */
			return;
		} catch (JwtException e) {
			log.info("서버에서 만들어진 토큰이 아님");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("유효하지 않은 토큰");
			return;
		} catch(Exception e) {
			log.info("그 외 이슈 확인");
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("문제 발생");
		}

		filterChain.doFilter(request, response);

	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
	    String uri = request.getRequestURI();
	    String method = request.getMethod();

	    return "POST".equals(method) && (
	        uri.equals("/api/emails/verification-requests") ||
	        uri.equals("/api/emails/verifications") ||
	        uri.equals("/api/emails/temporary-password") ||
	        uri.equals("/api/emails/sendcode/password")
	    );
	}
}
