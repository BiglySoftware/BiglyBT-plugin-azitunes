package com.vuze.itunes;

public interface ITunesPlaylist extends ITunesObject {

	public ITunesPlaylistKind getKind() throws ITunesCommunicationException;
	
}
