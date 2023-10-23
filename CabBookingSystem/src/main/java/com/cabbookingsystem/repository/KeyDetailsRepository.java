package com.cabbookingsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cabbookingsystem.entity.KeyDetails;

@Repository
public interface KeyDetailsRepository extends JpaRepository<KeyDetails, Long> {

	/**
	 * Retrieves key details based on the provided sign-up/login key.
	 *
	 * @param key The sign-up/login key to search for.
	 * @return KeyDetails object containing details associated with the key, or null
	 *         if no matching key is found.
	 */
	KeyDetails findByLogInKey(String key);

	/**
	 * Retrieves key details associated with a specific email address.
	 *
	 * @param emailId The email address for which to retrieve key details.
	 * @return A {@code KeyDetails} object representing the key details associated
	 *         with the email address, or {@code null} if no key details are found.
	 */
	Optional<KeyDetails> findByEmail(String email);
}
