package Insuleng.Insuleng_Backend.src.restaurant.repository;

import Insuleng.Insuleng_Backend.src.restaurant.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
}
