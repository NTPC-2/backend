package Insuleng.Insuleng_Backend.jwt;

import Insuleng.Insuleng_Backend.auth.CustomUserDetails;
import Insuleng.Insuleng_Backend.config.BaseException;
import Insuleng.Insuleng_Backend.config.BaseResponseStatus;
import Insuleng.Insuleng_Backend.src.user.entity.UserEntity;
import io.jsonwebtoken.ExpiredJwtException;
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
import java.io.PrintWriter;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, BaseException {

        //헤더에서 Access 토큰을 꺼냄
        String accessToken = request.getHeader("Authorization");

        //로그인 권한이 필요없는 화면 일수도 있어서 토큰이 없는 경우 doFilter을 타게 한다
        if(accessToken == null){
            filterChain.doFilter(request, response);

            return;
        }

        //토큰이 있다면 아래 로직이 실행
        //헤더에 대한 유효성 검사
        if(!accessToken.startsWith("Bearer")){
            System.out.println("헤더를 다시 검토해주세요.");

            throw new BaseException(BaseResponseStatus.INVALID_ACCESS_TOKEN);
        }

        String token = accessToken.split(" ")[1];

        //서버에서 발급한 토큰이 맞는지 검사
        if(jwtUtil.validToken(token) == false){
            throw new BaseException(BaseResponseStatus.INVALID_ACCESS_TOKEN);
        }

        //토큰 만료 여부 확인, 만료시 다음 필터로 넘어가지 않고 ExpiredException 예외를 띄움
        try{
            jwtUtil.isExpired(token);
        }catch (ExpiredJwtException e){

            //response body
            PrintWriter printWriter = response.getWriter();
            printWriter.print("access token is expired");

           throw new BaseException(BaseResponseStatus.EXPIRED_ACCESS_TOKEN);
            //토큰이 없으면 다음 필터로 넘기는데 토큰이 만료되면 다음 필터로 넘기지 않는다
        }

        //토큰이 만료되지 않았으면, 해당 토큰이 access인지 refresh인지 확인하는 작업을 거친다
        String category = jwtUtil.getCategory(token);

        if(!category.equals("access")){

            //response body
            PrintWriter printWriter = response.getWriter();
            printWriter.print("invalid access token");

            throw new BaseException(BaseResponseStatus.INVALID_ACCESS_TOKEN);
        }


        //이 후 과정은 액세스 토큰이 정상적으로 발급된 경우이다.
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
