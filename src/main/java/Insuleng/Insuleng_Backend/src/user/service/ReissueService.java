package Insuleng.Insuleng_Backend.src.user.service;

import Insuleng.Insuleng_Backend.config.BaseException;
import Insuleng.Insuleng_Backend.config.BaseResponseStatus;
import Insuleng.Insuleng_Backend.jwt.JWTUtil;
import Insuleng.Insuleng_Backend.src.user.entity.RefreshEntity;
import Insuleng.Insuleng_Backend.src.user.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ReissueService {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public void reissue(HttpServletRequest request, HttpServletResponse response) {

        //먼저 헤더에서 refresh token 가져오기
        String refresh = null;

        Cookie[] cookies = request.getCookies();

        if(cookies == null){
            throw new BaseException(BaseResponseStatus.NO_REFRESH_TOKEN);
        }

        for(Cookie cookie : cookies){
            if(cookie.getName().equals("refresh")){
                refresh = cookie.getValue();
            }
        }

        if(refresh == null){
            System.out.println("리프레쉬 토큰이 존재하지 않습니다");
            throw new BaseException(BaseResponseStatus.NO_REFRESH_TOKEN);
        }

        try{
            jwtUtil.isExpired(refresh);
        }catch (ExpiredJwtException e){
            throw new BaseException(BaseResponseStatus.EXPIRED_REFRESH_TOKEN);
        }

        String category = jwtUtil.getCategory(refresh);

        if(!category.equals("refresh")){
            throw new BaseException(BaseResponseStatus.INVALID_REFRESH_TOKEN);
        }

        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if(!isExist){
            throw new BaseException(BaseResponseStatus.INVALID_REFRESH_TOKEN);
        }

        String email = jwtUtil.getEmail(refresh);
        String role = jwtUtil.getRole(refresh);
        Long userId = jwtUtil.getUserId(refresh);

        String newAccess = jwtUtil.createJwt("access", userId, email, role, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", userId, email, role, 86400000L);

        //Refresh 토큰 저장, DB에 기존의 refresh 토큰 삭제 후, 새로운 refresh 토큰 저장
        refreshRepository.deleteByRefresh(refresh);
        addRefreshEntity(email, newRefresh, 86400000L);

        //response
        response.setHeader("Authorization", "Bearer "+newAccess);
        response.addCookie(createCookie("refresh", newRefresh));
    }

    private void addRefreshEntity(String email, String refresh, Long expiredMs){

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setEmail(email);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }


    private Cookie createCookie(String key, String value){

        Cookie cookie = new Cookie(key, value);

        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;

    }

}
