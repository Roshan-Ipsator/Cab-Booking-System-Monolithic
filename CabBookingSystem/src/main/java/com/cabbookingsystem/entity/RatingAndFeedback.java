package com.cabbookingsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ratings and feedbacks")
public class RatingAndFeedback {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ratingAndFeedbackId;

	private Integer rating;
	private String feedback;

}
