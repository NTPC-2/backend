package Insuleng.Insuleng_Backend.jwt;

import Insuleng.Insuleng_Backend.auth.CustomUserDetails;
import Insuleng.Insuleng_Backend.src.user.entity.UserEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        //헤더에 대한 유효성 검사
        if(authorization == null || !authorization.startsWith("Bearer")){
            System.out.println("헤더를 다시 검토해주세요.");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.split(" ")[1];

        //토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {

            //true면 토큰 시간 만료, false면 token이 살아있음
            //토큰이 만료됐을 때 예외처리도 해줘야 한다.
            System.out.println("토큰 시간이 만료되었습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        //이 후 과정은 토큰이 정상적으로 발급된 경우이다.
        Long userId = jwtUtil.getUserId(token);
        String email = jwtUtil.getEmail(token);
        String role = jwtUtil.getRole(token);

        UserEntity userEntity = new UserEntity();
        userEntity.setEmailAndPwdAndRole(email, "tokenApi", role);

        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity, userId);

        //강제로 authentication 객체 만들어 안에 userDetails 넣어주기
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        //강제로 시큐리티 세션에 접근해서 Authentication 객체를 저장. 이러면 로그인이 완료된다.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response); //필터 체인을 계속 타게 하기


    }
}
