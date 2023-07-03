package net.novauniverse.apilib.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import net.novauniverse.apilib.http.auth.Authentication;
import net.novauniverse.apilib.http.auth.AuthenticationProvider;
import net.novauniverse.apilib.http.auth.AuthenticationResponse;
import net.novauniverse.apilib.http.endpoint.HTTPEndpoint;
import net.novauniverse.apilib.http.enums.ExceptionMode;
import net.novauniverse.apilib.http.enums.HTTPMethod;
import net.novauniverse.apilib.http.enums.HTTPResponseCode;
import net.novauniverse.apilib.http.enums.StandardResponseType;
import net.novauniverse.apilib.http.exception.HTTPMethodNotSupportedException;
import net.novauniverse.apilib.http.files.StaticFileHandler;
import net.novauniverse.apilib.http.middleware.HTTPMiddleware;
import net.novauniverse.apilib.http.middleware.MiddlewarePriority;
import net.novauniverse.apilib.http.middleware.MiddlewareResponse;
import net.novauniverse.apilib.http.middleware.MiddlewareType;
import net.novauniverse.apilib.http.request.Request;
import net.novauniverse.apilib.http.response.AbstractHTTPResponse;
import net.novauniverse.apilib.http.response.JSONResponse;
import net.novauniverse.apilib.http.response.TextResponse;
import org.json.JSONObject;

import java.io.*;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class HTTPServer {
    public static final ExceptionMode DEFAULT_EXCEPTION_MODE = ExceptionMode.MESSAGE;

    private final List<AuthenticationProvider> authenticationProviders;
    private final InetSocketAddress address;
    private final HttpServer httpServer;
    private boolean hasShutDown;
    private StandardResponseType standardResponseType;
    private ExceptionMode exceptionMode;
    private List<HTTPMiddleware> middlewares;
    private List<Consumer<Exception>> exceptionConsumers;

    public List<AuthenticationProvider> getAuthenticationProviders() {
        return authenticationProviders;
    }

    public HTTPServer addAuthenticationProvider(AuthenticationProvider authenticationProvider) {
        authenticationProviders.add(authenticationProvider);
        return this;
    }

    /**
     * Create and start a http server on the provided port
     *
     * @param port The port number to use
     * @throws IOException   If the port number is invalid
     * @throws BindException If port is already in use
     */
    public HTTPServer(int port) throws IOException, BindException {
        this(new InetSocketAddress(port));
    }

    /**
     * Create and start a http server on the provided address
     *
     * @param address The port number to use
     * @throws IOException   If the address is invalid
     * @throws BindException If port is already in use
     */
    public HTTPServer(InetSocketAddress address) throws IOException, BindException {
        this.address = address;
        this.authenticationProviders = new ArrayList<>();
        this.standardResponseType = StandardResponseType.JSON;
        this.exceptionMode = DEFAULT_EXCEPTION_MODE;
        this.httpServer = HttpServer.create(address, 0);
        this.middlewares = new ArrayList<>();
        this.exceptionConsumers = new ArrayList<>();
        httpServer.start();
    }

    /**
     * Set the {@link ExceptionMode} to use
     *
     * @param exceptionMode The new {@link ExceptionMode} to use
     * @return this {@link HTTPServer} instance so that calls can be chained
     */
    public HTTPServer setExceptionMode(ExceptionMode exceptionMode) {
        if (exceptionMode == ExceptionMode.INHERIT) {
            this.exceptionMode = DEFAULT_EXCEPTION_MODE;
        } else {
            this.exceptionMode = exceptionMode;
        }
        return this;
    }

    /**
     * @return The current {@link ExceptionMode}
     */
    public ExceptionMode getExceptionMode() {
        return exceptionMode;
    }

    /**
     * @return The current {@link StandardResponseType}
     */
    public StandardResponseType getStandardResponseType() {
        return standardResponseType;
    }

    /**
     * Change the {@link StandardResponseType}. Default is
     * {@link StandardResponseType#JSON}
     *
     * @param standardResponseType The new {@link StandardResponseType} to use
     * @return this {@link HTTPServer} instance so that calls can be chained
     */
    public HTTPServer setStandardResponseType(StandardResponseType standardResponseType) {
        this.standardResponseType = standardResponseType;
        return this;
    }

    /**
     * @return The address the server is listening on
     */
    public InetSocketAddress getAddress() {
        return address;
    }

    /**
     * Stop the web server
     *
     * @return <code>true</code> on success
     */
    public boolean stop() {
        return this.stop(10);
    }

    /**
     * Stop the web server
     *
     * @param delay The delay to wait in seconds for connections to close
     * @return <code>true</code> on success
     */
    public boolean stop(int delay) {
        if (delay < 0) {
            throw new IllegalArgumentException("Delay cant be less than 0");
        }

        if (hasShutDown) {
            return false;
        }

        hasShutDown = true;
        httpServer.stop(10);
        return true;
    }

    /**
     * Add a {@link HTTPEndpoint} to the web server
     *
     * @param path     The path of the api endpoint
     * @param endpoint The {@link HTTPEndpoint} to add
     * @return this {@link HTTPServer} instance so that calls can be chained
     */
    public HTTPServer addEndpoint(String path, HTTPEndpoint endpoint) {
        httpServer.createContext(path, new ProxiedHttpHandler(path, this, endpoint));
        return this;
    }

    /**
     * Create a handler that allows the user to access files on the server. Can be
     * used to serve static html content
     *
     * @param url    The base url
     * @param folder The folder to share content from
     * @return this {@link HTTPServer} instance so that calls can be chained
     */
    public HTTPServer addStaticFileHandler(String url, File folder) {
        return this.addStaticFileHandler(url, folder, "index.html");
    }

    /**
     * Create a handler that allows the user to access files on the server. Can be
     * used to serve static html content
     *
     * @param url            The base url
     * @param folder         The folder to share content from
     * @param directoryIndex The name of the default index file to share
     * @return this {@link HTTPServer} instance so that calls can be chained
     */
    public HTTPServer addStaticFileHandler(String url, File folder, String directoryIndex) {
        if (!url.isEmpty() && url.charAt(url.length() - 1) == '/') {
            url = url.substring(0, url.length() - 1);
        }
        httpServer.createContext(url.length() == 0 ? "/" : url, new StaticFileHandler(url, folder.getAbsolutePath(), directoryIndex));
        return this;
    }

    /**
     * @return <code>true</code> if the {@link HTTPServer#stop()} function has been
     * called
     */
    public boolean hasShutDown() {
        return hasShutDown;
    }

    /**
     * @return The underlying {@link com.sun.net.httpserver.HttpServer}
     */
    public HttpServer getHttpServer() {
        return httpServer;
    }

    /**
     * @return A {@link List} with all {@link HTTPMiddleware} to use for the request
     */
    public List<HTTPMiddleware> getMiddlewares() {
        return middlewares;
    }

    /**
     * Add a {@link HTTPMiddleware} for this request
     *
     * @param middleware The {@link HTTPMiddleware} to add
     * @return this {@link HTTPServer} instance so that calls can be chained
     */
    public HTTPServer addMiddleware(HTTPMiddleware middleware) {
        middlewares.add(middleware);
        return this;
    }

    /**
     * Add a {@link Consumer} that will be called if an exception is caught while
     * processing a request
     *
     * @param consumer The {@link Consumer} of type {@link Exception} to add
     * @return this {@link HTTPServer} instance so that calls can be chained
     */
    public HTTPServer addExceptionConsumer(Consumer<Exception> consumer) {
        exceptionConsumers.add(consumer);
        return this;
    }

    /**
     * @return {@link List} with {@link Consumer}s that will be called when an
     * exception gets caught while processing a request
     */
    public List<Consumer<Exception>> getExceptionConsumers() {
        return exceptionConsumers;
    }

    /**
     * The internal {@link com.sun.net.httpserver.HttpHandler} used by the web
     * server
     *
     * @author Zeeraa
     */
    public static class ProxiedHttpHandler implements HttpHandler {
        private final String path;
        private final HTTPServer server;
        private final HTTPEndpoint endpoint;

        private ProxiedHttpHandler(String path, HTTPServer server, HTTPEndpoint endpoint) {
            this.path = path;
            this.server = server;
            this.endpoint = endpoint;
        }

        public String getPath() {
            return path;
        }

        public HTTPEndpoint getEndpoint() {
            return endpoint;
        }

        public HTTPServer getServer() {
            return server;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            AbstractHTTPResponse response = processRequest(exchange);
			if(response == null) {
				return;
			}
            response.handle(exchange);
        }

        public AbstractHTTPResponse processRequest(HttpExchange exchange) {
            try {
                // Get default response type
                StandardResponseType standardResponseType = server.getStandardResponseType();
                if (endpoint.getStandardResponseType() != null) {
                    standardResponseType = endpoint.getStandardResponseType();
                }

                // Get exception mode
                ExceptionMode exceptionMode = server.getExceptionMode();
                if (endpoint.getExceptionMode() != null) {
                    if (endpoint.getExceptionMode() != ExceptionMode.INHERIT) {
                        exceptionMode = endpoint.getExceptionMode();
                    }
                }

                // Request body
                String body = null;
                if (endpoint.getBodyParser() != null) {
                    try {
                        body = endpoint.getBodyParser().parseBody(exchange);
                    } catch (Exception e) {
                        consumeException(e);
                        return generateExceptionResponse(standardResponseType, exceptionMode, e, "An internal error occurred while processing the request body");
                    }
                }

                // Setup request object
                Request request;
                try {
                    request = new Request(server, exchange, body);
                } catch (HTTPMethodNotSupportedException e) {
                    return standardResponseType.error(e.getMessage(), HTTPResponseCode.METHOD_NOT_ALLOWED);
                }

                // Allowed method validation
                HTTPMethod method = request.getMethod();

                if (method == HTTPMethod.OPTIONS) {
                    getServer().getMiddlewares().forEach(middleware -> middleware.handleOptionsRequest(request));
                    getEndpoint().getMiddlewares().forEach(middleware -> middleware.handleOptionsRequest(request));

					List<HTTPMethod> allowed;

					if(endpoint.getAllowedMethods().length > 0) {
						allowed = Arrays.asList(endpoint.getAllowedMethods());
					} else {
						allowed = Arrays.asList(HTTPMethod.values());
					}

					StringBuilder sb = new StringBuilder();
					allowed.forEach(allowedMethod -> {
						sb.append(allowedMethod.name() + ", ");
					});

					if (sb.length() > 0) {
						sb.setLength(sb.length() - 2);
					}

					request.getExchange().getResponseHeaders().add("Allow", sb.toString());

					byte[] response = new byte[0];
					request.getExchange().sendResponseHeaders(200, response.length);
					OutputStream os = request.getExchange().getResponseBody();
					os.write(response);
					os.close();

					return null;
                }

                boolean methodAllowed = false;
                if (endpoint.getAllowedMethods().length == 0) {
                    methodAllowed = true;
                } else {
                    for (HTTPMethod allowed : endpoint.getAllowedMethods()) {
                        if (method == allowed) {
                            methodAllowed = true;
                            break;
                        }
                    }
                }

                if (!methodAllowed) {
                    return standardResponseType.error("Method " + method.name() + " is not allowed for this endpoint", HTTPResponseCode.METHOD_NOT_ALLOWED);
                }

                // Pre authentication middlewares
                try {
                    List<HTTPMiddleware> preAuthMiddlewares = new ArrayList<>();
                    preAuthMiddlewares.addAll(server.getMiddlewares().stream().filter(m -> m.getType() == MiddlewareType.PRE_AUTHENTICATION).collect(Collectors.toList()));
                    preAuthMiddlewares.addAll(endpoint.getMiddlewares().stream().filter(m -> m.getType() == MiddlewareType.PRE_AUTHENTICATION).collect(Collectors.toList()));
                    preAuthMiddlewares.sort(new MiddlewarePriority.MiddlewarePrioritySorter());
                    for (HTTPMiddleware middleware : preAuthMiddlewares) {
                        MiddlewareResponse response = middleware.handleRequest(endpoint, request, null);
                        if (response.isCancel()) {
                            return response.getResponse();
                        }
                    }
                } catch (Exception e) {
                    consumeException(e);
                    return generateExceptionResponse(standardResponseType, exceptionMode, e, "An internal error occurred while processing pre authentication middlewares");
                }

                // Authentication
                Authentication authentication = null;

                try {
                    // Try with endpoint specific first
                    for (AuthenticationProvider provider : endpoint.getAuthenticationProviders()) {
                        authentication = provider.authenticate(request);
                        if (authentication != null) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    consumeException(e);
                    return generateExceptionResponse(standardResponseType, exceptionMode, e, "An internal error occurred while processing authentication");
                }

                // Then with the ones from HTTPServer
                if (endpoint.isUseWebServerAuthentication()) {
                    if (authentication == null) {
                        for (AuthenticationProvider provider : server.getAuthenticationProviders()) {
                            authentication = provider.authenticate(request);
                            if (authentication != null) {
                                break;
                            }
                        }
                    }
                }

                // Validate authentication
                if (endpoint.isRequireAuthentication() && authentication == null) {
                    return standardResponseType.error("Unauthenticated", HTTPResponseCode.UNAUTHORIZED);
                }

                AuthenticationResponse authResponse = endpoint.handleAuthentication(authentication, request);
                if (!authResponse.isSuccess()) {
                    return standardResponseType.error(authResponse.getErrorMessage(), authResponse.getCode());
                }

                // Post authentication middlewares
                try {
                    List<HTTPMiddleware> postAuthMiddlewares = new ArrayList<>();
                    postAuthMiddlewares.addAll(server.getMiddlewares().stream().filter(m -> m.getType() == MiddlewareType.POST_AUTHENTICATION).collect(Collectors.toList()));
                    postAuthMiddlewares.addAll(endpoint.getMiddlewares().stream().filter(m -> m.getType() == MiddlewareType.POST_AUTHENTICATION).collect(Collectors.toList()));
                    postAuthMiddlewares.sort(new MiddlewarePriority.MiddlewarePrioritySorter());
                    for (HTTPMiddleware middleware : postAuthMiddlewares) {
                        MiddlewareResponse response = middleware.handleRequest(endpoint, request, null);
                        if (response.isCancel()) {
                            return response.getResponse();
                        }
                    }
                } catch (Exception e) {
                    consumeException(e);
                    return generateExceptionResponse(standardResponseType, exceptionMode, e, "An internal error occurred while processing pre authentication middlewares");
                }

                // Make request
                try {
                    return endpoint.handleRequest(request, authentication);
                } catch (Exception e) {
                    // Error handling
                    consumeException(e);
                    return generateExceptionResponse(standardResponseType, exceptionMode, e, "An internal error occurred while processing your request");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Unhandled exception in ProxiedHttpHandler");
                return new TextResponse("Unhandled exception in ProxiedHttpHandler", HTTPResponseCode.INTERNAL_SERVER_ERROR);
            }
        }

        public void consumeException(Exception exception) {
            server.getExceptionConsumers().forEach(c -> c.accept(exception));
        }

        public AbstractHTTPResponse generateExceptionResponse(StandardResponseType standardResponseType, ExceptionMode exceptionMode, Exception exception, String message) {
            if (standardResponseType == StandardResponseType.JSON) {
                JSONObject error = new JSONObject();

                if (exceptionMode == ExceptionMode.TYPE) {
                    error.put("error", message + ". " + exception.getClass().getName());
                    error.put("exception_type", exception.getClass().getName());
                } else if (exceptionMode == ExceptionMode.MESSAGE) {
                    error.put("error", message + ". " + exception.getClass().getName() + ". " + exception.getMessage());
                    error.put("exception_type", exception.getClass().getName());
                    error.put("message", exception.getMessage());
                } else if (exceptionMode == ExceptionMode.STACKTRACE) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    exception.printStackTrace(pw);

                    error.put("error", message + ". " + exception.getClass().getName() + ". " + exception.getMessage());
                    error.put("exception_type", exception.getClass().getName());
                    error.put("message", exception.getMessage());
                    error.put("stacktrace", sw.toString());

                    pw.close();
                } else {
                    error.put("error", message);
                }

                return new JSONResponse(error, HTTPResponseCode.INTERNAL_SERVER_ERROR, 4);
            } else {
                String text = message;

                if (exceptionMode == ExceptionMode.TYPE) {
                    text = message + ". " + exception.getClass().getName();
                } else if (exceptionMode == ExceptionMode.MESSAGE) {
                    text = message + ". " + exception.getClass().getName() + ". " + exception.getMessage();
                } else if (exceptionMode == ExceptionMode.STACKTRACE) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    exception.printStackTrace(pw);

                    text = message + ". " + exception.getClass().getName() + ". " + exception.getMessage() + "\n" + sw.toString();

                    pw.close();
                }

                return new TextResponse(text, HTTPResponseCode.INTERNAL_SERVER_ERROR, TextResponse.TextResponseMimeType.TEXT_PLAIN);
            }
        }
    }
}