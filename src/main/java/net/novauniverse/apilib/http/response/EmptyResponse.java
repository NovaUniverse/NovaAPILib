package net.novauniverse.apilib.http.response;

import com.sun.net.httpserver.HttpExchange;
import net.novauniverse.apilib.http.enums.HTTPResponseCode;

import java.io.IOException;

/**
 * Returns an empty http request
 * 
 * @author Zeeraa
 */
public class EmptyResponse extends AbstractHTTPResponse {
	private final int code;

	/**
	 * An empty response with the http response code of {@link HTTPResponseCode#OK}
	 */
	public EmptyResponse() {
		this(HTTPResponseCode.OK);
	}

	/**
	 * @param code The {@link HTTPResponseCode} to send
	 */
	public EmptyResponse(HTTPResponseCode code) {
		this(code.getCode());
	}

	/**
	 * @param code The http response to send
	 */
	public EmptyResponse(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		exchange.sendResponseHeaders(code, -1);
	}
}