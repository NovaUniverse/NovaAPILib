package net.novauniverse.apilib.http.middleware;

/**
 * This enum specified if the middleware should run before or after the built-in
 * authentication process
 * 
 * @author Zeeraa
 *
 */
public enum MiddlewareType {
	/**
	 * Run before the built-in authentication system
	 */
	PRE_AUTHENTICATION,
	/**
	 * Run after the built-in authentication system
	 */
	POST_AUTHENTICATION;
}