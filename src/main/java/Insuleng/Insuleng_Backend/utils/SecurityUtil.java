package Insuleng.Insuleng_Backend.utils;

import Insuleng.Insuleng_Backend.auth.CustomUserDetails;
import Insuleng.Insuleng_Backend.config.BaseException;
import Insuleng.Insuleng_Backend.config.BaseResponseStatus;
import org.eclipse.angus.mail.imap.SortTerm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.sound.midi.Soundbank;
import java.sql.SQLOutput;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

public class SecurityUtil {
    public static Optional<Long> getCurrentUserId(){ //userId 가져오기
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails customUserDetails;

        try {
            customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        }catch (ClassCastException e){
            return Optional.empty();
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.UNEXPECTED_ERROR);
        }

        return Optional.of(customUserDetails.getUserId());

    }


    public static String getCurrentUserRole(){

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();

        return auth.getAuthority();


    }
}
