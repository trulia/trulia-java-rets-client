package org.realtors.rets.client;

import java.util.NoSuchElementException;
import java.util.Iterator;

/**
 * Interface for retrieving additional information from of a result from a RETS query/search
 *
 */

public interface SearchResult extends SearchResultInfo {
	public String[] getRow(int idx) throws NoSuchElementException;

	public Iterator iterator();

	@Override
	public String[] getColumns();

	@Override
	public boolean isMaxrows();

	@Override
	public int getCount();

	@Override
	public boolean isComplete();
}
