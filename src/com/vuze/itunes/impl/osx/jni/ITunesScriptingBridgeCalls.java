package com.vuze.itunes.impl.osx.jni;

import com.vuze.itunes.ITunesCommunicationException;

public class ITunesScriptingBridgeCalls extends ScriptingBridgeCalls {

	public static final long getITunesApplication() {
		return getSBApplication("com.apple.iTunes");
	}
	
	public static final void			addObjectToArray(long array,long object) throws ScriptingBridgeException {
		callVoidMethod(array,"addObject:",object);
	}

	
	//Application object
	public static final long[] getApplicationSources(long application)  throws ScriptingBridgeException {
		return getReferenceArrayProperty(application, "sources");
	}
	
	public static final boolean isRunning(long application)  throws ScriptingBridgeException{
		return getBooleanProperty(application,"isRunning");

	}
	
	public static final long addTrack(long application,String file,long playlist)  throws ScriptingBridgeException {
		
		//so, we need to convert the path slightly ...
		//eg : /test/toto.mp4 > Machintosh HD:test/toto.mp4
		
		long array = createNSMutableArray();
		long nsFile = createNSString(file);
		long nsUrl = createNSURLFromPath(nsFile);
		release(nsFile);
		addObjectToArray(array, nsUrl);
		long result = callRefMethod(application, "add:to:",array,playlist);
		release(array);
		release(nsUrl);
		return result;
	}
	
	//Item object
	public static final String getItemName(long item) throws ScriptingBridgeException {
		return getStringProperty(item, "name");
	}
	
	//Source object
	public static final long[] getSourcePlaylists(long source)  throws ScriptingBridgeException{
		return getReferenceArrayProperty(source, "playlists");
	}
	
	public static final long[] getSourceLibraryPlaylists(long source)  throws ScriptingBridgeException{
		return getReferenceArrayProperty(source, "libraryPlaylists");
	}
	
	public static final long getSourceLibraryPlaylist(long source)  throws ScriptingBridgeException{
		long[] libraryPlaylists = getSourceLibraryPlaylists(source);
		if(libraryPlaylists.length == 1) {
			return libraryPlaylists[0];
		}
		
		return 0;
	}
	
	public static final int getSourceKind(long source)  throws ScriptingBridgeException{
		return getIntProperty(source, "kind");
	}
	
	public static final long getSourceCapacity(long source)  throws ScriptingBridgeException{
		return getLongProperty(source, "capacity");
	}
	
	public static final long getSourceFreeSpace(long source) throws ScriptingBridgeException {
		return getLongProperty(source,"freeSpace");
	}
	
	//Playlist object
	public static final int getPlaylistSpecialKind(long playlist) throws ScriptingBridgeException {
		return getIntProperty(playlist, "specialKind");
	}
	
	/**
     * Utility method to get a type descriptor name from its code
     * @param typeCode the code of the type
     * @return the name of the type
     */
    public static String getStringFromType( int typeCode ) {
        byte[] bytes = new byte[4];
        for( int i = 0; i < 4 ; i++ ) {
            bytes[3 - i] = ( byte ) ( typeCode & 0x000000FF );
            typeCode = typeCode / 256;
        }

        return new String( bytes );
    }

    /**
     * Utility method to get a type descriptor code from its name
     * @param name the name of the type
     * @return the typeCode of the type
     */
    public static int getIntCodeFromString( String name ) {
        int n = 0;
        byte[] bytes = name.getBytes();
        for( byte aByte : bytes ) {
            int b = 0x000000FF & ( int ) aByte;
            n = n * 256 + b;
        }

        return n;

    }
    
    public static void notificationReceived(long notification)  throws ScriptingBridgeException{
		System.out.println("===> Notification received from : " + getStringProperty(notification, "name"));
		long object = getReferenceProperty(notification, "object");
		System.out.println("===> obj : " + object + ", "  + getStringProperty(notification, "description"));
	}
	
	public static void main(String[] args) throws Exception {
		initLib();
		long[] listeners = new long[3];
		listeners[0] = listenTo("com/vuze/itunes/impl/osx/jni/ITunesScriptingBridgeCalls","CMDeviceRegisteredNotification");
		listeners[1] = listenTo("com/vuze/itunes/impl/osx/jni/ITunesScriptingBridgeCalls","CMDeviceUnregisteredNotification");
		listeners[2] = listenTo("com/vuze/itunes/impl/osx/jni/ITunesScriptingBridgeCalls","CMDeviceProfilesNotification");
		
		long application = getITunesApplication();
		System.out.println("iTunes : " + application);
		System.out.println("isRunning : " + isRunning(application) );
		
		long[] sources = getApplicationSources(application);
		for(int i = 0 ; i < sources.length ; i++) {
			System.out.println("\tsource : " + sources[i] + ", name : " + getItemName(sources[i]) + ", kind : " + getStringFromType(getSourceKind(sources[i])));
			System.out.println("\tcapacity : " + getSourceCapacity(sources[i]) + ", free space : " + getSourceFreeSpace(sources[i]) );
	
			long[] playlists = getSourcePlaylists(sources[i]);
			for(int j = 0 ; j < playlists.length ; j++) {
				System.out.println("\t\tplaylist : " + playlists[j] + ", name : " + getItemName(playlists[j]) + ", kind : " + getStringFromType(getPlaylistSpecialKind(playlists[j])) + ", class : " + getNSObjectClass(playlists[j]) );
			}
			
			playlists = getSourceLibraryPlaylists(sources[i]);
			for(int j = 0 ; j < playlists.length ; j++) {
				System.out.println("\t\tlibrary playlist : " + playlists[j] + ", name : " + getItemName(playlists[j]) + ", kind : " + getStringFromType(getPlaylistSpecialKind(playlists[j])) + ", class : " + getNSObjectClass(playlists[j]) );
				String file = "Macintosh HD:star_trek_vga.mp4";
				System.out.println("\t\tAdding file : " + file);
				
				try {
					long track = addTrack(application, file, playlists[j]);
					System.out.println("\t\tAdded : " + getItemName(track) );
				} catch(Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		
		Thread.sleep(60 * 1000);
		
		System.out.println("***stopping***");
		
		stopListener(listeners[0]);
		stopListener(listeners[1]);
		stopListener(listeners[2]);
		
		unInitLib();
	}
	
}
