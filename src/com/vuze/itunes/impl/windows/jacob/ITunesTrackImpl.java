package com.vuze.itunes.impl.windows.jacob;

import com.vuze.itunes.ITunesTrack;

public class ITunesTrackImpl extends ITunesObjectImpl implements ITunesTrack {
	
	public ITunesTrackImpl(com.jacob.com.Dispatch dispatch,ITunesPlaylistImpl playlist) {
		super(dispatch,playlist);
	}

}
