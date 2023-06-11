package net.novauniverse.apilib.http.request;

import com.sun.net.httpserver.HttpExchange;
import net.novauniverse.apilib.http.enums.HTTPMethod;
import net.novauniverse.apilib.http.HTTPServer;
import net.novauniverse.apilib.http.exception.HTTPMethodNotSupportedException;
import net.novauniverse.apilib.http.utilities.HeaderUtilities;
import net.novauniverse.apilib.http.utilities.QueryParameterUtilities;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Represents an incoming request to the api
 * 
 * @author Zeeraa
 */
public class Request {
	private final HTTPServer server;
	private final HttpExchange exchange;
	private final Map<String, String> queryParameters;
	private final HTTPMethod method;
	private final String body;

	public Request(HTTPServer server, HttpExchange exchange, String body) throws HTTPMethodNotSupportedException {
		this.server = server;
		this.exchange = exchange;
		this.queryParameters = QueryParameterUtilities.queryToMap(exchange.getRequestURI().getQuery());
		this.body = body;
		try {
			this.method = HTTPMethod.valueOf(exchange.getRequestMethod().toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new HTTPMethodNotSupportedException("The HTTP method " + exchange.getRequestMethod() + " is not yet supported by this library", e);
		}
	}

	/**
	 * @return The {@link HTTPServer} that handled the request
	 */
	public HTTPServer getServer() {
		return server;
	}

	/**
	 * @return A copy of all the request headers
	 */
	public Map<String, List<String>> getRequestHeaders() {
		return HeaderUtilities.headersToMap(exchange.getRequestHeaders());
	}

	/**
	 * @return A copy of all the response headers
	 */
	public Map<String, List<String>> getResponseHeaders() {
		return HeaderUtilities.headersToMap(exchange.getResponseHeaders());
	}

	public Request setResponseHeader(String key, String value) {
		exchange.getResponseHeaders().add(key, value);
		return this;
	}

	/**
	 * @return The request body as a string
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @return The remote address of the request
	 */
	public InetSocketAddress remoteAddress() {
		return exchange.getRemoteAddress();
	}

	/**
	 * @return The URL of the request
	 */
	public URI getRequestURI() {
		return exchange.getRequestURI();
	}

	/**
	 * @return {@link HTTPMethod} used in the request
	 */
	public HTTPMethod getMethod() {
		return method;
	}

	/**
	 * @return The sun {@link com.sun.net.httpserver.HttpExchange}
	 */
	public HttpExchange getExchange() {
		return exchange;
	}

	/**
	 * @return a {@link Map} with all query parameters supplied by the user
	 */
	public Map<String, String> getQueryParameters() {
		return queryParameters;
	}

	public InputStream getRequestBody() {
		return exchange.getRequestBody();
	}
}