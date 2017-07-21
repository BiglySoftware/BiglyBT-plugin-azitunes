package com.vuze.itunes.impl.osx.cocoa;

import com.vuze.itunes.*;
import com.vuze.itunes.impl.osx.cocoa.applescript.ASList;
import com.vuze.itunes.impl.osx.cocoa.applescript.CocoaBridgeAppleScriptExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Simple implementation of the {@link com.vuze.itunes.ITunes} interface. it contains no cache and no logic,
 * just performs AS calls whenever a method is invoked
 * 
 * @see com.vuze.itunes.impl.osx.cocoa.ITunesScripts
 *
 * @author olemarchand
 * @author gudy
 */
public class ITunesImpl implements ITunes {
    private final AppleScriptExecutor executor;
    private final List<ITunesEventListener> listeners;

    public ITunesImpl( AppleScriptExecutor executor ) {
        this.executor = executor;
        listeners = new CopyOnWriteArrayList<ITunesEventListener>();
    }

    public ITunesLibraryPlaylist getLibraryPlaylist() throws ITunesCommunicationException {
        return getLibrarySource().getLibraryPlaylist();
    }

    private ITunesLibrarySourceImpl getLibrarySource() throws ITunesCommunicationException {
        try {
            final ASList values = ( ASList ) executor.execute( ITunesScripts.getLibrarySourceScript );

            return new ITunesLibrarySourceImpl( executor, values );
        } catch( AppleScriptException e ) {
            throw new ITunesCommunicationException( e.getMessage(), e );
        }
    }

    public List<ITunesSource> getSources() throws ITunesCommunicationException {
        try {
            final List<ITunesSource> sources = new ArrayList<ITunesSource>( );
            final ASList values = ( ASList ) executor.execute( ITunesScripts.getAllSourcesScript );

            for( ASValue value : values ) {
                sources.add( new ITunesSourceImpl( executor, (ASList)value ) );
            }

            return Collections.unmodifiableList( sources );
        } catch( AppleScriptException e ) {
            throw new ITunesCommunicationException( e.getMessage(), e );
        }
    }

    public void addListener( ITunesEventListener listener ) {
        listeners.add( listener );
    }

    public void removeListener( ITunesEventListener listener ) {
        listeners.remove( listener );
    }

    public static void main( String[] args ) throws ITunesCommunicationException {
        AppleScriptExecutor e = new AppleScriptExecutor() {
            private final AppleScriptExecutor e = new CocoaBridgeAppleScriptExecutor();

            public ASValue execute( Script script ) throws AppleScriptException {

                System.out.println( "====================[executing]===================");
                System.out.println( script.getSourceCode() );
                System.out.println( "==================================================" );

                long start = System.currentTimeMillis();
                final ASValue result = e.execute( script );
                long end = System.currentTimeMillis();
                long time = end-start;

                System.out.printf( "\t>>>>>\tscript took %d ms to execute\n", time );
                return result;
            }
        };
        ITunesImpl itunes = new ITunesImpl( e );
        final ITunesLibraryPlaylist tunesLibraryPlaylist = itunes.getLibraryPlaylist();
        System.out.println( "tunesLibraryPlaylist = " + tunesLibraryPlaylist );
        final List<ITunesSource> tunesSources = itunes.getSources();
        System.out.println( "tunesSources = " + tunesSources );
        for( ITunesSource tunesSource : tunesSources ) {
            System.out.println( "tunesSource.getId() = " + tunesSource.getId() );
            System.out.println( "tunesSource.getFreeSpace() = " + tunesSource.getFreeSpace() );
            System.out.println( "tunesSource.getCapacity() = " + tunesSource.getCapacity() );
            System.out.println( "tunesSource.getKind() = " + tunesSource.getKind() );
            System.out.println( "tunesSource.getLibraryPlaylist() = " + tunesSource.getLibraryPlaylist() );
        }
    }
    
    public boolean isInstalled() {
    	return true;
    }
    
    public boolean isRunning() {
    	//TODO :
//    	tell application "System Events"
//    		exists process "iTunes"
//    	end tell
    	
    	return false;
    }
    
    public void
    destroy()
    {
    }
}
