package com.vuze.itunes.impl.windows.jacob;

import com.jacob.com.Dispatch;
import com.vuze.itunes.ITunesPlaylist;
import com.vuze.itunes.ITunesPlaylistKind;
import com.vuze.itunes.ITunesSource;

public class ITunesPlaylistImpl extends ITunesObjectImpl implements ITunesPlaylist {
	
	public ITunesPlaylistImpl(Dispatch dispatch) {
		this(dispatch,null);
		Dispatch source = Dispatch.get(dispatch,"Source").toDispatch();
		this.container = new ITunesSourceImpl(source,null);
	}
	
	public ITunesPlaylistImpl(Dispatch dispatch,ITunesSource container) {
		super(dispatch,container);
	}
	
	public ITunesPlaylistKind getKind() {
		return ITunesPlaylistKind.getITunesPlaylistKind(getInt("Kind"));
	}
	
	public ITunesSource getSource() {
		return (ITunesSource) container;
		
	}

}
