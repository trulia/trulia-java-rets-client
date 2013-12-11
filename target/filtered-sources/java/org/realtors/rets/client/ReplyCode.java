package org.realtors.rets.client;

import java.util.Map;
import java.util.HashMap;

public class ReplyCode {
	// static initialization loop.... this declaration _MUST_ come before the members
	private static final Map<Integer,ReplyCode> CODES = new HashMap<Integer,ReplyCode>();

	public static final ReplyCode SUCCESS = new ReplyCode(0, "Success");
	public static final ReplyCode ZERO_BALANCE = new ReplyCode(20003, "Zero balance");
	public static final ReplyCode BROKER_CODE_REQUIRED = new ReplyCode(20012, "Broker code required");
	public static final ReplyCode BROKER_CODE_INVALID = new ReplyCode(20013, "Broker Code Invalid");
	public static final ReplyCode ADDTIONAL_LOGIN_NOT_PREMITTED = new ReplyCode(20022, "Additional login not permitted");
	public static final ReplyCode MISCELLANEOUS_LOGIN_ERROR = new ReplyCode(20036, "Miscellaneous server login error");
	public static final ReplyCode CLIENT_PASSWORD_INVALID = new ReplyCode(20037, "Client passsword invalid");
	public static final ReplyCode SERVER_TEMPORARILY_DISABLED = new ReplyCode(20050, "Server temporarily disabled");
	public static final ReplyCode UNKNOWN_QUERY_FIELD = new ReplyCode(20200, "Unknown Query Field");
	public static final ReplyCode NO_RECORDS_FOUND = new ReplyCode(20201, "No Records Found");
	public static final ReplyCode INVALID_SELECT = new ReplyCode(20202, "Invalid select");
	public static final ReplyCode MISCELLANOUS_SEARCH_ERROR = new ReplyCode(20203, "Miscellaneous search error");
	public static final ReplyCode INVALID_QUERY_SYNTAX = new ReplyCode(20206, "Invalid query syntax");
	public static final ReplyCode UNAUTHORIZED_QUERY = new ReplyCode(20207, "Unauthorized query");
	public static final ReplyCode MAXIMUM_RECORDS_EXCEEDED = new ReplyCode(20208, "Maximum records exceeded");
	public static final ReplyCode SEARCH_TIMED_OUT = new ReplyCode(20209, "Search timed out");
	public static final ReplyCode TOO_MANY_OUTSTANDING_QUERIES = new ReplyCode(20210, "Too many outstanding queries");
	public static final ReplyCode INVALID_RESOURCE_GETOBJECT = new ReplyCode(20400, "Invalid Resource");
	public static final ReplyCode INVALID_TYPE_GETOBJECT = new ReplyCode(20401, "Invalid Type");
	public static final ReplyCode INVALID_IDENTIFIER_GETOBJECT = new ReplyCode(20402, "Invalid Identifier");
	public static final ReplyCode NO_OBJECT_FOUND = new ReplyCode(20403, "No Object Found");
	public static final ReplyCode UNSUPPORTED_MIME_TYPE_GETOBJECT = new ReplyCode(20406, "Unsupported MIME Type");
	public static final ReplyCode UNAUTHORIZED_RETRIEVAL_GETOBJECT = new ReplyCode(20407, "Unauthorized Retrieval");
	public static final ReplyCode RESOURCE_UNAVAILABLE_GETOBJECT = new ReplyCode(20408, "Resource Unavailable");
	public static final ReplyCode OBJECT_UNAVAILABLE = new ReplyCode(20409, "Object Unavailable");
	public static final ReplyCode REQUEST_TOO_LARGE_GETOBJECT = new ReplyCode(20410, "Request Too Large");
	public static final ReplyCode TIMEOUT_GETOBJECT = new ReplyCode(20411, "Timeout");
	public static final ReplyCode TOO_MANY_OUTSTANDING_QUERIES_GETOBJECT = new ReplyCode(20412,"Too Many Outstanding Queries");
	public static final ReplyCode MISCELLANEOUS_ERROR_GETOBJECT = new ReplyCode(20413, "Miscellaneous Error");
	public static final ReplyCode INVALID_RESOURCE = new ReplyCode(20500, "Invalid resource");
	public static final ReplyCode INVALID_TYPE = new ReplyCode(20501, "Invalid type");
	public static final ReplyCode INVALID_IDENTIFIER = new ReplyCode(20502, "Invalid identifier");
	public static final ReplyCode NO_METADATA_FOUND = new ReplyCode(20503, "No metadata found");
	public static final ReplyCode UNSUPPORTED_MIME_TYPE = new ReplyCode(20506, "Unsupported MIME type");
	public static final ReplyCode UNAUTHORIZED_RETRIEVAL = new ReplyCode(20507, "Unauthorized retrieval");
	public static final ReplyCode RESOURCE_UNAVAILABLE = new ReplyCode(20508, "Resource unavailable");
	public static final ReplyCode METADATA_UNAVAILABLE = new ReplyCode(20509, "Metadata unavailable");
	public static final ReplyCode REQUEST_TOO_LARGE = new ReplyCode(20510, "Request too large");
	public static final ReplyCode TIMEOUT = new ReplyCode(20511, "Timeout");
	public static final ReplyCode TOO_MANY_OUSTANDING_REQUESTS = new ReplyCode(20512, "Too many outstanding requests");
	public static final ReplyCode MISCELLANEOUS_ERROR = new ReplyCode(20513, "Miscellanous error");
	public static final ReplyCode REQUESTED_DTD_UNAVAILABLE = new ReplyCode(20514, "Requested DTD unvailable");

	private final int mValue;
	private final String mMessage;

	private ReplyCode(int value, String message) {
		this.mValue = value;
		this.mMessage = message;
		if (CODES.containsValue(new Integer(value))) 
			throw new IllegalArgumentException(String.format("value already used: %s ( %s ) ",value,message));
		CODES.put(new Integer(value), this);
	}


	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ReplyCode)) {
			return false;
		}

		ReplyCode rhs = (ReplyCode) o;
		return (this.mValue == rhs.mValue);
	}

	public boolean equals(int value) {
		return this.mValue == value;
	}

	@Override
	public String toString() {
		return String.format("%s (%s)",this.mValue,this.mMessage);
	}

	public int getValue() {
		return this.mValue;
	}

	public String getMessage() {
		return this.mMessage;
	}

	public static ReplyCode fromValue(int value) {
		ReplyCode replyCode = CODES.get(new Integer(value));
		if (replyCode != null) 
			return replyCode;

		return new ReplyCode(value, "Unknown");
	}

}
