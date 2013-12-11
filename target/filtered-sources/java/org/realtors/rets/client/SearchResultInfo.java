package org.realtors.rets.client;

/**
 * Interface that describes high level information
 * about the results of a search.
 * @author jrayburn
 */
public interface SearchResultInfo {
	public int getCount() throws RetsException;

	public String[] getColumns() throws RetsException;

	/** @throws IllegalStateException */
	public boolean isMaxrows() throws RetsException, IllegalStateException;

	/** 
	 * Indicates that processing of this search 
	 * is complete.
	 * 
	 * @return true if this SearchResultSet is finished processing.
	 * @throws RetsException Thrown if there is an error
	 * processing the SearchResultSet.
	 */
	public boolean isComplete() throws RetsException;
}
