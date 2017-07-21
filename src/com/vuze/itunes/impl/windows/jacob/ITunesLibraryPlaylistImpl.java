package com.vuze.itunes.impl.windows.jacob;

import com.jacob.com.ComFailException;
import com.jacob.com.Dispatch;
import com.vuze.itunes.ITunesCommunicationException;
import com.vuze.itunes.ITunesLibraryPlaylist;
import com.vuze.itunes.ITunesPlaylistIsNotModifiableException;
import com.vuze.itunes.ITunesTrack;

public class ITunesLibraryPlaylistImpl extends ITunesPlaylistImpl implements ITunesLibraryPlaylist {
	
	public ITunesLibraryPlaylistImpl(Dispatch dispatch) {
		super(dispatch);
	}
	
	
	public ITunesTrack addFile(String filePath) throws ITunesCommunicationException,ITunesPlaylistIsNotModifiableException {
		try {
			
			Dispatch result = Dispatch.call(dispatch, "AddFile", filePath).toDispatch();
			
			if(result.m_pDispatch == 0) {
				throw new ITunesCommunicationException("Can't add file (possible bad transcode)");
			}
			
			ITunesOperationStatusImpl status = new ITunesOperationStatusImpl(result);
			
			while(status.inProgress()) {
				
				try {
					
					Thread.sleep(50);
					
				} catch (Exception e) {
					//Ignore
				}
				
			}
			
			Dispatch collection = Dispatch.get(result,"Tracks").toDispatch();
			
			int count = Dispatch.get(collection,"Count").getInt();
			
			if(count == 1) {
				
				Dispatch track = Dispatch.call(collection,"Item", new Long(1)).toDispatch();
				
				return new com.vuze.itunes.impl.windows.jacob.ITunesTrackImpl(track,this);
				
			} else {
				
				return null;
				
			}

			
		} catch(ComFailException e) {
			
			if( e.getMessage().contains("The playlist is not modifiable") ) {
				
				throw new ITunesPlaylistIsNotModifiableException(e);
				
			} else {
				
				throw new ITunesCommunicationException(e);
				
			}
		}
		
		
	}
	

}
