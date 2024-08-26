package Insuleng.Insuleng_Backend.src.community.repository;

import Insuleng.Insuleng_Backend.src.community.dto.CommentInfoDto;
import Insuleng.Insuleng_Backend.src.community.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CommentRepository extends JpaRepository<CommentEntity, Long> {


}
