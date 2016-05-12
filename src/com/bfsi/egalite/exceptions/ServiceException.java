package com.bfsi.egalite.exceptions;

/**
 * Root exception for all exceptions thrown in Service layer
 * 
 * @author Vijay
 * 
 */
public class ServiceException extends Exception {

	private static final long serialVersionUID = -6302755397435948044L;

	public ServiceException() {
	}

	public ServiceException(String detailMessage) {
		super(detailMessage);
	}

	public ServiceException(Throwable throwable) {
		super(throwable);
	}

	public ServiceException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}
}
