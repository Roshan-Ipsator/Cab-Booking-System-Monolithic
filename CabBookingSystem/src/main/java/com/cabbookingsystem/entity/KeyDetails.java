package com.cabbookingsystem.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "key_details")
public class KeyDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String email;

	private String logInKey;
	private LocalDateTime keyGenerationTime;
	private Integer consecutiveAttemptCount;
	private LocalDateTime trackingStartTime;

	public KeyDetails(String email, String logInKey, LocalDateTime keyGenerationTime,
			Integer consecutiveAttemptCount, LocalDateTime trackingStartTime) {
		super();
		this.email = email;
		this.logInKey = logInKey;
		this.keyGenerationTime = keyGenerationTime;
		this.consecutiveAttemptCount = consecutiveAttemptCount;
		this.trackingStartTime = trackingStartTime;
	}

}
