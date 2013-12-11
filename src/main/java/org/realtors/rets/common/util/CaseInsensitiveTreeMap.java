package org.realtors.rets.common.util;

import java.util.Map;
import java.util.TreeMap;

public class CaseInsensitiveTreeMap<K, V> extends TreeMap<K, V> {
	public CaseInsensitiveTreeMap(Map<K, V> map) {
		this();
		this.putAll(map);
	}

	public CaseInsensitiveTreeMap() {
		super(new CaseInsensitiveComparator());
	}

}
