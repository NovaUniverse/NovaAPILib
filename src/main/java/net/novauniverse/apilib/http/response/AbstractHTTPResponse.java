package net.novauniverse.apilib.http.response;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

/**
 * This represents a response from the api
 * 
 * @author Zeeraa
 */
public abstract class AbstractHTTPResponse {
	/**
	 * Send the result of the {@link AbstractHTTPResponse} to the
	 * {@link com.sun.net.httpserver.HttpExchange}
	 * 
	 * @param exchange the {@link com.sun.net.httpserver.HttpExchange}
	 * @throws IOException In case something goes wrong while writing the response
	 */
	public abstract void handle(HttpExchange exchange) throws IOException;
}