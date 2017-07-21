package com.vuze.itunes;

public interface ITunesObject {
	
	public long getId() throws ITunesCommunicationException;
	
	public long getIndex() throws ITunesCommunicationException;
	
	public String getName() throws ITunesCommunicationException;

}
