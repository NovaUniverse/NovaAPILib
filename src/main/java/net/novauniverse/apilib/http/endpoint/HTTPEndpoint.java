package net.novauniverse.apilib.http.endpoint;

import net.novauniverse.apilib.http.HTTPServer;
import net.novauniverse.apilib.http.auth.Authentication;
import net.novauniverse.apilib.http.auth.AuthenticationProvider;
import net.novauniverse.apilib.http.auth.AuthenticationResponse;
import net.novauniverse.apilib.http.body.BodyParser;
import net.novauniverse.apilib.http.body.DefaultBodyParser;
import net.novauniverse.apilib.http.enums.ExceptionMode;
import net.novauniverse.apilib.http.enums.HTTPMethod;
import net.novauniverse.apilib.http.enums.StandardResponseType;
import net.novauniverse.apilib.http.middleware.HTTPMiddleware;
import net.novauniverse.apilib.http.request.Request;
import net.novauniverse.apilib.http.response.AbstractHTTPResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an api endpoint
 *
 * @author Zeeraa
 */
public abstract class HTTPEndpoint {
	private HTTPMethod[] allowedMethods;
	private boolean useWebServerAuthentication;
	private final List<AuthenticationProvider> authenticationProviders;
	private StandardResponseType standardResponseType;
	private ExceptionMode exceptionMode;
	private boolean requireAuthentication;
	private BodyParser bodyParser;
	private final List<HTTPMiddleware> middlewares;

	public HTTPEndpoint() {
		this.allowedMethods = new HTTPMethod[] {};
		this.useWebServerAuthentication = true;
		this.authenticationProviders = new ArrayList<>();
		this.standardResponseType = null;
		this.exceptionMode = ExceptionMode.INHERIT;
		this.requireAuthentication = false;
		this.bodyParser = new DefaultBodyParser();
		this.middlewares = new ArrayList<>();
	}

	/**
	 * @return <code>true</code> if the endpoint requires authentication
	 */
	public boolean isRequireAuthentication() {
		return requireAuthentication;
	}

	/**
	 * Set if this endpoint needs authentication. if <code>true</code> unauthorized
	 * users will automatically get show an error message
	 *
	 * @param requireAuthentication <code>true</code> if this enpoint should require
	 *                              authentication
	 */
	protected void setRequireAuthentication(boolean requireAuthentication) {
		this.requireAuthentication = requireAuthentication;
	}

	/**
	 * @return the {@link ExceptionMode} this endpoint is using. By default this is
	 *         {@link ExceptionMode#INHERIT}
	 */
	public ExceptionMode getExceptionMode() {
		return exceptionMode;
	}

	/**
	 * Set the exception mode for this endpoint. By default this is
	 * {@link ExceptionMode#INHERIT}
	 *
	 * @param exceptionMode The new {@link ExceptionMode}
	 */
	protected void setExceptionMode(ExceptionMode exceptionMode) {
		this.exceptionMode = exceptionMode;
	}

	/**
	 * @return <code>true</code> if this enpoint should use
	 *         {@link AuthenticationProvider}s from the {@link HTTPServer}
	 */
	public boolean isUseWebServerAuthentication() {
		return useWebServerAuthentication;
	}

	/**
	 * Set if we should use the {@link AuthenticationProvider}s from
	 * {@link HTTPServer}. By disabling this you can create individual enpoints
	 * using their own authentication system
	 *
	 * @param useWebServerAuthentication <code>false</code> to disable
	 *                                   {@link AuthenticationProvider}s from the
	 *                                   {@link HTTPServer}
	 */
	protected void setUseWebServerAuthentication(boolean useWebServerAuthentication) {
		this.useWebServerAuthentication = useWebServerAuthentication;
	}

	/**
	 * @return {@link StandardResponseType} for this endpoint. If <code>null</code>
	 *         the value will be inherited from {@link HTTPServer}
	 */
	public StandardResponseType getStandardResponseType() {
		return standardResponseType;
	}

	/**
	 * Change the default {@link StandardResponseType} for this endpoint. Set to
	 * <code>null</code> to inherit from {@link HTTPServer}
	 *
	 * @param standardResponseType The {@link StandardResponseType} to use
	 */
	protected void setStandardResponseType(StandardResponseType standardResponseType) {
		this.standardResponseType = standardResponseType;
	}

	/**
	 * Clear the allowed {@link HTTPMethod}s
	 */
	protected void clearAllowedMethods() {
		this.setAllowedMethods();
	}

	/**
	 * Set the allowed {@link HTTPMethod}s for this endpoint
	 *
	 * @param allowedMethods Array of {@link HTTPMethod}s to allow
	 */
	protected void setAllowedMethods(HTTPMethod... allowedMethods) {
		this.allowedMethods = allowedMethods;
	}

	/**
	 * @return {@link List} with all {@link AuthenticationProvider}s this endpoint
	 *         has defined
	 */
	public List<AuthenticationProvider> getAuthenticationProviders() {
		return authenticationProviders;
	}

	/**
	 * Add an {@link AuthenticationProvider} to this endpoint
	 *
	 * @param provider The {@link AuthenticationProvider} to add
	 */
	protected void addAuthenticationProvider(AuthenticationProvider provider) {
		authenticationProviders.add(provider);
	}

	/**
	 * @return List of the allowed {@link HTTPMethod}s
	 */
	public HTTPMethod[] getAllowedMethods() {
		return allowedMethods;
	}

	/**
	 * @return The {@link BodyParser} to be used by all requests to this endpoint
	 */
	public BodyParser getBodyParser() {
		return bodyParser;
	}

	/**
	 * Add a {@link HTTPMiddleware} to this endpoint
	 * 
	 * @param middleware The {@link HTTPMiddleware} to add
	 */
	protected void addMiddleware(HTTPMiddleware middleware) {
		middlewares.add(middleware);
	}

	/**
	 * @return {@link List} with all {@link HTTPMiddleware}s for this endpoint
	 */
	public List<HTTPMiddleware> getMiddlewares() {
		return middlewares;
	}

	/**
	 * Set the {@link BodyParser} to use
	 *
	 * @param bodyParser The new {@link BodyParser} to use
	 */
	protected void setBodyParser(BodyParser bodyParser) {
		this.bodyParser = bodyParser;
	}

	/**
	 * Handle a user request
	 *
	 * @param request        The {@link Request} object
	 * @param authentication The {@link Authentication} object or <code>null</code>
	 *                       if the user is not authenticated
	 * @return The {@link AbstractHTTPResponse} to send
	 * @throws Exception If anything goes wrong with the request
	 */
	public abstract AbstractHTTPResponse handleRequest(Request request, Authentication authentication) throws Exception;

	/**
	 * Override this method to prevent certain users from accessing the api. By
	 * default, this allows all authenticated users
	 *
	 * @param authentication The authentication object. This will be
	 *                       <code>null</code> if no authentication was provided
	 * @param request        The request
	 * @return the AuthenticationResponse
	 */
	public AuthenticationResponse handleAuthentication(Authentication authentication, Request request) {
		return AuthenticationResponse.OK;
	}
}
