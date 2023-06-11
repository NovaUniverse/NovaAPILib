package net.novauniverse.apilib.http.enums;

import net.novauniverse.apilib.http.HTTPServer;

/**
 * This enum defines what action to take if an error occurs in a request
 * 
 * @author Zeeraa
 */
public enum ExceptionMode {
	/**
	 * Send everything including the stack trace
	 */
	STACKTRACE,
	/**
	 * Include the exception class name and message
	 */
	MESSAGE,
	/**
	 * Include the exception class name
	 */
	TYPE,
	/**
	 * Show a generic error message
	 */
	HIDE,
	/**
	 * Inherit settings from {@link HTTPServer}
	 */
	INHERIT;
}