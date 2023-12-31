package com.cabbookingsystem.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
@Table(name = "ratings_and_feedbacks")
public class RatingAndFeedback {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ratingAndFeedbackId;

	private Double rating;
	private String feedback;

	@ManyToOne
	private Ride ride;

	@ManyToOne
	private User giver;

	@ManyToOne
	private User receiver;

}
