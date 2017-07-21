package com.vuze.itunes.impl.osx.jni;

import java.util.ArrayList;
import java.util.List;

import com.vuze.itunes.ITunesCommunicationException;
import com.vuze.itunes.ITunesLibraryPlaylist;
import com.vuze.itunes.ITunesObject;
import com.vuze.itunes.ITunesPlaylist;
import com.vuze.itunes.ITunesSource;
import com.vuze.itunes.ITunesSourceKind;

public class ITunesSourceImpl extends ITunesObjectImpl implements ITunesSource {

	public ITunesSourceImpl(long reference,ITunesImpl application) {
		super(reference,application);
	}
	
	public long getCapacity() throws ITunesCommunicationException  {
		try {
			return ITunesScriptingBridgeCalls.getLongProperty(reference, "capacity");
		} catch (ScriptingBridgeException e) {
			throw new ITunesCommunicationException(e);
		}
		
	}

	public long getFreeSpace() throws ITunesCommunicationException {
		try {
			return ITunesScriptingBridgeCalls.getLongProperty(reference, "freeSpace");
		} catch (ScriptingBridgeException e) {
			throw new ITunesCommunicationException(e);
		}
	}

	public ITunesSourceKind getKind() throws ITunesCommunicationException  {
		
		try {
			String kind = ITunesScriptingBridgeCalls.getStringFromType(
					ITunesScriptingBridgeCalls.getSourceKind(reference));
			
			return ITunesSourceKind.fromTypeString(kind);
		} catch (ScriptingBridgeException e) {
			throw new ITunesCommunicationException(e);
		}
		
	}

	public ITunesLibraryPlaylist getLibraryPlaylist()
			throws ITunesCommunicationException {
		
		try {
			
			long libraryPlaylist = ITunesScriptingBridgeCalls.getSourceLibraryPlaylist(reference);
			
			if(libraryPlaylist != 0) {
				return new ITunesLibraryPlaylistImpl(libraryPlaylist,application);
			}
			
			return null;
			
		} catch (ScriptingBridgeException e) {
			
			throw new ITunesCommunicationException(e);
			
		}
		
		
	}

}
