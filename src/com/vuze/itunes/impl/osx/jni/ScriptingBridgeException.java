package com.vuze.itunes.impl.osx.jni;

public class ScriptingBridgeException extends Exception {
	
	public
	ScriptingBridgeException(
		String		operation,
		String		message )
	{
		super(operation + ":" + message);
	}
	
	public
	ScriptingBridgeException(
		String		message )
	{
		super(message);
	}
	
}
