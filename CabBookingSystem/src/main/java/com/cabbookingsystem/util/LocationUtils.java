package com.cabbookingsystem.util;

import java.util.Random;

public class LocationUtils {

	public static final double EARTH_RADIUS_KM = 6371.0; // Earth's radius in kilometers

	public static double[] generateLocationCoordinates() {
		double minLatitude = -90.0; // Minimum latitude value (-90 degrees)
		double maxLatitude = 90.0; // Maximum latitude value (90 degrees)
		double minLongitude = -180.0; // Minimum longitude value (-180 degrees)
		double maxLongitude = 180.0; // Maximum longitude value (180 degrees)

		// Create a random number generator
		Random random = new Random();

		// Generate random latitude and longitude values
		double latitude = minLatitude + (maxLatitude - minLatitude) * random.nextDouble();
		double longitude = minLongitude + (maxLongitude - minLongitude) * random.nextDouble();

		// Return the generated values
		double[] latitudeAndLongitudeArr = { latitude, longitude };

		return latitudeAndLongitudeArr;
	}

	public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
		// Convert latitude and longitude from degrees to radians
		double lat1Rad = Math.toRadians(lat1);
		double lon1Rad = Math.toRadians(lon1);
		double lat2Rad = Math.toRadians(lat2);
		double lon2Rad = Math.toRadians(lon2);

		// Haversine formula
		double dlon = lon2Rad - lon1Rad;
		double dlat = lat2Rad - lat1Rad;
		double a = Math.pow(Math.sin(dlat / 2), 2)
				+ Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.pow(Math.sin(dlon / 2), 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = EARTH_RADIUS_KM * c;

		return distance;

	}
}
