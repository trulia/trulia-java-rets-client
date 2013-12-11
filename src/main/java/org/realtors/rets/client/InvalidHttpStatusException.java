package org.realtors.rets.client;

import org.apache.http.StatusLine;

public class InvalidHttpStatusException extends RetsException {
	public InvalidHttpStatusException(StatusLine status) {
		super("Status code (" + status.getStatusCode() + ") " + status.getReasonPhrase());
	}
	public InvalidHttpStatusException(StatusLine status, String message) {
		super("Status code (" + status.getStatusCode() + ") " + status.getReasonPhrase() +" '"+message+"'");
	}
}