package net.novauniverse.apilib.http.auth;

import java.util.List;

/**
 * Represents an authenticated user
 * 
 * @author Zeeraa
 */
public abstract class Authentication {
	/**
	 * Returns a list of permission strings for the user
	 * 
	 * @return {@link List} with string of permissions
	 */
	public abstract List<String> getPermissions();

	/**
	 * Check if the user has a specific permission
	 * 
	 * @param permission The permission string
	 * @return <code>true</code> if the user has the permission
	 */
	public boolean hasPermission(String permission) {
		return this.getPermissions().contains(permission);
	}
}