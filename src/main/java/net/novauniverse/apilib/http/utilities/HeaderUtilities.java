package net.novauniverse.apilib.http.utilities;

import com.sun.net.httpserver.Headers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utilities from headers
 * 
 * @author Zeeraa
 */
public class HeaderUtilities {
	/**
	 * Convert a {@link com.sun.net.httpserver.Headers} object into a {@link Map}
	 * with headers
	 * 
	 * @param headers The {@link com.sun.net.httpserver.Headers} object
	 * @return {@link Map} with headers
	 */
	public static Map<String, List<String>> headersToMap(Headers headers) {
		Map<String, List<String>> result = new HashMap<>();
		headers.forEach((key, value) -> {
			result.put(key, new ArrayList<>(value));
		});
		return result;
	}
}