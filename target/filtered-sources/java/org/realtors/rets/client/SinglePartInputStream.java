package org.realtors.rets.client;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;

class SinglePartInputStream extends FilterInputStream {
	private static final int EOS = -1;
	
	private final String boundary;
	private boolean eos;

	
	SinglePartInputStream(PushbackInputStream partInput, String boundary) {
		super(partInput);
		this.boundary = boundary;
	}
	
	@Override
	public int read() throws IOException {
		int read = this.getPushBackStream().read();
		// was this the start of a boundary?
		if( read != '\r' && read != '\n' ) return read;
		this.getPushBackStream().unread(read);
		byte[] peek = new byte[ "\r\n".length() + this.boundary.length()];
		// if so, check and see if the rest of the boundary is next
		int peekRead = this.getPushBackStream().read(peek);
		this.getPushBackStream().unread(peek, 0, peekRead);
		if( new String(peek).contains(this.boundary) ) return EOS;
		// if not, just a coincidence, just return the byte
		return this.getPushBackStream().read();
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		if(this.eos) return EOS;
		
		int read = off;
		for( ; read < off + len; read++) {
			int nextByte = this.read();
			if(nextByte == EOS) {
				this.eos = true;
				break;
			}
			
			b[read] = (byte) nextByte;
		}
		return ( read - off );
	}
	
	@Override
	public int read(byte[] b) throws IOException {
		return this.read(b, 0, b.length);
	}
	
	@Override
	public void close() {
		// noop - part of a larger stream
	}
	
	private PushbackInputStream getPushBackStream() {
        return (PushbackInputStream) this.in;
    }
}