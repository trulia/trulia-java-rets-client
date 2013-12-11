package org.realtors.rets.client;

import java.io.InputStream;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * dbt is lame and hasn't overridden the default
 * javadoc string.
 */
public class ChangePasswordResponse {
	public ChangePasswordResponse(InputStream stream) throws RetsException {
		SAXBuilder builder = new SAXBuilder();
		Document document = null;
		try {
			document = builder.build(stream);
		} catch (Exception e) {
			throw new RetsException(e);
		}
		Element rets = document.getRootElement();
		if (!rets.getName().equals("RETS")) {
			throw new RetsException("Invalid Change Password Response");
		}

		int replyCode = Integer.parseInt(rets.getAttributeValue("ReplyCode"));
		if (replyCode != 0) {
			InvalidReplyCodeException exception;
			exception = new InvalidReplyCodeException(replyCode);
			exception.setRemoteMessage(rets.getAttributeValue("ReplyText"));
			throw exception;
		}
	}
}
