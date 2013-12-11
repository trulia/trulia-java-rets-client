package org.realtors.rets.client;

public class NullNetworkEventMonitor implements NetworkEventMonitor {
	@Override
	public Object eventStart(String message) {
		return null;
	}

	@Override
	public void eventFinish(Object o) {
		//noop
	}
}
