package Insuleng.Insuleng_Backend.src.user.repository;

import Insuleng.Insuleng_Backend.config.Status;
import Insuleng.Insuleng_Backend.src.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//CRUD 함수를 JpaRepository가 들고 있음
//@Repository 어노테이션이 없어도 JpaRepository를 상속했기 때문에 자동으로 IoC된다.
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Boolean existsUserEntitiesByEmailAndStatus(String email, Status status);
    Boolean existsUserEntitiesByNicknameAndStatus(String nickname, Status status);

    Optional<UserEntity> findUserEntityByEmailAndStatus(String email, Status status);
    Optional<UserEntity> findUserEntityByNicknameAndPhoneNumberAndStatus(String nickname, int phoneNumber, Status status);
    Optional<UserEntity> findUserEntityByUserIdAndStatus(Long userId, Status status);
}
