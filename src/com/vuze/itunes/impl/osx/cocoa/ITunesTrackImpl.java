package com.vuze.itunes.impl.osx.cocoa;

import com.vuze.itunes.ITunesObject;
import com.vuze.itunes.ITunesTrack;

public class ITunesTrackImpl extends ITunesObjectImpl implements ITunesTrack {

	public ITunesTrackImpl(AppleScriptExecutor appleScriptExecutor,
			String persistentId, long id, int index, String name,
			ITunesObject container) {
		super(appleScriptExecutor, persistentId, id, index, name, container);
	}

	public ITunesTrackImpl(AppleScriptExecutor appleScriptExecutor,
			String persistentId, long id, int index, String name) {
		super(appleScriptExecutor, persistentId, id, index, name);
	}

	
}
