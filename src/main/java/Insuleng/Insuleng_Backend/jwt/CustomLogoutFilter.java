package Insuleng.Insuleng_Backend.jwt;

import Insuleng.Insuleng_Backend.config.BaseException;
import Insuleng.Insuleng_Backend.config.BaseResponseStatus;
import Insuleng.Insuleng_Backend.src.user.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException, BaseException{

        //uri와 method 방식을 통해 유효성 검사
        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^\\/logout$")) {

            filterChain.doFilter(request, response);
            return;
        }

        String requestMethod = request.getMethod();

        if(!requestMethod.equals("POST")){

            filterChain.doFilter(request, response);
            return;
        }

        String refresh = null;
        Cookie[] cookies = request.getCookies();

        for(Cookie cookie : cookies){

            if(cookie.getName().equals("refresh")){
                refresh = cookie.getValue();
            }
        }

        //refresh null 체크
        if(refresh == null){
            throw new BaseException(BaseResponseStatus.ALREADY_LOGOUT);
        }

        //expired check
        try{
            jwtUtil.isExpired(refresh);
        }catch (ExpiredJwtException e){

            throw new BaseException(BaseResponseStatus.EXPIRED_REFRESH_TOKEN);
        }

        String category = jwtUtil.getCategory(refresh);
        if(!category.equals("refresh")){

            throw new BaseException(BaseResponseStatus.INVALID_REFRESH_TOKEN); // 메인 화면으로 돌려보내기
        }

        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if(!isExist){

            throw new BaseException(BaseResponseStatus.INVALID_REFRESH_TOKEN); // 메인 화면으로 돌래보내기
        }

        refreshRepository.deleteByRefresh(refresh);

        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);


    }

}
