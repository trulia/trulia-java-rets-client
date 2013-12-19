package org.realtors.rets.client;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class CollectionOfCollectionsIterator implements Iterator {
    private Iterator mOuter;
    private Iterator mInner;

    public CollectionOfCollectionsIterator(Collection c) {
        this.mOuter = c.iterator();
        hasNext();
    }

    public boolean hasNext() {
        if( this.mInner != null && this.mInner.hasNext() ) {
            return true;
        }
        while( this.mOuter.hasNext() ){
            this.mInner = ((Collection) this.mOuter.next()).iterator();
            if( this.mInner.hasNext() ){
                return true;
            }
        }
        return false;
    }

    public Object next() {
        if ( this.hasNext() ) 
            return this.mInner.next();

        throw new NoSuchElementException();
    }

    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

}
