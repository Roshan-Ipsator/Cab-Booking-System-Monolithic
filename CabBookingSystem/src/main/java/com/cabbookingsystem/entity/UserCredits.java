package com.cabbookingsystem.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
public class UserCredits {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long creditsId;
	private Double currentBalance;
	private Double overDue;

	@OneToOne // Unidirectional
	@JoinColumn(name = "user_id")
	private User user;
}
