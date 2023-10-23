package com.cabbookingsystem.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "key_details")
public class KeyDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String emailId;
	private String logInKey;
	private LocalDateTime keyGenerationTime;
	private Integer consecutiveAttemptCount;
	private LocalDateTime trackingStartTime;

	public KeyDetail(String emailId, String logInKey, LocalDateTime keyGenerationTime, Integer consecutiveAttemptCount,
			LocalDateTime trackingStartTime) {
		super();
		this.emailId = emailId;
		this.logInKey = logInKey;
		this.keyGenerationTime = keyGenerationTime;
		this.consecutiveAttemptCount = consecutiveAttemptCount;
		this.trackingStartTime = trackingStartTime;
	}

}
