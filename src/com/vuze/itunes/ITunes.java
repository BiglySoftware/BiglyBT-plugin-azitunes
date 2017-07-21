package com.vuze.itunes;

import java.util.List;

public interface ITunes {
	
	public List<ITunesSource> getSources() throws ITunesCommunicationException;
	
	public ITunesLibraryPlaylist getLibraryPlaylist() throws ITunesCommunicationException;
	
	public boolean isInstalled() throws ITunesCommunicationException;
	
	public boolean isRunning() throws ITunesCommunicationException;
	
	public void destroy();
	
	// Gudy : didn't find a reliable way to detect an iPod being plugged in on OSX, let's disable this for now.
//	public void addListener(ITunesEventListener listener);
//	
//	public void removeListener(ITunesEventListener listener);

}
