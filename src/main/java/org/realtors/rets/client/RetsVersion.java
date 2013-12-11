package org.realtors.rets.client;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

public class RetsVersion implements Serializable {
	public static final String RETS_VERSION_HEADER = "RETS-Version";

	public static final RetsVersion RETS_10 = new RetsVersion(1, 0, 0, 0);
	public static final RetsVersion RETS_15 = new RetsVersion(1, 5, 0, 0);
	public static final RetsVersion RETS_16 = new RetsVersion(1, 6, 0, 0);
	public static final RetsVersion RETS_17 = new RetsVersion(1, 7, 0, 0);
	public static final RetsVersion RETS_1_7_2 = new RetsVersion(1, 7, 2, 0);
	public static final RetsVersion DEFAULT = RETS_1_7_2;

	private int mMajor;
	private int mMinor;
	private int mRevision;
	private int mDraft;

	public RetsVersion(int major, int minor) {
		this(major,minor,0,0);
	}
	
	/** @deprecated use <code>new RetsVersion(major, minor, 0, draft)</code> */
	@Deprecated
	public RetsVersion(int major, int minor, int draft) {
		this(major,minor,0,draft);
	}
	
	public RetsVersion(int major, int minor, int revision, int draft) {
		this.mMajor = major;
		this.mMinor = minor;
		this.mRevision = revision;
		this.mDraft = draft;
	}

	public int getMajor() {
		return this.mMajor;
	}

	public int getMinor() {
		return this.mMinor;
	}

	public int getRevision() {
		return this.mRevision;
	}
	
	public int getDraft() {
		return this.mDraft;
	}

	@Override
	public String toString() {
		if (this.mRevision == 0) {
			if (this.mDraft == 0) {
				return "RETS/" + this.mMajor + "." + this.mMinor;
			}
			return "RETS/" + this.mMajor + "." + this.mMinor + "d" + this.mDraft;
		}
		if (this.mDraft == 0) {
			return "RETS/" + this.mMajor + "." + this.mMinor + "." + this.mRevision;
		}
		return "RETS/" + this.mMajor + "." + this.mMinor + "." + this.mRevision + "d" + this.mDraft;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof RetsVersion) {
			RetsVersion v = (RetsVersion) o;
			if ((v.getMajor() == this.mMajor) && (v.getMinor() == this.mMinor) && (v.getRevision() == this.mRevision) && (v.getDraft() == this.mDraft)) {
				return true;
			}
		}
		return false;
	}

	public static RetsVersion getVersion(String ver) {
		if( StringUtils.isEmpty(ver) ) return null;
		String[] split = StringUtils.trimToEmpty(ver).split("\\.");
		int ma = NumberUtils.toInt(split[0],1);
		int mn = split.length > 1 ? NumberUtils.toInt(split[1],0) : 0;
		int re = 0;
		int dr = 0;
		if (split.length > 2) {
			split = StringUtils.defaultString(split[2]).split("d");
			re = NumberUtils.toInt(split[0],0);
			dr = split.length > 1 ? NumberUtils.toInt(split[1],0) : 0;
		}
		return new RetsVersion(ma,mn,re,dr);
	}
	
}
