package com.vuze.itunes;

import java.util.List;

public interface ITunesEventListener {

	public void sourceConnected(ITunesSource source);
	
	public void sourceDisconnected(int sourceId);
}
