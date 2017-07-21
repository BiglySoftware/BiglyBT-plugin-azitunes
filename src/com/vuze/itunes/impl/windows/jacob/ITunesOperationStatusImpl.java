package com.vuze.itunes.impl.windows.jacob;

import com.jacob.com.Dispatch;
import com.vuze.itunes.ITunesOperationStatus;
import com.vuze.itunes.ITunesTrack;

public class ITunesOperationStatusImpl extends DispatchObject implements ITunesOperationStatus {
	
	public ITunesOperationStatusImpl(Dispatch dispatch) {
		super(dispatch);
	}
	
	public boolean inProgress() {
		return getBoolean("InProgress");
	}
	
	public ITunesTrack getTrack() {
		return null;
	}

}
