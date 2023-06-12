package net.novauniverse.apilib.http.middleware;

import net.novauniverse.apilib.http.auth.Authentication;
import net.novauniverse.apilib.http.endpoint.HTTPEndpoint;
import net.novauniverse.apilib.http.request.Request;
import net.novauniverse.apilib.http.response.AbstractHTTPResponse;

/**
 * Middlewares can be used to run code before the request and can be used to
 * modify behavior without having to write the code multiple times for each
 * endpoint.<br>
 * For passing data use {@link Request#getMiddlewareData()} and set the values
 * in there.<br>
 * Middlewares can also cancel the request and send their own
 * {@link AbstractHTTPResponse} to the user.
 * 
 * @author Zeeraa
 */
public abstract class HTTPMiddleware {
	private MiddlewarePriority priority;
	private MiddlewareType type;

	public HTTPMiddleware() {
		priority = MiddlewarePriority.MEDIUM;
		type = MiddlewareType.POST_AUTHENTICATION;
	}

	/**
	 * @return The {@link MiddlewarePriority} of this middleware
	 */
	public MiddlewarePriority getPriority() {
		return priority;
	}

	/**
	 * @return The {@link MiddlewareType} of this middleware
	 */
	public MiddlewareType getType() {
		return type;
	}

	/**
	 * Set the new priority
	 * 
	 * @param priority The new {@link MiddlewarePriority} to use
	 */
	protected void setPriority(MiddlewarePriority priority) {
		this.priority = priority;
	}

	/**
	 * Set the new type
	 * 
	 * @param type The new {@link MiddlewareType} of this middleware
	 */
	protected void setType(MiddlewareType type) {
		this.type = type;
	}

	/**
	 * This method is called when the middleware executes.<br>
	 * For passing data use {@link Request#getMiddlewareData()} and set the values
	 * in there.<br>
	 * Note that the authentication parameter will always be <code>null</code> if
	 * the {@link MiddlewareType} is set to
	 * {@link MiddlewareType#PRE_AUTHENTICATION}
	 * 
	 * @param endpoint       The requested {@link HTTPEndpoint}
	 * @param request        The {@link Request} from the user
	 * @param authentication The {@link Authentication} object. This will be
	 *                       <code>null</code> if the user is not authenticated or
	 *                       if the {@link MiddlewareType} is set to
	 *                       {@link MiddlewareType#PRE_AUTHENTICATION}
	 * 
	 * @return The {@link MiddlewareResponse} specifying what action to take
	 */
	public abstract MiddlewareResponse handleRequest(HTTPEndpoint endpoint, Request request, Authentication authentication);
}