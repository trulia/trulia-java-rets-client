/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: David Terrell
 * Copyright (c) 2003, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 * 
 *
 * Vangulo Changed:
 * gives ability to handle dates in this format
 * 2011-06-01T18:06:58
 * should find a more elegant way
 */
package org.realtors.rets.common.metadata.attrib;

//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;

import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.MetaParseException;

/**
 * Converted this class to return a String instead of a
 * Date object which allows for more flexiblity since
 * Many Rets Servers format their dates differently 
 * 
 * @author vangulo
 *
 */
public class AttrDate implements AttrType<String> {
	
	// need date attribute to be flexible since different MLS's have 
	// different formats for dates 
	public String parse(String value, boolean strict) throws MetaParseException {
		return value;
//		Date d;
//		try {
//			d = this.df.parse(value);
//		} catch (ParseException e) {
//			if( strict ) 
//				throw new MetaParseException(e);
//			try {
//				value = value.replaceAll("[A-Za-z]", " ");
//				d = this.df1.parse(value);
//			} catch (ParseException e1) {
//				//e1.printStackTrace();
//				return value;
//			}
//			return d;
//		}
//		return d;
	}

	public String render(String value) {
		return value;
		//Date date = value;
		//return this.df.format(date);
	}

	public Class<String> getType() {
		return String.class;
	}

	//private DateFormat df = new SimpleDateFormat("E, d MMM yyyy HH:mm:ss z");
	//2011-06-01T18:06:58
	//private DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//Tuesday, 22-Dec-2009 21:03:18 GMT
	//private DateFormat df2 = new SimpleDateFormat("E, dd-MMM-yyyy HH:mm:ss z");
}
