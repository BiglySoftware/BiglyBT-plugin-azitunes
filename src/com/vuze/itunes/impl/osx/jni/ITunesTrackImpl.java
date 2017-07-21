package com.vuze.itunes.impl.osx.jni;

import com.vuze.itunes.ITunesTrack;

public class ITunesTrackImpl extends ITunesObjectImpl implements ITunesTrack {

	public ITunesTrackImpl(long reference,ITunesImpl application) {
		super(reference,application);
	}
}
