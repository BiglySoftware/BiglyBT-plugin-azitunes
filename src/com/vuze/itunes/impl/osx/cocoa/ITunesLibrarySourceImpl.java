package com.vuze.itunes.impl.osx.cocoa;

import com.vuze.itunes.ITunesSourceKind;
import com.vuze.itunes.ITunesLibraryPlaylist;
import com.vuze.itunes.ITunesCommunicationException;
import com.vuze.itunes.impl.osx.cocoa.applescript.ASList;

/**
 * specialization of {@link ITunesSourceImpl} for a LibrarySource
 *
 * @see com.vuze.itunes.impl.osx.cocoa.ITunesScripts
 * 
 * @author olemarchand
 */
public class ITunesLibrarySourceImpl extends ITunesSourceImpl {

    public ITunesLibrarySourceImpl( AppleScriptExecutor appleScriptExecutor, String persistentId, long id, int index, String name, ITunesSourceKind kind, long capacity, long freeSpace ) {
        super( appleScriptExecutor, persistentId, id, index, name, kind, capacity, freeSpace );
    }



    /**
     * @param appleScriptExecutor an {@link com.vuze.itunes.impl.osx.cocoa.AppleScriptExecutor}
     * @param fields an {@link ASList} representing a source with the following values:
     * <ul>
     *  <li>persistent ID as an {@link com.vuze.itunes.impl.osx.cocoa.applescript.ASString}</li>
     *  <li>id as an {@link com.vuze.itunes.impl.osx.cocoa.applescript.ASInteger}</li>
     *  <li>index as an {@link com.vuze.itunes.impl.osx.cocoa.applescript.ASInteger}</li>
     *  <li>name as an {@link com.vuze.itunes.impl.osx.cocoa.applescript.ASString}</li>
     *  <li>kind as an {@link com.vuze.itunes.impl.osx.cocoa.applescript.ASString}</li>
     *  <li>capacity as an {@link com.vuze.itunes.impl.osx.cocoa.applescript.ASInteger}</li>
     *  <li>free space as an {@link com.vuze.itunes.impl.osx.cocoa.applescript.ASInteger}</li>
     * </ul>
     */
    public ITunesLibrarySourceImpl( AppleScriptExecutor appleScriptExecutor, ASList fields ) {
        super( appleScriptExecutor, fields );
    }

    public ITunesLibraryPlaylist getLibraryPlaylist() throws ITunesCommunicationException {
        try {
            return new ITunesLibraryPlaylistImpl( getExecutor(), (ASList)execute( ITunesScripts.getLibraryPlaylistScript ), this );
        } catch( AppleScriptException e ) {
            throw new ITunesCommunicationException( e.getMessage(), e );
        }
    }
}
