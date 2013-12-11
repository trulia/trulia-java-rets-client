package org.realtors.rets.client;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;

public class IOFailReader extends FilterReader {

	protected IOFailReader(Reader reader) {
		super(reader);
	}

	public void setFailRead(boolean failRead) {
		this.mFailRead = failRead;
	}

	@Override
	public int read() throws IOException {
		checkFailRead();
		return super.read();
	}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		checkFailRead();
		return super.read(cbuf, off, len);
	}

	private void checkFailRead() throws IOException {
		if (this.mFailRead)
			throw new IOException("Simulated IOException");
	}

	private boolean mFailRead;
}
