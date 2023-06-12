package net.novauniverse.apilib.http.middleware;

import net.novauniverse.apilib.http.response.AbstractHTTPResponse;

/**
 * This represents the action that should be taken after a
 * {@link HTTPMiddleware} runs. Use {@link MiddlewareResponse#CONTINUE} to
 * continue the request and {@link MiddlewareResponse#cancel} to cancel the
 * request with a provided response
 * 
 * @author Zeeraa
 *
 */
public class MiddlewareResponse {
	private final boolean cancel;
	private final AbstractHTTPResponse response;

	public static final MiddlewareResponse CONTINUE = new MiddlewareResponse(false, null);

	public static MiddlewareResponse CANCEL(AbstractHTTPResponse response) {
		return new MiddlewareResponse(true, response);
	}

	private MiddlewareResponse(boolean cancel, AbstractHTTPResponse response) {
		this.cancel = cancel;
		this.response = response;
	}

	public AbstractHTTPResponse getResponse() {
		return response;
	}

	public boolean isCancel() {
		return cancel;
	}
}