package org.realtors.rets.client;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.realtors.rets.common.metadata.MetaCollector;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataType;

public abstract class MetaCollectorAdapter implements MetaCollector {

	
	public MetaObject[] getMetadata(MetadataType type, String path) {
		return getSome(type, path, "0");
	}

	
	public MetaObject[] getMetadataRecursive(MetadataType type, String path) {
		return getSome(type, path, "*");
	}

	private MetaObject[] getSome(MetadataType type, String path, String sfx) {
		boolean compact = Boolean.getBoolean("rets-client.metadata.compact");
		try {
			GetMetadataRequest req;
			if (path == null || path.equals("")) {
				req = new GetMetadataRequest(type.name(), sfx);
			} else {
				String[] ppath = StringUtils.split(path, ":");
				String[] id = new String[ppath.length + 1];
				System.arraycopy(ppath, 0, id, 0, ppath.length);
				id[ppath.length] = sfx;
				req = new GetMetadataRequest(type.name(), id);
			}
			if (compact) {
				req.setCompactFormat();
			}
			GetMetadataResponse response;

			response = doRequest(req);

			return response.getMetadata();
		} catch (RetsException e) {
			LOG.error("bad metadata request", e);
			return null;
		}
	}

	/**
	 * Perform operation of turning a GetMetadataRequest into 
	 * a GetMetadataResponse
	 * 
	 * @param req Requested metadata
	 * @return parsed MetaObjects 
	 * 
	 * @throws RetsException if an error occurs
	 */
	protected abstract GetMetadataResponse doRequest(GetMetadataRequest req) throws RetsException;

	private static final Log LOG = LogFactory.getLog(MetaCollectorAdapter.class);
}
