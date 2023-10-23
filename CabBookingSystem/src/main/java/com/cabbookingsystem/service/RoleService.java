package com.cabbookingsystem.service;

import com.cabbookingsystem.entity.Role;
import com.cabbookingsystem.entity.User;
import com.cabbookingsystem.payload.ServiceResponse;
import com.cabbookingsystem.record.AddPermissionToRoleRecord;
import com.cabbookingsystem.record.CreateRoleRecord;
import com.cabbookingsystem.record.UpdateRoleRecord;

public interface RoleService {

	/**
	 * Creates a new role based on the provided CreateRoleRecord.
	 *
	 * @param createRoleRecord The record containing information to create the role.
	 * @return A ServiceResponse containing the created Role if successful, along
	 *         with a success message, or an error message if the operation fails.
	 */
	public ServiceResponse<Role> createRole(CreateRoleRecord createRoleRecord);

	/**
	 * Adds permissions to an existing role based on the provided
	 * AddPermissionToRoleRecord.
	 *
	 * @param addPermissionToRoleRecord The record containing information to add
	 *                                  permissions to the role.
	 * @return A ServiceResponse containing the updated Role with added permissions
	 *         if successful, along with a success message, or an error message if
	 *         the operation fails.
	 */
	public ServiceResponse<Role> addPermissionToRole(AddPermissionToRoleRecord addPermissionToRoleRecord);

	/**
	 * Updates the details of an existing role based on the provided
	 * UpdateRoleRecord.
	 *
	 * @param updateRoleRecord The record containing information to update the role.
	 * @return A ServiceResponse containing the updated Role if successful, along
	 *         with a success message, or an error message if the operation fails.
	 */
	public ServiceResponse<User> updateRole(UpdateRoleRecord updateRoleRecord);
}
