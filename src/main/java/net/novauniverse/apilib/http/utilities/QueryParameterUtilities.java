package net.novauniverse.apilib.http.utilities;

import java.util.HashMap;
import java.util.Map;

/**
 * Utilities from query parameters
 * 
 * @author Zeeraa
 */
public class QueryParameterUtilities {
	/**
	 * Get a {@link Map} with all query parameters from a provided query
	 * 
	 * @param query The query to parse
	 * @return {@link Map} with query parameters
	 */
	public static Map<String, String> queryToMap(String query) {
		try {
			Map<String, String> result = new HashMap<>();
			for (String param : query.split("&")) {
				String[] pair = param.split("=");
				if (pair.length > 1) {
					result.put(pair[0], pair[1]);
				} else {
					result.put(pair[0], "");
				}
			}
			return result;
		} catch (Exception e) {
			return new HashMap<String, String>();
		}
	}
}