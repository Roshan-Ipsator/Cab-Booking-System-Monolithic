package com.cabbookingsystem.entity;

import jakarta.persistence.Entity;
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
@Table(name = "favourite addresses")
public class FavouriteAddress {
private Long addressId;
//private User user;
private String addressName;
private String district;
private String state;
private String country;
private String pincode;
}
