package org.realtors.rets.client;

import org.apache.commons.logging.LogFactory;


public interface CompactRowPolicy {
	
	/** fail fast and furiously */
	public static final CompactRowPolicy STRICT = new CompactRowPolicy(){
		
		public boolean apply(int row, String[] columns, String[] values) {
			if( values.length != columns.length )
				throw new IllegalArgumentException(String.format("Invalid number of result columns: got %s, expected %s",values.length, columns.length));
			return true;
		}};

	/** drop everything thats suspect */
	public static final CompactRowPolicy DROP = new CompactRowPolicy(){
		
		public boolean apply(int row, String[] columns, String[] values) {
			if (values.length != columns.length) {
				LogFactory.getLog(CompactRowPolicy.class).warn(String.format("Row %s: Invalid number of result columns:  got %s, expected ",row, values.length, columns.length));
				return false;
			}
			return true;
		}};
		
	/** fail fast on long rows */
	public static final CompactRowPolicy DEFAULT = new CompactRowPolicy(){
		
		public boolean apply(int row, String[] columns, String[] values) {
			if (values.length > columns.length) {
				throw new IllegalArgumentException(String.format("Invalid number of result columns: got %s, expected %s",values.length, columns.length));
			}
			if (values.length < columns.length) {
				LogFactory.getLog(CompactRowPolicy.class).warn(String.format("Row %s: Invalid number of result columns:  got %s, expected ",row, values.length, columns.length));
			}
			return true;
		}};
		
	/** drop and log long rows, try to keep short rows */
	public static final CompactRowPolicy DROP_LONG = new CompactRowPolicy(){
		
		public boolean apply(int row, String[] columns, String[] values) {
			if (values.length > columns.length) {
				LogFactory.getLog(CompactRowPolicy.class).warn(String.format("Row %s: Invalid number of result columns:  got %s, expected ",row, values.length, columns.length));
				return false;
			}
			if (values.length < columns.length) {
				LogFactory.getLog(CompactRowPolicy.class).warn(String.format("Row %s: Invalid number of result columns:  got %s, expected ",row, values.length, columns.length));
			}
			return true;
		}};
		
	public boolean apply(int row, String[] columns, String[] values);
}
