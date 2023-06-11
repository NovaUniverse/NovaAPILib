package net.novauniverse.apilib.http.response;

import com.sun.net.httpserver.HttpExchange;
import net.novauniverse.apilib.http.enums.HTTPResponseCode;

import java.io.IOException;

/**
 * Responds with a redirect. The type of redirect can be specified in the
 * constructor
 * 
 * @author Zeeraa
 */
public class RedirectResponse extends AbstractHTTPResponse {
	private final String target;
	private final RedirectType type;

	/**
	 * Redirect the user to a specified url
	 * 
	 * @param target The url to redirect to
	 */
	public RedirectResponse(String target) {
		this(target, RedirectType.TEMPORARY_REDIRECT);
	}

	/**
	 * Redirect the user to a specified url and use the provided redirect type
	 * 
	 * @param target The url to redirect to
	 * @param type   The {@link RedirectType} to use
	 */
	public RedirectResponse(String target, RedirectType type) {
		this.target = target;
		this.type = type;
	}

	/**
	 * @return The target url
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * @return The {@link RedirectType}
	 */
	public RedirectType getType() {
		return type;
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		exchange.getResponseHeaders().add("Location", target);
		exchange.sendResponseHeaders(type.getResponseCode().getCode(), -1);
	}

	/**
	 * Represents the different redirect types to use
	 * 
	 * @author Zeeraa
	 */
	public enum RedirectType {
		TEMPORARY_REDIRECT(HTTPResponseCode.TEMPORARY_REDIRECT),
		PERMANENT_REDIRECT(HTTPResponseCode.PERMANENT_REDIRECT),
		MOVED_PERMANENTLY(HTTPResponseCode.MOVED_PERMANENTLY);

		private final HTTPResponseCode responseCode;

		RedirectType(HTTPResponseCode responseCode) {
			this.responseCode = responseCode;
		}

		/**
		 * @return The corresponding {@link HTTPResponseCode} for the redirect type
		 */
		public HTTPResponseCode getResponseCode() {
			return responseCode;
		}
	}
}