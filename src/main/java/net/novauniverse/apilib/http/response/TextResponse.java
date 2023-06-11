package net.novauniverse.apilib.http.response;

import com.sun.net.httpserver.HttpExchange;
import net.novauniverse.apilib.http.enums.HTTPResponseCode;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Send a text response to the user
 * 
 * @author Zeeraa
 */
public class TextResponse extends AbstractHTTPResponse {
	private final String response;
	private final int code;

	private final TextResponseMimeType mimeType;

	/**
	 * A text response with the default {@link HTTPResponseCode} of
	 * {@link HTTPResponseCode#OK}
	 * 
	 * @param text The text to send
	 */
	public TextResponse(String text) {
		this(text, TextResponseMimeType.TEXT_PLAIN);
	}

	/**
	 * A text response with the provided {@link HTTPResponseCode}
	 * 
	 * @param text The text to send
	 * @param code The {@link HTTPResponseCode} to use
	 */
	public TextResponse(String text, HTTPResponseCode code) {
		this(text, code, TextResponseMimeType.TEXT_PLAIN);
	}

	/**
	 * A text response with the provided http response code
	 * 
	 * @param text The text to send
	 * @param code The http response code to use
	 */
	public TextResponse(String text, int code) {
		this(text, code, TextResponseMimeType.TEXT_PLAIN);
	}

	/**
	 * A text response with the provided mime type
	 * 
	 * @param text     The text to send
	 * @param mimeType The {@link TextResponseMimeType} to use
	 */
	public TextResponse(String text, TextResponseMimeType mimeType) {
		this(text, HTTPResponseCode.OK);
	}

	/**
	 * A text response with the provided {@link HTTPResponseCode} and mime type
	 * 
	 * @param text     The text to send
	 * @param code     The {@link HTTPResponseCode} to use
	 * @param mimeType The {@link TextResponseMimeType} to use
	 */
	public TextResponse(String text, HTTPResponseCode code, TextResponseMimeType mimeType) {
		this(text, code.getCode());
	}

	/**
	 * A text response with the provided http response code and mime type
	 * 
	 * @param text     The text to send
	 * @param code     The http response code to use
	 * @param mimeType The {@link TextResponseMimeType} to use
	 */
	public TextResponse(String text, int code, TextResponseMimeType mimeType) {
		this.response = text;
		this.code = code;
		this.mimeType = mimeType;
	}

	/**
	 * @return The text to send
	 */
	public String getResponse() {
		return response;
	}

	/**
	 * @return The http response code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @return The {@link TextResponseMimeType}
	 */
	public TextResponseMimeType getMimeType() {
		return mimeType;
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		exchange.getResponseHeaders().add("Content-type", mimeType.getMimeType() + "; charset=utf-8");
		byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
		exchange.sendResponseHeaders(code, bytes.length);
		OutputStream stream = exchange.getResponseBody();
		stream.write(bytes);
		stream.close();
	}

	/**
	 * This enum contains the different mime types for text
	 * 
	 * @author Zeeraa
	 */
	public enum TextResponseMimeType {
		TEXT_PLAIN("text/plain"),
		TEXT_HTML("text/html"),
		TEXT_CSV("text/csv");

		private final String mimeType;

		TextResponseMimeType(String mimeType) {
			this.mimeType = mimeType;
		}

		public String getMimeType() {
			return mimeType;
		}
	}
}