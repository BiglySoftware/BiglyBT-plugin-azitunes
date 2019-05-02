package com.vuze.itunes.impl.windows.jacob;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.biglybt.core.util.Constants;
import com.biglybt.core.util.Debug;
import com.biglybt.platform.win32.access.AEWin32Access;
import com.biglybt.platform.win32.access.impl.AEWin32AccessImpl;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComFailException;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.LibraryLoader;
import com.vuze.itunes.ITunes;
import com.vuze.itunes.ITunesCommunicationException;
import com.vuze.itunes.ITunesEventListener;
import com.vuze.itunes.ITunesLibraryPlaylist;
import com.vuze.itunes.ITunesPlugin;
import com.vuze.itunes.ITunesSource;
import com.vuze.processes.ProcessAccess;

public class ITunesImpl extends DispatchObject implements ITunes {
	
	private static boolean	tried_reg_fix = false;
	
	
	private ITunesPlugin		plugin;
	
	//DispatchEvents events;
	
	List<ITunesEventListener> listeners;
	
	List<ITunesSource> sources;
	
	private boolean	com_init_done;
	
	private boolean isInstalled;
	
	public ITunesImpl( ITunesPlugin _plugin, boolean init_com ) {
		//start not initialized
		super(null);

		plugin	= _plugin;
		
		String dir_name = plugin.getPluginInterface().getPluginDirectoryName();
		
		System.setProperty( "bigly.processaccess.library.path", dir_name ); 
				
		String jacob_dll_name = dir_name + File.separator + LibraryLoader.getPreferredDLLName() + ".dll";
		
		System.setProperty( "jacob.dll.path", jacob_dll_name );
		
		isInstalled = false;
		
		if ( init_com ){
			//events = new DispatchEvents(dispatch,this);
			
			try {
				ComThread.InitMTA();
				
				com_init_done = true;
				
				
					
			} catch (Throwable t) {
				
				log( "Com initialisation failed", t );
			}
		}
		
	}
	
	private synchronized boolean isInitialized() {
		return (dispatch != null);
	}
	
	private synchronized void initialize() throws ComFailException {
		ActiveXComponent iTunesCom;
		
		try{
			iTunesCom = new ActiveXComponent("iTunes.Application");
			
		}catch( ComFailException e ){
			
			String	msg = e.getMessage();
			
			if ( msg != null && msg.equals( "Can't get object clsid from progid" )){
				
				if ( !tried_reg_fix ){
					
					tried_reg_fix = true;
					
					tryRegFix();
					
					iTunesCom = new ActiveXComponent("iTunes.Application");
				}
			}
			
			throw( e );
		}
		
		dispatch = iTunesCom.getObject();
	}
	
	protected void
	tryRegFix()
	{		
		if ( !tryRegFix( AEWin32Access.HKEY_CLASSES_ROOT, "" )){
			
			tryRegFix( AEWin32Access.HKEY_CURRENT_USER, "Software\\Classes\\" );
		}
	}
	
	protected boolean
	tryRegFix(
		int		reg_type,
		String	initial_prefix )
	{		
		try{
			
			AEWin32Access win32 = AEWin32AccessImpl.getSingleton( true );
			
			try{
				String temp = win32.readStringValue( reg_type, initial_prefix + "iTunes.Application", "" );
				
				if ( temp != null && temp.length() > 0 ){
					
						// seems already registered, bail
					
					return( true );
				}
			}catch( Throwable e ){
			}
			
			log( "iTunes appears to be incorrectly installed, trying to fix" );

			String	itunes_exe = null;
			
			try{
					// base on iTunesDetector location
					// no initial-prefix here as we expect the detector to be under HCR
				
				String temp = win32.readStringValue( reg_type, "CLSID\\{D719897A-B07A-4C0C-AEA9-9B663A28DFCB}\\InprocServer32", "" );
				
				int pos = temp.lastIndexOf( "\\" );
				
				if ( pos >= 0 ){
					
					temp = temp.substring( 0, pos+1 ) + "iTunes.exe";
					
					if ( new File( temp ).exists()){
						
						itunes_exe = temp;
					}
				}
			}catch( Throwable e ){
			}
			
			if ( itunes_exe == null ){
				
					// try basing on prog dir
				
				try{
					String temp = win32.getProgramFilesDir() + "\\iTunes\\iTunes.exe";
					
					if ( new File( temp ).exists()){
						
						itunes_exe = temp;
					}
				}catch( Throwable e ){
				}
			}
			
			if ( itunes_exe == null ){
				
				log( "Can't locate iTunes.exe, abandoning" );
				
				return( false );
			}
			
			log( "iTunes.exe located: " + itunes_exe );
					
			String	prefix = initial_prefix;
			
			win32.writeStringValue( reg_type, prefix + "iTunes.Application", "", "iTunes Class" );
			win32.writeStringValue( reg_type, prefix + "iTunes.Application\\CLSID", "", "{DC0C2640-1415-4644-875C-6F4D769839BA}" );
			win32.writeStringValue( reg_type, prefix + "iTunes.Application\\CurVer", "", "iTunes.Application.1" );
				
			win32.writeStringValue( reg_type, prefix + "iTunes.Application.1", "", "iTunes Class" );
			win32.writeStringValue( reg_type, prefix + "iTunes.Application.1\\CLSID", "", "{DC0C2640-1415-4644-875C-6F4D769839BA}" );
			
			String[]	locs = { "", "Wow6432Node\\" };
			
			for ( String s: locs ){
				
				prefix = initial_prefix + s + "CLSID\\{DC0C2640-1415-4644-875C-6F4D769839BA}";
				
				win32.writeStringValue( reg_type, prefix, "", "iTunes Class" );
				win32.writeStringValue( reg_type, prefix, "AppId", "{F98206B5-F052-4965-9FA0-85F61BC3C19D}" );
				win32.writeStringValue( reg_type, prefix + "\\LocalServer32", "", "\"" + itunes_exe + "\"" );
				win32.writeStringValue( reg_type, prefix + "\\ProgID", "", "iTunes.Application.1" );
				win32.writeStringValue( reg_type, prefix + "\\TypeLib", "", "{9E93C96F-CF0D-43f6-8BA8-B807A3370712}" );
				win32.writeStringValue( reg_type, prefix + "\\VersionIndependentProgID", "", "iTunes.Application" );
			
				prefix = initial_prefix + s + "AppID\\{FE9E4896-A014-11D1-855C-00A0C944138C}";
	
				win32.writeStringValue( reg_type, prefix, "", "TlntSvr" );
				win32.writeStringValue( reg_type, prefix, "ServiceParameters", "-Service -From_DCOM" );
				win32.writeStringValue( reg_type, prefix, "LocalService", "TlntSvr" );
	
				prefix = initial_prefix + s + "TypeLib\\{9E93C96F-CF0D-43F6-8BA8-B807A3370712}\\1.b";
	
				win32.writeStringValue( reg_type, prefix, "", "iTunes 1.11 Type Library" );
				win32.writeStringValue( reg_type, prefix + "\\0\\win32", "", itunes_exe );
				win32.writeStringValue( reg_type, prefix + "\\FLAGS", "", "0" );
				win32.writeStringValue( reg_type, prefix + "\\HELPDIR", "", "" );
			}
			
			plugin.restartRequired();
			
			return( true );
			
		}catch( Throwable e ){
			
			log( "Failed to fix iTunes installation", e );
			
			return( false );
		}
	}
	
	private static boolean tried_itd_reg;
	private static boolean logged_itd_error;
	
	public boolean 
	isInstalled() 
	
		throws ITunesCommunicationException 
	{
		try{			
			return( isInstalledSupport());
			
		}catch( ITunesCommunicationException e ){
			
			if ( tryITDReg()){
				
				return( isInstalledSupport());
			}
				
			throw( e );
		}
	}
	
	private boolean
	tryITDReg()
	{
		if ( !tried_itd_reg ){
			
			tried_itd_reg = true;
			
			try{
					// hack to see if we can reg it
				
				File ocx = new File( "C:\\Program Files (x86)\\iTunes\\ITDetector.ocx" );
			
				if ( !ocx.exists()){
					
					ocx = new File( "C:\\Program Files\\iTunes\\ITDetector.ocx" );
				}
				
				if ( ocx.exists()){
					
						// apparently regsvr32 handles both 32 and 64 bit registration magically as there are two versions of
						// it, one in windows\system32 and one in windows\syswow64
					
					Process process = 
						Runtime.getRuntime().exec(
							new String[]{
								"regsvr32",
								"/s",
								"\"" + ocx.getAbsolutePath() + "\"",
							});
						        
					process.waitFor();
				
					return( true );
				}
			}catch( Throwable f ){
			}
		}
		
		return( false );
	}
	
	private boolean isInstalledSupport() throws ITunesCommunicationException {
		try {
			DispatchObject iTunesDetector = new DispatchObject(new ActiveXComponent("ITDetector.iTunesDetector").getObject());
		
			boolean isInstalled = iTunesDetector.getBoolean("IsiTunesAvailable");
			
			return isInstalled;
			
		} catch (ComFailException cfe) {
			// ITDetector isn't 64bit.. assume true
			
			tryITDReg();	// might as well give it a go
			
			if ( !Constants.is64Bit ){
				
				if ( !logged_itd_error ){
				
					logged_itd_error = true;
					
					Debug.out(cfe.getMessage());
				}
			}
			return true;
		} catch (Throwable t) {
			throw new ITunesCommunicationException(t);
		}
	}
	
	public boolean isRunning() throws ITunesCommunicationException {
		String[] processes = ProcessAccess.getProcessList();
		for(String process:processes) {
			
			if ( process != null && process.equals("iTunes.exe")) {
				
				return true;
			}
		}
		
		return false;
	}
	
	public void
	destroy()
	{
		if ( com_init_done ){
		
			ComThread.Release();
			
			com_init_done = false;
		}
	}
	
	public List<ITunesSource> getSources() {
		
		if(! isInitialized()) {
			initialize();
		}
		
		Dispatch collection = Dispatch.get(dispatch,"Sources").toDispatch();
		
		int count = Dispatch.get(collection,"Count").getInt();
		
		List<ITunesSource> sources = new ArrayList<ITunesSource>(count);
		
		for(int i = 1 ; i <= count ; i++) {
			
			Dispatch source = Dispatch.call(collection,"Item",new Long(i)).toDispatch();
			
			sources.add(new ITunesSourceImpl(source,null));
			
		}
		
		return sources;
	}
	
	public ITunesLibraryPlaylist getLibraryPlaylist() {
		
		if(! isInitialized()) {
			initialize();
		}
		
		return new ITunesLibraryPlaylistImpl(Dispatch.get(dispatch,"LibraryPlaylist").toDispatch());
	}
	
	protected void
	log(
		String		str )
	{
		if ( plugin == null ){
			
			System.out.println( str );
			
		}else{
			
			plugin.log( str );
		}
	}
	
	protected void
	log(
		String		str,
		Throwable	e )
	{
		if ( plugin == null ){
			
			System.out.println( str );
			
			e.printStackTrace();
			
		}else{
			
			plugin.log( str, e );
		}
	}
	
//	public ITunesSource getITunesSourceById(long sourceId) {
//		Dispatch object = 
//			Dispatch.call(
//					dispatch,
//					"GetITObjectByID",
//					new Long(sourceId),
//					new Long(0),
//					new Long(0),
//					new Long(0)
//				).toDispatch();
//		
//		return new ITunesSourceImpl(object,null);
//	}
	
//	public ITunesObject getITunesObjectById(long sourceId, long playlistId,
//			long trackId, long databaseId) {
//		ITunesObject result;
//		Dispatch object = 
//				Dispatch.call(
//						dispatch,
//						"GetITObjectByID",
//						new Long(sourceId),
//						new Long(playlistId),
//						new Long(trackId),
//						new Long(databaseId)
//					).toDispatch();
//		
//		if(trackId == 0 && databaseId == 0) {
//			if(playlistId == 0) {
//				//it is a source
//				result = new ITunesSourceImpl(object,null);
//			} else {
//				//it's a playlist
//				ITunesPlaylist playlist = new ITunesPlaylistImpl(object);
//				if(playlist.getKind() == ITunesPlaylistKind.ITunesPlaylistKindLibrary) {
//					playlist = new ITunesLibraryPlaylistImpl(object);
//				}
//				result = playlist;
//			}
//		} else {
//			//it's a track
//			result = new ITunesObjectImpl(object);
//		}
//		
//		return result;
//		
//	}
	
//	public void addListener(ITunesEventListener listener) {
//		synchronized (listeners) {
//			listeners.add(listener);
//		}
//	}
//
//	public void removeListener(ITunesEventListener listener) {
//		synchronized (listeners) {
//			while(listeners.contains(listener)) {
//				listeners.remove(listener);
//			}
//		}
//	}
	
//	public void OnDatabaseChangedEvent(Variant[] args) {
//		SafeArray deletedIds = args[0].toSafeArray(false);
//		for(int i = deletedIds.getLBound() ; i < deletedIds.getUBound() ; i++) {
//			int sourceId = deletedIds.getInt(i, 0);
//			int playlistId = deletedIds.getInt(i, 1);
//			int trackId = deletedIds.getInt(i, 2);
//			int databaseId = deletedIds.getInt(i, 3);
//			
//			if(playlistId == 0 && trackId == 0 && databaseId == 0) {
//				//A source has been disconnected
//				synchronized (listeners) {
//					boolean removed = false;
//					Iterator<ITunesSource> iter = sources.iterator();
//					while(iter.hasNext()) {
//						ITunesSource source = iter.next();
//						if(source.getId() == sourceId) {
//							iter.remove();
//							removed = true;
//						}
//					}
//					if(removed) {
//						for(ITunesEventListener listener : listeners) {
//							listener.sourceDisconnected(sourceId);
//						}
//					}
//				}
//			}
//		}
//		
//		SafeArray modifiedIds = args[1].toSafeArray(false);
//		for(int i = modifiedIds.getLBound() ; i < modifiedIds.getUBound() ; i++) {
//			int sourceId = modifiedIds.getInt(i, 0);
//			int playlistId = modifiedIds.getInt(i, 1);
//			int trackId = modifiedIds.getInt(i, 2);
//			int databaseId = modifiedIds.getInt(i, 3);
//			
//			if(trackId == 0 && playlistId == 0 && databaseId == 0) {
//				ITunesSource source  = getITunesSourceById(sourceId);
//				if(!sources.contains(source)) {
//					sources.add(source);
//					synchronized (listeners) {
//						for(ITunesEventListener listener : listeners) {
//							listener.sourceConnected(source);
//						}
//					}
//				}
//			}
//		}
//
//	}
	
	public static void
	main(
		String[]	args )
	{
		new ITunesImpl( null, false ).tryRegFix();
	}
}
