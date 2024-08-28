package Insuleng.Insuleng_Backend.src.community.repository;

import Insuleng.Insuleng_Backend.config.Status;
import Insuleng.Insuleng_Backend.src.community.dto.CommentInfoDto;
import Insuleng.Insuleng_Backend.src.community.entity.CommentEntity;
import Insuleng.Insuleng_Backend.src.community.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    Optional<CommentEntity> findCommentEntityByCommentIdAndStatusAndPost(Long parentCommentId, Status status, PostEntity post);
}
