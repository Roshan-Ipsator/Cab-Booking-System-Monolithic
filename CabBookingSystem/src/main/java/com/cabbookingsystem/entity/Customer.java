//package com.cabbookingsystem.entity;
//
//import java.util.List;
//
//import jakarta.persistence.CascadeType;
//import jakarta.persistence.ElementCollection;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.OneToMany;
//import jakarta.persistence.OneToOne;
//import jakarta.persistence.Table;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@Table(name = "customers")
//public class Customer {
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long customerId;
//
//	@OneToOne
////	@JoinColumn(name = "user_id")
//	private User user;
//
//	@OneToMany(cascade = CascadeType.ALL)
//	@JoinColumn(name = "customer_id")
//	private List<Ride> bookingHistory;
//	
//	@OneToMany(cascade = CascadeType.ALL)
//	private List<RatingAndFeedback> ratingAndFeedbacks;
//	
//	@OneToMany(cascade = CascadeType.ALL)
//	private List<FavouriteAddress> favouriteAddresses;
//
//}
