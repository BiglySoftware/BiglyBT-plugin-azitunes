package com.vuze.itunes.impl.windows.jacob;

import java.util.ArrayList;
import java.util.List;

import com.jacob.com.Dispatch;
import com.vuze.itunes.ITunesLibraryPlaylist;
import com.vuze.itunes.ITunesObject;
import com.vuze.itunes.ITunesPlaylist;
import com.vuze.itunes.ITunesPlaylistKind;
import com.vuze.itunes.ITunesSource;
import com.vuze.itunes.ITunesSourceKind;

public class ITunesSourceImpl extends ITunesObjectImpl implements ITunesSource {
	
	ITunesSourceImpl(Dispatch dispatch,ITunesObject container) {
		super(dispatch,container);
	}

	public ITunesSourceKind getKind() {
		return ITunesSourceKind.getITunesSourceKind(getInt("Kind"));
	}
	
	public long getCapacity() {
		return (long) getDouble("Capacity");
	}
	
	public long getFreeSpace() {
		return (long) getDouble("FreeSpace");
	}
	
	public ITunesLibraryPlaylist getLibraryPlaylist() {
		
		Dispatch collection = Dispatch.get(dispatch,"Playlists").toDispatch();
		
		int count = Dispatch.get(collection,"Count").getInt();
		
		List<ITunesLibraryPlaylist> playlists = new ArrayList<ITunesLibraryPlaylist>(count);
		
		for(int i = 1 ; i <= count ; i++) {
			
			Dispatch playlist = Dispatch.call(collection,"Item", new Long(i)).toDispatch();
			ITunesPlaylistImpl impl = new ITunesPlaylistImpl(playlist);
			if(impl.getKind() == ITunesPlaylistKind.ITunesPlaylistKindLibrary) {
				ITunesLibraryPlaylistImpl libraryImpl = new ITunesLibraryPlaylistImpl(playlist);
				playlists.add(libraryImpl);
			}
		
		}
		
		if(playlists.size() == 1) {
			return playlists.get(0);
		} else {
			return null;
		}
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof ITunesSourceImpl) {
			ITunesSourceImpl other = (ITunesSourceImpl) obj;
			if(other.getId() == this.getId()) return true;
		}
		
		return false;
		
	}
	
	public int hashCode() {
		return ("S." + getId()).hashCode();
	}
}
