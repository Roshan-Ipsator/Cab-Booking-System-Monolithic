package com.cabbookingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cabbookingsystem.entity.RatingAndFeedback;

@Repository
public interface RatingAndFeedBackRepository extends JpaRepository<RatingAndFeedback, Long> {
	@Query("SELECT AVG(r.rating) FROM RatingAndFeedback r " + "WHERE r.receiver.userId = :receiverId")
	Double findAverageRatingByReceiverId(Long receiverId);
}
