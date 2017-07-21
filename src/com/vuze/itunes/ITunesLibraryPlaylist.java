package com.vuze.itunes;

public interface ITunesLibraryPlaylist extends ITunesPlaylist {
	
	public ITunesTrack addFile(String filePath) throws ITunesCommunicationException,ITunesPlaylistIsNotModifiableException;

}
