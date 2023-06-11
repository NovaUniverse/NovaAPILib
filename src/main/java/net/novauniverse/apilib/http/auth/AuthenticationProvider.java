package net.novauniverse.apilib.http.auth;

import net.novauniverse.apilib.http.request.Request;

/**
 * This class tries to get an {@link Authentication} object from a
 * {@link Request}
 * 
 * @author Zeeraa
 */
public interface AuthenticationProvider {
	/**
	 * Try to get an {@link Authentication} from a {@link Request}. Return
	 * <code>null</code> if the authentication was not successful
	 * 
	 * @param request The incoming {@link Request}
	 * @return {@link Authentication} object on success and <code>null</code> on
	 *         failure
	 */
	Authentication authenticate(Request request);
}