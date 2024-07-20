package Insuleng.Insuleng_Backend.auth;

import Insuleng.Insuleng_Backend.config.Status;
import Insuleng.Insuleng_Backend.src.user.entity.UserEntity;
import Insuleng.Insuleng_Backend.src.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        String email = username;

        UserEntity user = userRepository.findUserEntityByEmailAndStatus(email, Status.ACTIVE);

        if(user != null){
            System.out.println("현재 userId는 " + user.getUserId());
            return new CustomUserDetails(user);
        }
        return null;
    }
}
