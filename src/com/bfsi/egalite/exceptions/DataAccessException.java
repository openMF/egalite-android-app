package com.bfsi.egalite.exceptions;

/**
 * Root exception for all exceptions from data access layer
 * 
 * @author vijay
 * 
 */
public class DataAccessException extends RuntimeException {
	private static final long serialVersionUID = -3577401435756331015L;

	public DataAccessException() {
	}

	public DataAccessException(String detailMessage) {
		super(detailMessage);
	}

	public DataAccessException(Throwable throwable) {
		super(throwable);
	}

	public DataAccessException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}
}
