package com.cabbookingsystem.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	private String firstName;
	private String lastName;
	private String gender;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(unique = true)
	@Pattern(regexp = "^[0-9]{10}", message = "Mobile number length must be 10 digits [0-9]")
	private String phone;

	private LocalDateTime userCreationTime;

	private LocalDateTime userUpdationTime;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Role role;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "passenger")
	private List<Ride> bookingHistory;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL)
	private List<FavouriteAddress> favouriteAddresses;

	public User(Long userId, String firstName, String lastName, String email, String gender, String phone,
			LocalDateTime userCreationTime) {
		super();
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.gender = gender;
		this.phone = phone;
		this.userCreationTime = userCreationTime;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<>();
		for (Permission permission : role.getPermissions()) {
			authorities.add(new SimpleGrantedAuthority(permission.getName()));
		}
		authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
		return authorities;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
