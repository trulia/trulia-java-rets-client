package org.realtors.rets.client;

public interface InvalidReplyCodeHandler {
	InvalidReplyCodeHandler FAIL = new InvalidReplyCodeHandler() {
		@Override
		public void invalidRetsReplyCode(int replyCode) throws InvalidReplyCodeException {
			throw new InvalidReplyCodeException(replyCode);
		}

		@Override
		public void invalidRetsStatusReplyCode(int replyCode) throws InvalidReplyCodeException {
			throw new InvalidReplyCodeException(replyCode);
		}
	};

	public void invalidRetsReplyCode(int replyCode) throws InvalidReplyCodeException;

	public void invalidRetsStatusReplyCode(int replyCode) throws InvalidReplyCodeException;
}
