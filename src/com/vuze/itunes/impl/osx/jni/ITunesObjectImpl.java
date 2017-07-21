package com.vuze.itunes.impl.osx.jni;

import com.vuze.itunes.ITunesCommunicationException;
import com.vuze.itunes.ITunesObject;

public class ITunesObjectImpl implements ITunesObject {
	
	long reference;
	ITunesImpl application;
	
	ITunesObjectImpl(long reference,ITunesImpl application) {
		this.reference = reference;
		this.application = application;
	}
	
	public long getId()  throws ITunesCommunicationException {
		try {
			return ITunesScriptingBridgeCalls.getIntProperty(reference, "id");
		} catch (ScriptingBridgeException e) {
			throw new ITunesCommunicationException(e);
		}
	}
	
	public long getIndex()  throws ITunesCommunicationException {
		try {
			return ITunesScriptingBridgeCalls.getIntProperty(reference, "index");
		} catch (ScriptingBridgeException e) {
			throw new ITunesCommunicationException(e);
		}
	}
	
	public String getName()  throws ITunesCommunicationException {
		try {
			return ITunesScriptingBridgeCalls.getStringProperty(reference, "name");
		} catch (ScriptingBridgeException e) {
			throw new ITunesCommunicationException(e);
		}
	}
	
	protected void finalize() throws Throwable {
		ITunesScriptingBridgeCalls.release(reference);
	}
	

}
