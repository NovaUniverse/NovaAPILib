package net.novauniverse.apilib.http.enums;

import net.novauniverse.apilib.http.HTTPServer;
import net.novauniverse.apilib.http.response.AbstractHTTPResponse;
import net.novauniverse.apilib.http.response.JSONResponse;
import net.novauniverse.apilib.http.response.TextResponse;
import org.json.JSONObject;

/**
 * This enum specifies the default error response types from the
 * {@link HTTPServer}
 * 
 * @author Zeeraa
 */
public enum StandardResponseType {
	/**
	 * Send errors as plain text
	 */
	TEXT,
	/**
	 * Send errors in JSON format
	 */
	JSON;

	public AbstractHTTPResponse error(String message, HTTPResponseCode code) {
		return this.error(message, code.getCode());
	}

	public AbstractHTTPResponse error(String message, int code) {
		if (this == TEXT) {
			return new TextResponse(message, code, TextResponse.TextResponseMimeType.TEXT_PLAIN);
		}

		JSONObject json = new JSONObject();
		json.put("error", message);
		return new JSONResponse(json, code);
	}
}