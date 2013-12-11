package org.realtors.rets.client;

import org.apache.commons.lang.exception.NestableException;

public class RetsException extends NestableException {
	public RetsException() {
		super();
	}

	public RetsException(String message) {
		super(message);
	}

	public RetsException(String message, Throwable cause) {
		super(message, cause);
	}

	public RetsException(Throwable cause) {
		super(cause);
	}
}
