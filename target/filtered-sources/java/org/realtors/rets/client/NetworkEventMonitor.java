package org.realtors.rets.client;

/**
 * A client can register a monitor for network events
 */
public interface NetworkEventMonitor
{
    /**
     * inform the client app that an event has started.
     * the client app can return an object, which will be passed
     * to eventFinish().
     *
     * @param message a message describing the event
     * @return an object to be passed to eventFinish, or null
     */
    public Object eventStart(String message);
    /**
     * Inform the client app that the previous event has completed
     *
     * @param o the object returned from eventStart
     */
    public void eventFinish(Object o);
}
