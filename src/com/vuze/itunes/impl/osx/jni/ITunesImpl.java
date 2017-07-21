package com.vuze.itunes.impl.osx.jni;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.StyledEditorKit.ItalicAction;

import com.vuze.itunes.ITunes;
import com.vuze.itunes.ITunesCommunicationException;
import com.vuze.itunes.ITunesLibraryPlaylist;
import com.vuze.itunes.ITunesSource;
import com.vuze.itunes.ITunesSourceKind;

public class ITunesImpl implements ITunes {

	static int nbInstances;
	
	static {
		nbInstances = 0;
	}
	
	long iTunesApplication;
	
	
	public ITunesImpl() {
		try {
			if(nbInstances == 0) {
				ITunesScriptingBridgeCalls.initLib();
				nbInstances++;
			}
			iTunesApplication = ITunesScriptingBridgeCalls.getITunesApplication();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void finalize() throws Throwable {
		nbInstances--;
		if(nbInstances == 0) {
			ITunesScriptingBridgeCalls.unInitLib();
		}
		ITunesScriptingBridgeCalls.release(iTunesApplication);
	}
	
	public boolean isInstalled() {
		return iTunesApplication != 0;
	}
	
	public boolean isRunning() throws ITunesCommunicationException {
		try {
			return ITunesScriptingBridgeCalls.isRunning(iTunesApplication);
		} catch (ScriptingBridgeException e) {
			throw new ITunesCommunicationException(e);
		}
	}
	
	public ITunesLibraryPlaylist getLibraryPlaylist()
			throws ITunesCommunicationException {
		
		List<ITunesSource> sources = getSources();
		
		for(ITunesSource source : sources) {
			if(source.getKind() == ITunesSourceKind.ITunesSourceKindLibrary) {
				return source.getLibraryPlaylist();
			}
		}
		
		
		return null;
		
	}

	public List<ITunesSource> getSources() throws ITunesCommunicationException {
		
		try {
			long[] sourceRefs = ITunesScriptingBridgeCalls.getApplicationSources(iTunesApplication);
			
			List<ITunesSource> result = new ArrayList<ITunesSource>(sourceRefs.length);
			
			for(long sourceRef:sourceRefs) {
				ITunesSourceImpl source = new ITunesSourceImpl(sourceRef,this);
				result.add(source);
			}
			
			return result;
		} catch (ScriptingBridgeException e) {
			throw new ITunesCommunicationException(e);
		}
	}

	public void
	destroy()
	{
	}
}
