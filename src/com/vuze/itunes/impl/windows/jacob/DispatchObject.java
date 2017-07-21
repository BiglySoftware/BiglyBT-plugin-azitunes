package com.vuze.itunes.impl.windows.jacob;

import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class DispatchObject {

	protected Dispatch dispatch;
	
	DispatchObject(Dispatch dispatch) {
		this.dispatch = dispatch;
	}
	
	protected boolean getBoolean(String name) {
		Variant value = Dispatch.get(dispatch,name);
		return value.getBoolean();
	}
	
	protected int getInt(String name) {
		Variant value = Dispatch.get(dispatch,name);
		return value.getInt();
	}
	
	protected long getLong(String name) {
		Variant value = Dispatch.get(dispatch,name);
		return value.getLong();
	}
	
	protected double getDouble(String name) {
		Variant value = Dispatch.get(dispatch,name);
		return value.getDouble();
	}
	
	protected String getString(String name) {
		Variant value = Dispatch.get(dispatch,name);
		return value.getString();
	}
}
