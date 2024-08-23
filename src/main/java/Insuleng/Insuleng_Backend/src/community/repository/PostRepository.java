package Insuleng.Insuleng_Backend.src.community.repository;

import Insuleng.Insuleng_Backend.src.community.entity.PostEntity;
import Insuleng.Insuleng_Backend.src.home.dto.PopularPostDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

    @Query("select p from PostEntity as p inner join fetch p.userEntity order by p.countLike desc")
    public List<PostEntity> findPopularPosts();


    /*@Query("select new Insuleng.Insuleng_Backend.src.home.dto.PopularPostDto(" +
            "p.postId, " +
            "p.topic, " +
            "p.contents, " +
            "p.countLike, " +
            "p.countScrap, " +
            "p.countComment, " +
            "p.imgUrl, " +
            "p.userEntity.nickname, " +
            "p.createAt) " +
            "from PostEntity p " +
            "inner join fetch p.userEntity " +
            "order by p.countLike desc")
    public PopularPostDto findPopularPosts();*/

}
