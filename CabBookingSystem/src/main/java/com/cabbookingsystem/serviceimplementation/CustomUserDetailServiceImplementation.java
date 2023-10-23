package com.cabbookingsystem.serviceimplementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.cabbookingsystem.entity.User;
import com.cabbookingsystem.repository.UserRepository;

public class CustomUserDetailServiceImplementation implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	/**
	 * Loads user details by username for authentication and authorization.
	 *
	 * @param username The username (typically an email or username) provided during
	 *                 authentication.
	 * @return A UserDetails object containing the user's information.
	 * @throws UsernameNotFoundException If no user with the provided username is
	 *                                   found.
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found!!"));
		return user;
	}
}
