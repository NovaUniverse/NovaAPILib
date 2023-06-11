package net.novauniverse.apilib.http.exception;

public class HTTPMethodNotSupportedException extends Exception {
	private static final long serialVersionUID = -5782670625101315732L;

	public HTTPMethodNotSupportedException() {
    }

    public HTTPMethodNotSupportedException(String message) {
        super(message);
    }

    public HTTPMethodNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public HTTPMethodNotSupportedException(Throwable cause) {
        super(cause);
    }
}