/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: David Terrell
 * Copyright (c) 2003, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * dbt is lame and hasn't overridden the default
 * javadoc string.
 */
public class BrokerCodeRequredException extends RetsException {
    private final List mCodeList;

    public BrokerCodeRequredException(Collection codes) {
        this.mCodeList = Collections.unmodifiableList(new ArrayList(codes));
    }

    public List getCodeList(){
        return this.mCodeList;
    }

}
