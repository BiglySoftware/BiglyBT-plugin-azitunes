package com.vuze.itunes.impl.osx.cocoa;

import com.vuze.itunes.*;
import com.vuze.itunes.impl.osx.cocoa.applescript.ASList;

/**
 *
 * @see com.vuze.itunes.impl.osx.cocoa.ITunesScripts
 * 
 * @author olemarchand
 */
public class ITunesPlaylistImpl extends ITunesObjectImpl implements ITunesPlaylist {
    private final ITunesPlaylistKind kind;

    /**
     *
     * @param fields
     * @param source
     * @return
     */
    /**
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
     * @param source the source of the playlist
     *
     * @return proper instance of {@link com.vuze.itunes.ITunesPlaylist}
     *
     * @see ITunesPlaylistImpl
     * @see ITunesLibraryPlaylistImpl
     */
    public static ITunesPlaylist build(ASList fields, ITunesSourceImpl source){
        if ( ITunesPlaylistKind.isLibrary(fields.getString( 4 ))){
            return new ITunesLibraryPlaylistImpl( source.getExecutor(), fields, source );
        }else{
            return new ITunesPlaylistImpl( source.getExecutor(), fields, source );
        }
    }

    public ITunesPlaylistImpl( AppleScriptExecutor appleScriptExecutor, String persistentId, long id, int index, String name, ITunesPlaylistKind kind, ITunesSource source ) {
        super( appleScriptExecutor, persistentId, id, index, name, source );
        this.kind = kind;
    }

    public ITunesPlaylistImpl( AppleScriptExecutor executor, ASList fields, ITunesSource source ) {
        this(executor,
              fields.getString( 0 ),
              fields.getLong( 1 ),
              fields.getInt( 2 ),
              fields.getString( 3 ),
              ITunesPlaylistKind.getITunesPlaylistKind( fields.getObjectTypeString( 4 ) ), 
              source );
    }

    public ITunesPlaylistKind getKind() {
        return kind;
    }

    public ITunesSource getSource() {
        return ( ITunesSource ) getContainer();
    }
}
