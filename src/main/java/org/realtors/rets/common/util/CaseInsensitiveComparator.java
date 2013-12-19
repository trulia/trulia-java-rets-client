package org.realtors.rets.common.util;

import java.io.Serializable;
import java.util.Comparator;

public class CaseInsensitiveComparator<T> implements Comparator<T>, Serializable {
	public int compare(Object o1, Object o2) {
		String s1 = (String) o1;
		String s2 = (String) o2;
		return s1.compareToIgnoreCase(s2);
	}
}
