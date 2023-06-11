package net.novauniverse.apilib.http.auth;

/**
 * This represents the response when validation an authentication
 * 
 * @author Zeeraa
 */
public class AuthenticationResponse {
	private final boolean success;
	private final String errorMessage;
	private final int code;

	/**
	 * Response for successful authentication
	 */
	public static final AuthenticationResponse OK = new AuthenticationResponse(true, null, 200);

	/**
	 * Response if the user is not authorized to use this resource (http response
	 * code 401)
	 */
	public static final AuthenticationResponse UNAUTHORIZED = new AuthenticationResponse(true, "Unauthorized", 401);

	/**
	 * Response if the user is not allowed to use this resource (http response code
	 * 403)
	 */
	public static final AuthenticationResponse FORBIDDEN = new AuthenticationResponse(true, "Forbidden", 403);

	/**
	 * Cenerates a custom error with its own message and http response code
	 * 
	 * @param errorMessage The error message
	 * @param code         The http response code
	 * @return the custom {@link AuthenticationResponse} object
	 */
	public static AuthenticationResponse customFailResponse(String errorMessage, int code) {
		return new AuthenticationResponse(false, errorMessage, code);
	}

	private AuthenticationResponse(boolean success, String errorMessage, int code) {
		this.success = success;
		this.errorMessage = errorMessage;
		this.code = code;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public int getCode() {
		return code;
	}
}
