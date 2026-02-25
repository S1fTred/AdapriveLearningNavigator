package com.example.adaprivelearningnavigator.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    // чтобы отдавать 401 в формате ApiErrorResponse
    private final AuthenticationEntryPoint authenticationEntryPoint;

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    private static final List<String> WHITELIST = List.of(
            "/", "/index.html", "/favicon.ico",
            "/assets/**", "/css/**", "/js/**",
            "/auth/**", "/error", "/h2-console/**", "/actuator/**",
            "/api/topics/**", "/api/roles/**"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;
        String uri = request.getRequestURI();
        for (String pattern : WHITELIST) {
            if (PATH_MATCHER.match(pattern, uri)) return true;
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws ServletException, IOException {

        String header = req.getHeader(HttpHeaders.AUTHORIZATION);

        // Если заголовка нет — просто идём дальше (доступ решит SecurityFilterChain)
        if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(req, res);
            return;
        }

        String token = header.substring(7);

        try {
            Claims claims = jwtService.parseAndValidate(token);
            Long userId = claims.get("userId", Number.class).longValue();

            var userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                // Токен валиден, но пользователь не найден — считаем, что авторизация невозможна
                SecurityContextHolder.clearContext();
                authenticationEntryPoint.commence(req, res,
                        new org.springframework.security.authentication.BadCredentialsException(
                                "Пользователь не найден"
                        )
                );
                return;
            }

            var principal = UserPrincipal.from(userOpt.get());
            var auth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                    principal, null, principal.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(auth);

            chain.doFilter(req, res);

        } catch (Exception ex) {
            // Любая ошибка валидации JWT (просрочен, подпись, формат) => 401 в едином формате
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(req, res,
                    new org.springframework.security.authentication.BadCredentialsException(
                            "Неверный или просроченный токен", ex
                    )
            );
        }
    }
}