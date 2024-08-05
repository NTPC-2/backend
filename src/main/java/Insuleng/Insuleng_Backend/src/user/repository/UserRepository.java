package Insuleng.Insuleng_Backend.src.user.repository;

import Insuleng.Insuleng_Backend.config.Status;
import Insuleng.Insuleng_Backend.src.user.dto.UserStatics;
import Insuleng.Insuleng_Backend.src.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//CRUD 함수를 JpaRepository가 들고 있음
//@Repository 어노테이션이 없어도 JpaRepository를 상속했기 때문에 자동으로 IoC된다.
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Boolean existsUserEntitiesByEmailAndStatus(String email, Status status);
    Boolean existsUserEntitiesByNicknameAndStatus(String nickname, Status status);

    Optional<UserEntity> findUserEntityByEmailAndStatus(String email, Status status);
    Optional<UserEntity> findUserEntityByNicknameAndPhoneNumberAndStatus(String nickname, int phoneNumber, Status status);
    Optional<UserEntity> findUserEntityByUserIdAndStatus(Long userId, Status status);

    //@Query 문에서 Entity가 아닌 Dto로 반환을 받고 싶다면 (select new Dto 클래스의 경로명) 을 지정해주면 된다.
    @Query("select new Insuleng.Insuleng_Backend.src.user.dto.UserStatics("
            + "(select count(b) from BookmarkEntity b where b.userEntity.userId = :userId), "
            + "(select count(r) from ReviewEntity r where r.userEntity.userId = :userId), "
            + "(select count(h) from HeartEntity h where h.userEntity.userId = :userId), "
            + "(select count(p) from PostEntity p where p.userEntity.userId = :userId), "
            + "(select count(s) from ScrapEntity s where s.userEntity.userId = :userId)) "
            + "FROM UserEntity u WHERE u.userId = :userId")
    public UserStatics findUserStatics(@Param("userId")Long userId);

}
