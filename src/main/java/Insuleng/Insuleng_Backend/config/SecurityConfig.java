package Insuleng.Insuleng_Backend.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder encodePwd(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
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
                .anyRequest().permitAll());

        // 세션을 안 쓰는 stateless 만들기
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}
