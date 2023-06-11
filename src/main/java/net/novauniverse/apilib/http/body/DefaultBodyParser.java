package net.novauniverse.apilib.http.body;

import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * The default {@link BodyParser} implementation
 *
 * @author Zeeraa
 */
public class DefaultBodyParser implements BodyParser {
    private Charset charset;

    public DefaultBodyParser() {
        this(StandardCharsets.UTF_8);
    }

    public DefaultBodyParser(Charset charset) {
        this.charset = charset;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    @Override
    public String parseBody(HttpExchange exchange) throws IOException {
        return IOUtils.toString(exchange.getRequestBody(), charset);
    }
}