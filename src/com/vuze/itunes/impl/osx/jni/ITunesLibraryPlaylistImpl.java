package com.vuze.itunes.impl.osx.jni;

import com.vuze.itunes.ITunesCommunicationException;
import com.vuze.itunes.ITunesLibraryPlaylist;
import com.vuze.itunes.ITunesOperationStatus;
import com.vuze.itunes.ITunesPlaylistIsNotModifiableException;
import com.vuze.itunes.ITunesPlaylistKind;
import com.vuze.itunes.ITunesTrack;

public class ITunesLibraryPlaylistImpl extends ITunesObjectImpl implements ITunesLibraryPlaylist {

	public ITunesLibraryPlaylistImpl(long reference, ITunesImpl application) {
		super(reference,application);
	}
	
	public ITunesTrack addFile(String filePath)
			throws ITunesCommunicationException,
			ITunesPlaylistIsNotModifiableException {
		
		try {
			long trackRef = ITunesScriptingBridgeCalls.addTrack(application.iTunesApplication, filePath, reference);
			if(trackRef != 0) {
				return new ITunesTrackImpl(trackRef,application);
			}
			return null;
		} catch (Exception e) {
			throw new ITunesPlaylistIsNotModifiableException(e);
		}
	}

	public ITunesPlaylistKind getKind() throws ITunesCommunicationException {
		try {
			String kind = ITunesScriptingBridgeCalls.getStringFromType(ITunesScriptingBridgeCalls.getPlaylistSpecialKind(reference));
			
			ITunesPlaylistKind result = ITunesPlaylistKind.getITunesPlaylistKind(kind);
			
			return result;
		} catch (ScriptingBridgeException e) {
			throw new ITunesCommunicationException(e);
		}
	}

}
