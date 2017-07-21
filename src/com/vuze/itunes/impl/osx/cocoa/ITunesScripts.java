package com.vuze.itunes.impl.osx.cocoa;

import static com.vuze.itunes.impl.osx.cocoa.applescript.ScriptBuilder.tellApplication;
import com.vuze.itunes.impl.osx.cocoa.applescript.Scripts;
import com.vuze.itunes.impl.osx.cocoa.applescript.CocoaBridgeAppleScriptExecutor;

/**
 * Utility class to hold the AppleScript scripts used in various objects
 *
 * @see ITunesImpl
 * @see ITunesSourceImpl
 * @see ITunesLibrarySourceImpl
 * @see ITunesPlaylistImpl
 * @see ITunesLibraryPlaylistImpl
 *
 * @author olemarchand
 */
public final class ITunesScripts {
    public static final String ITUNES = "iTunes";
    public static final int DEFAULT_APPLESCRIPT_TIMEOUT = 10; //10 seconds timeout to execute an AS script


    public static final String getSources =
            "tell application \"iTunes\"\n" +
            "	set s to a reference to sources\n" +
            "	set result to {}\n" +
            "	set resultRef to a reference to result\n" +
            "	repeat with aSource in s\n" +
            "		set sourceDef to ({index, persistent ID, id, name, kind, capacity, free space} of aSource)\n" +
            "		log sourceDef\n" +
            "		copy sourceDef to the end of resultRef\n" +
            "	end repeat\n" +
            "	resultRef\n" +
            "end tell";

    public static final String getAllSources =
            tellApplication( ITUNES, DEFAULT_APPLESCRIPT_TIMEOUT ).forEach( "sources" )
                    .extract( "persistent ID", "id", "index", "name", "kind", "capacity", "free space" )
                    .toScriptSource();

    public static final String getLibrarySource =
            tellApplication( ITUNES, DEFAULT_APPLESCRIPT_TIMEOUT )
                    .returnResult( "{persistent ID,id,index,name,kind,capacity,free space} of source 1" )
                    .toScriptSource();

    public static final String getLibraryPlaylist =
            tellApplication( ITUNES, DEFAULT_APPLESCRIPT_TIMEOUT )
                    .returnResult( "{persistent ID, id, index, name, class} of library playlist 1" )
                    .toScriptSource();

    public static final String getAllPlaylists =
            tellApplication( ITUNES ).forEach( "playlists of source id %d" )
                    .extract( "persistent ID","id","index","name","class")
                    .toScriptSource();

    public static final Script getAllSourcesScript = Scripts.fromSource( getAllSources );
    public static final Script getLibrarySourceScript = Scripts.fromSource( getLibrarySource );
    public static final Script getLibraryPlaylistScript = Scripts.fromSource( getLibraryPlaylist );
    
    public static Script getAllPlaylistsScript(ITunesSourceImpl source ) {
        return Scripts.fromTemplate( getAllPlaylists, source.getId() );
    }
    
    public static Script getLibraryPlaylistScript(ITunesSourceImpl source ) {
        return Scripts.fromTemplate( getLibraryPlaylist, source.getId() );
    }
    

    private ITunesScripts() throws IllegalAccessException {
        throw new IllegalAccessException("this class should not be instanciated");
    }
}
