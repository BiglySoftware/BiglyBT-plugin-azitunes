package com.vuze.itunes;

import java.util.List;

public interface ITunesSource extends ITunesObject {
	
	public ITunesSourceKind getKind() throws ITunesCommunicationException;
	
	public long getCapacity() throws ITunesCommunicationException;
	
	public long getFreeSpace() throws ITunesCommunicationException;
	
	public ITunesLibraryPlaylist getLibraryPlaylist() throws ITunesCommunicationException;
	
}
