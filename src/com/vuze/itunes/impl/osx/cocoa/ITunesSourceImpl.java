package com.vuze.itunes.impl.osx.cocoa;

import com.vuze.itunes.*;
import com.vuze.itunes.impl.osx.cocoa.applescript.ASList;

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

/**
 * Simple implementation of the {@link com.vuze.itunes.ITunesSource} interface that simply performs an AS script
 * execution whenever asked for its playlists
 *
 * @author olemarchand
 */
public class ITunesSourceImpl extends ITunesObjectImpl implements ITunesSource{
    private final ITunesSourceKind kind;
    private final long capacity;
    private final long freeSpace;

    public ITunesSourceImpl( AppleScriptExecutor appleScriptExecutor, String persistentId, long id, int index, String name, ITunesSourceKind kind, long capacity, long freeSpace, ITunesObject container ) {
        super( appleScriptExecutor,persistentId, id, index, name, container );
        this.capacity = capacity;
        this.freeSpace = freeSpace;
        this.kind = kind;
    }

    public ITunesSourceImpl( AppleScriptExecutor appleScriptExecutor, String persistentId, long id, int index, String name, ITunesSourceKind kind, long capacity, long freeSpace ) {
        this(appleScriptExecutor, persistentId, id, index, name, kind, capacity, freeSpace,null);
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
    public ITunesSourceImpl( AppleScriptExecutor appleScriptExecutor, ASList fields){
        this(appleScriptExecutor,
              fields.getString( 0 ),
              fields.getLong( 1 ),
              fields.getInt( 2 ),
              fields.getString( 3 ),
              ITunesSourceKind.fromTypeString( fields.getString( 4 ) ),
              fields.getLong( 5 ),
              fields.getLong( 6 ));
    }

    public long getCapacity() {
        return capacity;
    }

    public long getFreeSpace() {
        return freeSpace;
    }

    public ITunesSourceKind getKind() {
        return kind;
    }

    /**
     * TODO somehow the script returns very fast in script editor but does not return here ????
     */
    public ITunesLibraryPlaylist getLibraryPlaylist() throws ITunesCommunicationException {
        try {
            final List<ITunesPlaylist> playlists = new ArrayList<ITunesPlaylist>();

            final ASList values = ( ASList ) execute( ITunesScripts.getLibraryPlaylistScript( this ) );

            return new ITunesLibraryPlaylistImpl( this.getExecutor(), values, this );
          
        } catch( AppleScriptException e ) {
            throw new ITunesCommunicationException( e.getMessage(), e );
        }
    }
}
