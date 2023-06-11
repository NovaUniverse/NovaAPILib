package net.novauniverse.apilib.http.response;

import com.sun.net.httpserver.HttpExchange;
import net.novauniverse.apilib.http.enums.HTTPResponseCode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * A response containing a {@link JSONObject} or {@link JSONArray}
 * 
 * @author Zeeraa
 */
public class JSONResponse extends AbstractHTTPResponse {
	private final JSONContainer json;
	private final int code;
	private final int indentation;

	/* ---- JSONObject ---- */
	/**
	 * Sends a {@link JSONObject} with the default repose code of
	 * {@link HTTPResponseCode#OK} and no indentation
	 * 
	 * @param json The {@link JSONObject} to send
	 */
	public JSONResponse(JSONObject json) {
		this(json, HTTPResponseCode.OK, 0);
	}

	/**
	 * Sends a {@link JSONObject} with the provided response code and no indentation
	 * 
	 * @param json The {@link JSONObject} to send
	 * @param code The {@link HTTPResponseCode}
	 */
	public JSONResponse(JSONObject json, HTTPResponseCode code) {
		this(json, code.getCode(), 0);
	}

	/**
	 * Sends a {@link JSONObject} with the provided response code and indentation
	 * 
	 * @param json        The {@link JSONObject} to send
	 * @param code        The {@link HTTPResponseCode}
	 * @param indentation The indentation to use
	 */
	public JSONResponse(JSONObject json, HTTPResponseCode code, int indentation) {
		this(json, code.getCode(), indentation);
	}

	/**
	 * Sends a {@link JSONObject} with the provided response code and no indentation
	 * 
	 * @param json The {@link JSONObject} to send
	 * @param code The http response code
	 */
	public JSONResponse(JSONObject json, int code) {
		this(json, code, 0);
	}

	/**
	 * Sends a {@link JSONObject} with the provided response code and indentation
	 * 
	 * @param json        The {@link JSONObject} to send
	 * @param code        The http response code
	 * @param indentation The indentation to use
	 */
	public JSONResponse(JSONObject json, int code, int indentation) {
		this(new JSONObjectContainer(json), code, indentation);
	}

	/* ---- JSONArrray ---- */
	/**
	 * Sends a {@link JSONArray} with the default repose code of
	 * {@link HTTPResponseCode#OK} and no indentation
	 * 
	 * @param json The {@link JSONArray} to send
	 */
	public JSONResponse(JSONArray json) {
		this(json, HTTPResponseCode.OK, 0);
	}

	/**
	 * Sends a {@link JSONArray} with the provided response code and no indentation
	 * 
	 * @param json The {@link JSONArray} to send
	 * @param code The {@link HTTPResponseCode}
	 */
	public JSONResponse(JSONArray json, HTTPResponseCode code) {
		this(json, code.getCode(), 0);
	}

	/**
	 * Sends a {@link JSONArray} with the provided response code and indentation
	 * 
	 * @param json        The {@link JSONArray} to send
	 * @param code        The {@link HTTPResponseCode}
	 * @param indentation The indentation to use
	 */
	public JSONResponse(JSONArray json, HTTPResponseCode code, int indentation) {
		this(json, code.getCode(), indentation);
	}

	/**
	 * Sends a {@link JSONArray} with the provided response code and no indentation
	 * 
	 * @param json The {@link JSONArray} to send
	 * @param code The http response code
	 */
	public JSONResponse(JSONArray json, int code) {
		this(json, code, 0);
	}

	/**
	 * Sends a {@link JSONArray} with the provided response code and indentation
	 * 
	 * @param json        The {@link JSONArray} to send
	 * @param code        The http response code
	 * @param indentation The indentation to use
	 */
	public JSONResponse(JSONArray json, int code, int indentation) {
		this(new JSONArrayContainer(json), code, indentation);
	}

	/* ---- Common ---- */
	public JSONResponse(JSONContainer json, int code, int indentation) {
		this.json = json;
		this.code = code;
		this.indentation = indentation;
	}

	/**
	 * @return http response code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @return indentation to use
	 */
	public int getIndentation() {
		return indentation;
	}

	/**
	 * @return The {@link JSONObject} or {@link JSONArray} as text with specified
	 *         indent
	 */
	public String getJSONText() {
		if (indentation > 0) {
			return json.stringify(indentation);
		}
		return json.stringify();
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		exchange.getResponseHeaders().add("Content-type", "application/json; charset=utf-8");
		String response = getJSONText();
		byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
		exchange.sendResponseHeaders(code, bytes.length);
		OutputStream stream = exchange.getResponseBody();
		stream.write(bytes);
		stream.close();
	}
}

interface JSONContainer {
	String stringify();

	String stringify(int indentation);
}

class JSONObjectContainer implements JSONContainer {
	private final JSONObject json;

	public JSONObjectContainer(JSONObject json) {
		this.json = json;
	}

	@Override
	public String stringify() {
		return json.toString();
	}

	@Override
	public String stringify(int indentation) {
		return json.toString(indentation);
	}
}

class JSONArrayContainer implements JSONContainer {
	private final JSONArray json;

	public JSONArrayContainer(JSONArray json) {
		this.json = json;
	}

	@Override
	public String stringify() {
		return json.toString();
	}

	@Override
	public String stringify(int indentation) {
		return json.toString(indentation);
	}
}