package com.vuze.itunes.impl.osx.cocoa;

import com.vuze.itunes.*;
import com.vuze.itunes.impl.osx.cocoa.applescript.ASInteger;
import com.vuze.itunes.impl.osx.cocoa.applescript.ASList;
import com.vuze.itunes.impl.osx.cocoa.applescript.ASString;
import com.vuze.itunes.impl.osx.cocoa.applescript.Scripts;

/**
 * implementation of the {@link com.vuze.itunes.ITunesLibraryPlaylist} interface
 *
 * @see com.vuze.itunes.impl.osx.cocoa.ITunesScripts
 *
 * @author olemarchand
 */
public class ITunesLibraryPlaylistImpl extends ITunesPlaylistImpl implements ITunesLibraryPlaylist{

    public ITunesLibraryPlaylistImpl( AppleScriptExecutor appleScriptExecutor,
                                      String persistentId,
                                      long id,
                                      int index,
                                      String name,
                                      ITunesPlaylistKind kind,
                                      ITunesSource source ) {
        super( appleScriptExecutor, persistentId, id, index, name, kind, source );
    }

    public ITunesLibraryPlaylistImpl( AppleScriptExecutor executor, ASList fields, ITunesSource source ) {
        super(executor,  fields, source );
    }

    public ITunesTrack addFile( String filePath ) throws ITunesPlaylistIsNotModifiableException {
        StringBuffer sb = new StringBuffer();
        sb.append("set p to \"");
        sb.append(filePath);
        sb.append("\"\n");
        sb.append("set i to ");
        sb.append(getId());
        sb.append("\n");
        sb.append("set f to POSIX file p\n"+
        			"tell application \"iTunes\"\n" +
        			"set l to library playlist id i\n" +
        			"set t to add {f}\n" +
        			"{persistent ID, id, index, name} of t\n" +
        			"end tell");
        try {
        	ASList result = (ASList) execute( Scripts.fromSource(sb.toString()));
        	ASString persistentId = (ASString) result.get(0);
        	ASInteger id = (ASInteger) result.get(1);
        	ASInteger index = (ASInteger) result.get(2);
        	ASString name = (ASString) result.get(3);
        	
        	ITunesTrackImpl track = new ITunesTrackImpl(getExecutor(),persistentId.getValue(),index.getValue(),id.getValue(),name.getValue(),this);
        	return track;
        } catch (Exception e) {
			throw new ITunesPlaylistIsNotModifiableException(e);
		}
    }
}
