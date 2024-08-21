package Insuleng.Insuleng_Backend.config;

import Insuleng.Insuleng_Backend.jwt.CustomLogoutFilter;
import Insuleng.Insuleng_Backend.jwt.JWTFilter;
import Insuleng.Insuleng_Backend.jwt.JWTUtil;
import Insuleng.Insuleng_Backend.jwt.LoginFilter;
import Insuleng.Insuleng_Backend.src.user.repository.RefreshRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }
    @Bean
    public BCryptPasswordEncoder encodePwd(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http.cors((cors) -> cors
                .configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration corsConfiguration = new CorsConfiguration();

                        corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:5137"));
                        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                        corsConfiguration.setAllowCredentials(true);
                        corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
                        corsConfiguration.setMaxAge(3600L);

                        corsConfiguration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return corsConfiguration;
                    }
                })

        );

        //메서드 참조 방식
        //http.csrf(AbstractHttpConfigurer::disable);
        //람다 활용 방식
        //토큰 방식을 사용하면 csrf을 굳이 킬 필요가 없다.
        http.csrf((csrfConfig) -> csrfConfig.disable());
        //form 로그인 방식 disable
        http.formLogin((auth) -> auth.disable());

        //bearer방식을 쓰기 위해 basic disable 하기
        http.httpBasic((basic)->basic.disable());


        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/user/**").authenticated()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/profiles/**").authenticated()
                .requestMatchers("/post/**").authenticated()
                .requestMatchers("/comment/**").authenticated()
                .anyRequest().permitAll());

        // 세션을 안 쓰는 stateless 만들기
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

        http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, refreshRepository), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshRepository), LogoutFilter.class);

        return http.build();
    }

}
