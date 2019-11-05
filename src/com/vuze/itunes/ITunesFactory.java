package com.vuze.itunes;

import java.util.List;
import java.util.StringTokenizer;

import com.vuze.itunes.impl.osx.cocoa.ASValue;
import com.vuze.itunes.impl.osx.cocoa.AppleScriptException;
import com.vuze.itunes.impl.osx.cocoa.AppleScriptExecutor;
import com.vuze.itunes.impl.osx.cocoa.Script;
import com.vuze.itunes.impl.osx.cocoa.applescript.CocoaBridgeAppleScriptExecutor;
import com.vuze.itunes.impl.osx.jni.ITunesImpl;

public class ITunesFactory {
	
	public static ITunes getITunesApplication( ITunesPlugin plugin) throws UnsupportedOperationException {
		String  osName = System.getProperty("os.name").toLowerCase();
		if(osName.startsWith("mac os")) {
			String osVersion = System.getProperty("os.version");
			StringTokenizer st = new StringTokenizer(osVersion,".");
			if(st.countTokens() >= 2) {
				try {
					int version = Integer.parseInt(st.nextToken());
					int majorVersion = Integer.parseInt(st.nextToken());
					//int minorVersion = Integer.parseInt(st.nextToken());
					if(version == 10) {
						if(majorVersion >= 5) {
							return new ITunesImpl();
						}
						AppleScriptExecutor e = new AppleScriptExecutor() {
				            private final AppleScriptExecutor e = new CocoaBridgeAppleScriptExecutor();

				            public ASValue execute( Script script ) throws AppleScriptException {
				            	
				                final ASValue result = e.execute( script );

				                return result;
				            }
				        };
				        return new com.vuze.itunes.impl.osx.cocoa.ITunesImpl( e );
					}
				} catch (Throwable t) {
					throw new UnsupportedOperationException("error while instantiating the itunes library",t);
				}
			}
			throw new UnsupportedOperationException("os version not supported : " + osVersion);
			
		} else if(osName.startsWith("windows")) {
			try {
				return new com.vuze.itunes.impl.windows.jacob.ITunesImpl( plugin, true );
			} catch (Exception e) {
				throw new UnsupportedOperationException("failed to load the windows iTunes library",e);
			}
		}else{
			return( new com.vuze.itunes.impl.linux.ITunesImpl());
		}
	}
	
	public static void main(String[] args) {
		try {
			int i = 0;
			
			while(true) {
				i++;
				if(i % 1000 == 0) {
					System.out.println(i + " iterations");
				}
				ITunes iTunes = ITunesFactory.getITunesApplication( null );
				long startTime;
				ITunesLibraryPlaylist library;
				
				/*
				iTunes.isInstalled();
				if(iTunes.isRunning()) {
					while(true) {
						Thread.sleep(1000);
						List<ITunesSource> i_sources = iTunes.getSources();
						for(ITunesSource source : i_sources) {
							long id = source.getId();
							String name = source.getName();
							System.out.println(id + " : " + name);
						}
					}
				}*/
				
				
				
				//iTunes.destroy();
				
				
				startTime = System.currentTimeMillis();
				int nbIterations = 1;
				for(int j = 0 ; j < nbIterations ; j++) {
					iTunes.isRunning();
				}
				System.out.println("Took " + (System.currentTimeMillis() - startTime) + " ms to call isRunning() " + nbIterations + " times.");
			
				
				iTunes.getSources();
				startTime = System.currentTimeMillis();
				for(int j = 0 ; j < nbIterations ; j++) {
					iTunes.getSources();
				}
				
				System.out.println("Took " + (System.currentTimeMillis() - startTime) + " ms to call getSources() " + nbIterations + " times.");
				
				
				
				library = iTunes.getLibraryPlaylist();
				startTime = System.currentTimeMillis();
				for(int j = 0 ; j < nbIterations ; j++) {
					library.getName();
				}
			
				System.out.println("Took " + (System.currentTimeMillis() - startTime) + " ms to call getName() " + nbIterations + " times.");
			
				library = iTunes.getLibraryPlaylist();
				startTime = System.currentTimeMillis();
				for(int j = 0 ; j < 10 ; j++) {
					library.addFile("/Users/olivier/Library/Application Support/Azureus/Documents/Azureus Downloads/transcodes/iTunes/Nickelback - Far Away.mp4");
				}
			
				System.out.println("Took " + (System.currentTimeMillis() - startTime) + " ms to call addFile() " + 10 + " times.");
			
				
	//			Thread.sleep(10);
				//System.gc();
				
			}
	
			
			//Get the list of sources :
	//		List<ITunesSource> sources = iTunes.getSources();
	//		for(ITunesSource source : sources) {
	//			System.out.println("Found source : " + source.getName());
	//			ITunesLibraryPlaylist playlist = source.getLibraryPlaylist();
	//			if(playlist != null) {
	//				System.out.println("\thas a library playlist : " + playlist.getName());
	//			}
	//		}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
