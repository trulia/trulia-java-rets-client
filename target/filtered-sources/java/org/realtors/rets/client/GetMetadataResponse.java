package org.realtors.rets.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.JDomCompactBuilder;
import org.realtors.rets.common.metadata.MetadataException;
import org.realtors.rets.common.metadata.JDomStandardBuilder;

public class GetMetadataResponse {
	private MetaObject[] mMetadataObjs;

	public GetMetadataResponse(InputStream stream, boolean compact, boolean isStrict) throws RetsException {
		try {
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(stream);
			Element retsElement = document.getRootElement();
			if (!retsElement.getName().equals("RETS")) {
				throw new RetsException("Expecting RETS");
			}
			int replyCode = NumberUtils.toInt(retsElement.getAttributeValue("ReplyCode"));
			if (ReplyCode.SUCCESS.equals(replyCode)) {
				if (compact) {
					handleCompactMetadata(document, isStrict);
				} else {
					handleStandardMetadata(document, isStrict);
				}
			} else if (ReplyCode.NO_METADATA_FOUND.equals(replyCode)) {
				// No metadata is not an exceptional case
				handleNoMetadataFound(retsElement);
			} else {
				InvalidReplyCodeException e = new InvalidReplyCodeException(replyCode);
				e.setRemoteMessage(retsElement.getAttributeValue(retsElement.getAttributeValue("ReplyText")));
				throw e;
			}
		} catch (JDOMException e) {
			throw new RetsException(e);
		} catch (IOException e) {
			throw new RetsException(e);
		}
	}

	private void handleNoMetadataFound(Element retsElement) throws RetsException {
		List children = retsElement.getChildren();
		if (children.size() != 0) {
			throw new RetsException("Expecting 0 children when results");
		}
		this.mMetadataObjs = new MetaObject[0];
	}

	private void handleCompactMetadata(Document document, boolean isStrict) throws RetsException {
		try {
			JDomCompactBuilder builder = new JDomCompactBuilder();
			builder.setStrict(isStrict);
			this.mMetadataObjs = builder.parse(document);
		} catch (MetadataException e) {
			throw new RetsException(e);
		}
	}

	private void handleStandardMetadata(Document document, boolean isStrict) throws RetsException {
		try {
			JDomStandardBuilder builder = new JDomStandardBuilder();
			builder.setStrict(isStrict);
			this.mMetadataObjs = builder.parse(document);
		} catch (MetadataException e) {
			throw new RetsException(e);
		}
	}

	public MetaObject[] getMetadata() {
		return this.mMetadataObjs;
	}

}
