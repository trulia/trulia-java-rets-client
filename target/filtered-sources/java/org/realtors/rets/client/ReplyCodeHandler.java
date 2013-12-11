package org.realtors.rets.client;

/**
 * @author jrayburn
 */
public interface ReplyCodeHandler {

	/**
	 * ReplyCodeHandler can choose to handle reply codes
	 * that are non-zero reply codes in its own fashion.
	 * 
	 * This is intended to be used to allow the SearchResultCollector
	 * to choose to throw InvalidReplyCodeException if the response is
	 * 20201 (Empty) or 20208 (MaxRowsExceeded).
	 * 
	 * @param replyCode The RETS reply code
	 * 
	 * @throws InvalidReplyCodeException Thrown if reply code is 
	 * invalid for the SearchResultCollector.
	 */
	public void handleReplyCode(int replyCode) throws InvalidReplyCodeException;

}
