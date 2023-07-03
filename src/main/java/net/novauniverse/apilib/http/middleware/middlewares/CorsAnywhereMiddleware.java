package net.novauniverse.apilib.http.middleware.middlewares;

import net.novauniverse.apilib.http.auth.Authentication;
import net.novauniverse.apilib.http.endpoint.HTTPEndpoint;
import net.novauniverse.apilib.http.middleware.HTTPMiddleware;
import net.novauniverse.apilib.http.middleware.MiddlewarePriority;
import net.novauniverse.apilib.http.middleware.MiddlewareResponse;
import net.novauniverse.apilib.http.middleware.MiddlewareType;
import net.novauniverse.apilib.http.request.Request;

/**
 * This middlewares send the <code>Access-Control-Allow-Origin: *</code>
 * header.<br>
 * <br>
 * Its type is {@link MiddlewareType#PRE_AUTHENTICATION} and has the priority of
 * {@link MiddlewarePriority#LOWEST}
 * 
 * @author Zeeraa
 */
public class CorsAnywhereMiddleware extends HTTPMiddleware {
	public CorsAnywhereMiddleware() {
		this.setPriority(MiddlewarePriority.LOWEST);
		this.setType(MiddlewareType.PRE_AUTHENTICATION);
	}

	@Override
	public MiddlewareResponse handleRequest(HTTPEndpoint endpoint, Request request, Authentication authentication) {
		request.setResponseHeader("Access-Control-Allow-Origin", "*");
		request.setResponseHeader("Access-Control-Allow-Headers", "*");
		return MiddlewareResponse.CONTINUE;
	}

	@Override
	public void handleOptionsRequest(Request request) {
		request.setResponseHeader("Access-Control-Allow-Origin", "*");
		request.setResponseHeader("Access-Control-Allow-Headers", "*");
}