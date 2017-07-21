package com.vuze.itunes.impl.windows.jacob;

import com.jacob.com.Dispatch;
import com.vuze.itunes.ITunesObject;

public class ITunesObjectImpl extends DispatchObject implements ITunesObject {
	
	long id = 0;
	
	int index;
	
	String name;
	
	ITunesObject container;

	ITunesObjectImpl(Dispatch dispatch,ITunesObject container) {
		super(dispatch);
		name = getString("Name");
		this.container = container;
	}
	
	public long getIndex() {
		return getLong("Index");
	}
	
	public ITunesObject getContainer() {
		return container;
	}
	
	public String getName() {
		return name;
	}
	
	public long getId() {
		return id;
	}
	
	public String toString() {
		return this.getClass().getName() + " : " + getName() + "(" + getId() + ")";
	}
	
}
