package com.cabbookingsystem.payload;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResponse<T> {

	private Boolean success;
	private T data;
	private String message;

	public ResponseEntity<ApiResponse> finalResponse() {
		if (this.success) {
			// If the operation was successful, create a response with success status, data,
			// and HTTP status OK (200).
			ApiResponse apiResponse = new ApiResponse("success", this.data, null);

			return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
		} else {
			// If the operation failed, create a response with error status, error message,
			// and HTTP status BAD REQUEST (400).
			Error error = new Error();
			error.setMessage(this.message);

			ApiResponse apiResponse = new ApiResponse("error", null, error);

			return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.BAD_REQUEST);
		}
	}
}
