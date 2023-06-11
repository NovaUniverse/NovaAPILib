package net.novauniverse.apilib.http.body;

import com.sun.net.httpserver.HttpExchange;

/**
 * BodyParsers are used to turn the request body to a string
 *
 * @author Zeeraa
 */
public interface BodyParser {
    String parseBody(HttpExchange exchange) throws Exception;
}