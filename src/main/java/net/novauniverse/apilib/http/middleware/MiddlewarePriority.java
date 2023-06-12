package net.novauniverse.apilib.http.middleware;

import java.util.Comparator;

/**
 * Specifies the priority of the middleware. The highest priority middlewares
 * will run last to allow them to have the final say in requests
 * 
 * @author Zeeraa
 */
public enum MiddlewarePriority {
	/**
	 * Middlewares with this priority will run first
	 */
	LOWEST(-32),
	/*
	 * Middlewares with this priority will run second
	 */
	LOW(-16),
	/**
	 * Middlewares with this priority will run in the third place
	 */
	MEDIUM(0),
	/**
	 * Middlewares with this priority will run in the forth place
	 */
	HIGH(16),
	/**
	 * Middlewares with this priority will run last
	 */
	HIGHEST(32);

	private final int priority;

	private MiddlewarePriority(int priority) {
		this.priority = priority;
	}

	/**
	 * @return The priority as an integer so that it can be sorted
	 */
	public int getPriority() {
		return priority;
	}

	public static class MiddlewarePrioritySorter implements Comparator<HTTPMiddleware> {
		public int compare(HTTPMiddleware m1, HTTPMiddleware m2) {
			return Integer.compare(m1.getPriority().getPriority(), m2.getPriority().getPriority());
		}
	}
}