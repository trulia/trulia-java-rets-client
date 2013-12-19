package org.realtors.rets.client;

import java.io.InputStream;
import java.io.Reader;
import java.util.LinkedList;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.InputSource;

/**
 * SearchResultProcessor that returns a streaming SearchResult implementation.
 * 
 * @author jrayburn
 */
public class StreamingSearchResultProcessor implements SearchResultProcessor {
	private final int mBufferSize;
	private final int mTimeout;
	private InvalidReplyCodeHandler mInvalidReplyCodeHandler;
	private CompactRowPolicy mCompactRowPolicy;

	/**
	 * Construct a StreamingSearchResultProcessor.
	 * 
	 * Waits indefinitely for buffer to be read from by 
	 * client.
	 * 
	 * @param bufferSize 
	 * 	How many rows to buffer
	 */
	public StreamingSearchResultProcessor(int bufferSize) {
		this(bufferSize, 0);
	}

	/**
	 * Construct a StreamingSearchResultProcessor.
	 * 
	 * Waits <code>timeout</code> milliseconds for buffer to 
	 * be read from by client.
	 * 
	 * @param bufferSize 
	 * 	How many rows to buffer
	 * 
	 * @param timeout 
	 * 	How long to wait, in milliseconds, for the buffer 
	 * 	to be read from when full. 0 indicates an indefinite
	 * 	wait.
	 */
	public StreamingSearchResultProcessor(int bufferSize, int timeout) {
		super();
		this.mBufferSize = bufferSize;
		this.mTimeout = timeout;
	}

	/** how to deal with badly delimited data */
	public void setCompactRowPolicy(CompactRowPolicy badRowPolicy) {
		this.mCompactRowPolicy = badRowPolicy;
	}

	private CompactRowPolicy getCompactRowPolicy() {
		if (this.mCompactRowPolicy == null)
			return CompactRowPolicy.DEFAULT;
		return this.mCompactRowPolicy;
	}

	public void setInvalidRelyCodeHandler(InvalidReplyCodeHandler invalidReplyCodeHandler) {
		this.mInvalidReplyCodeHandler = invalidReplyCodeHandler;
	}

	private InvalidReplyCodeHandler getInvalidRelyCodeHandler() {
		if (this.mInvalidReplyCodeHandler == null)
			return InvalidReplyCodeHandler.FAIL;
		return this.mInvalidReplyCodeHandler;
	}

	public SearchResultSet parse(InputStream reader) {
		return parse(new InputSource(reader));
	}

	public SearchResultSet parse(Reader reader) {
		return parse(new InputSource(reader));
	}

	public SearchResultSet parse(InputSource source) {
		StreamingSearchResult result = new StreamingSearchResult(this.mBufferSize, this.mTimeout);
		StreamingThread thread = new StreamingThread(source, result, this.getInvalidRelyCodeHandler(), this.getCompactRowPolicy());
		thread.start();
		return result;
	}

}

class StreamingThread extends Thread {
	private StreamingSearchResult mResult;
	private InputSource mSource;
	private InvalidReplyCodeHandler mInvalidReplyCodeHandler;
	private CompactRowPolicy badRowPolicy;

	public StreamingThread(InputSource source, StreamingSearchResult result,InvalidReplyCodeHandler invalidReplyCodeHandler, CompactRowPolicy badRowPolicy) {
		this.mSource = source;
		this.mResult = result;
		this.mInvalidReplyCodeHandler = invalidReplyCodeHandler;
		this.badRowPolicy = badRowPolicy;
	}

	@Override
	public void run() {
		SearchResultHandler handler = new SearchResultHandler(this.mResult, this.mInvalidReplyCodeHandler, this.badRowPolicy);
		try {
			handler.parse(this.mSource);
		} catch (RetsException e) {
			this.mResult.setException(e);
		} catch (Exception e) {
			// socket timeouts, etc while obtaining xml bytes from InputSource ...
			this.mResult.setException(new RetsException("Low level exception while attempting to parse input from source.", e));
		}
	}

}

class StreamingSearchResult implements SearchResultSet, SearchResultCollector {

	private static final int PREPROCESS = 0;
	private static final int BUFFER_AVAILABLE = 1;
	private static final int BUFFER_FULL = 2;
	private static final int COMPLETE = 3;

	private final int timeout;
	private final int bufferSize;
	private final LinkedList<String[]> buffer;

	private boolean mMaxrows;
	private int state;
	private String[] columns;
	private int count;
	private RetsException exception;

	public StreamingSearchResult(int bufferSize, int timeout) {
		if (bufferSize < 1)
			throw new IllegalArgumentException("[bufferSize=" + bufferSize + "] must be greater than zero");
		if (timeout < 0)
			throw new IllegalArgumentException("[timeout=" + timeout + "] must be greater than or equal to zero");

		this.bufferSize = bufferSize;
		this.timeout = timeout;
		this.state = PREPROCESS;
		this.buffer = new LinkedList<String[]>();
		this.count = -1;
		this.columns = null;
		this.exception = null;
	}

	// ------------ Producer Methods

	public synchronized boolean addRow(String[] row) {
		if (row.length > this.columns.length) {
			throw new IllegalArgumentException(String.format("Invalid number of result columns: got %s, expected %s",row.length, this.columns.length));
		}
		if (row.length < this.columns.length) {
			LogFactory.getLog(SearchResultCollector.class).warn(String.format("Row %s: Invalid number of result columns:  got %s, expected ",this.count, row.length, this.columns.length));
		}

		if (state() > BUFFER_FULL) {
			if (this.exception == null)
				setException(new RetsException("Attempting to add rows to buffer when in complete state"));
			throw new NestableRuntimeException(this.exception);
		}

		// check complete.
		while (checkRuntime() && state() == BUFFER_FULL) {
			_wait();

			if (state() >= BUFFER_FULL) {
				if (this.exception == null)
					setException(new RetsException("Timeout writing to streaming result set buffer, timeout length = "
							+ this.timeout));
				throw new NestableRuntimeException(this.exception);
			}
		}

		this.buffer.addLast(row);

		if (this.bufferSize == this.buffer.size())
			pushState(BUFFER_FULL);
		else
			pushState(BUFFER_AVAILABLE);

		this.notifyAll();
		return true;
	}

	public synchronized void setComplete() {
		pushState(COMPLETE);
		notifyAll();
	}

	public synchronized void setCount(int count) {
		this.count = count;
		pushState(PREPROCESS);
		notifyAll();
	}

	public synchronized void setColumns(String[] columns) {
		this.columns = columns;
		pushState(BUFFER_AVAILABLE);
		notifyAll();
	}

	public synchronized void setMaxrows() {
		this.mMaxrows = true;
		pushState(COMPLETE);
		notifyAll();
	}

	synchronized void setException(RetsException e) {
		this.exception = e;
		pushState(COMPLETE);
		notifyAll();
	}

	// ----------- Consumer Methods

	public synchronized boolean hasNext() throws RetsException {
		// wait for someone to add data to the queue
		// or flag complete
		while (checkException() && state() < COMPLETE) {
			if (!this.buffer.isEmpty())
				return true;

			_wait();
		}

		return !this.buffer.isEmpty();
	}

	public synchronized String[] next() throws RetsException {
		checkException();
		String[] row = this.buffer.removeFirst();
		if (this.state < COMPLETE)
			pushState(BUFFER_AVAILABLE);
		this.notifyAll();
		return row;
	}

	public synchronized int getCount() throws RetsException {
		while (checkException() && state() < BUFFER_AVAILABLE) {
			_wait();
		}
		return this.count;
	}

	public synchronized String[] getColumns() throws RetsException {
		while (checkException() && state() < BUFFER_AVAILABLE) {
			_wait();
		}
		return this.columns;
	}

	public synchronized boolean isMaxrows() throws RetsException {
		checkException();

		if (!isComplete())
			throw new IllegalStateException("Cannot call isMaxRows until isComplete == true");

		return this.mMaxrows;
	}

	public synchronized SearchResultInfo getInfo() throws RetsException {
		checkException();

		if (!isComplete())
			throw new IllegalStateException("Cannot call isMaxRows until isComplete == true");

		return this;
	}

	public synchronized boolean isComplete() throws RetsException {
		checkException();
		return state() >= COMPLETE;
	}

	private synchronized boolean checkRuntime() {
		try {
			return checkException();
		} catch (RetsException e) {
			throw new NestableRuntimeException(e);
		}
	}

	private synchronized boolean checkException() throws RetsException {
		// considering doing something here to maintain the original
		// stack trace but also provide the stack trace from this
		// location...
		if (this.exception != null)
			throw this.exception;
		return true;
	}

	private void _wait() {
		try {
			wait(this.timeout);
		} catch (InterruptedException e) {
			pushState(COMPLETE);
			throw new NestableRuntimeException(e);
		}
	}

	private void pushState(int newState) {
		if (this.state >= COMPLETE && newState < COMPLETE)
			throw new IllegalStateException("Cannot revert from complete state");

		if (this.state > PREPROCESS && newState <= PREPROCESS)
			throw new IllegalStateException("Cannot revert to preprocess state");

		if (newState < this.state && newState != BUFFER_AVAILABLE && this.state != BUFFER_FULL)
			throw new IllegalStateException("Cannot go back in state unless reverting to buffer available from full");

		this.state = newState;
	}

	private int state() {
		return this.state;
	}

}
