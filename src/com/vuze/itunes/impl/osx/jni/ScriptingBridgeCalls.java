package com.vuze.itunes.impl.osx.jni;

public class ScriptingBridgeCalls {

	static {
		System.loadLibrary("ScriptingBridgeCalls");
	}
	
	//Top level
	//public static final native void initLib();
	//public static final native void unInitLib();
	
	public static final void initLib() {}
	public static final void unInitLib() {}
	
	public static final native long listenTo(String listenerClass,String name);
	public static final native long stopListener(long listener);
	
	public static final native long getSBApplication(String name);
	
	//Generic selectors
	public static final native long[]	getReferenceArrayProperty(long object,String property) throws ScriptingBridgeException;
	public static final native long		getReferenceProperty(long object,String property) throws ScriptingBridgeException;
	public static final native long		getLongProperty(long object,String property) throws ScriptingBridgeException;
	public static final native boolean	getBooleanProperty(long object,String property) throws ScriptingBridgeException;
	public static final native int		getTypeProperty(long object,String property) throws ScriptingBridgeException;
	public static final native int		getIntProperty(long object,String property) throws ScriptingBridgeException;
	public static final native String	getStringProperty(long object,String property) throws ScriptingBridgeException;
	
	public static final native void		callVoidMethod(long object,String method) throws ScriptingBridgeException;
	public static final native void		callVoidMethod(long object,String method,long value) throws ScriptingBridgeException;
	public static final native void		callVoidMethod(long object,String method,long value1,long value2) throws ScriptingBridgeException;
	
	public static final native long		callRefMethod(long object,String method) throws ScriptingBridgeException;
	public static final native long		callRefMethod(long object,String method,long value) throws ScriptingBridgeException;
	public static final native long		callRefMethod(long object,String method,long value1,long value2) throws ScriptingBridgeException;
	
	public static final native String	getNSObjectClass(long object) throws ScriptingBridgeException;
	
	//Methods for creating NSObjects
	public static final native long		createNSString(String s) throws ScriptingBridgeException;
	public static final native long		createNSURLFromPath(long nsString) throws ScriptingBridgeException;
	public static final native long		createNSMutableArray() throws ScriptingBridgeException;
	
	//Release code
	public static final native void		release(long object) throws ScriptingBridgeException;
	
	public static void notificationReceived(long notification)  throws ScriptingBridgeException {
		System.out.println("===> blah received : " + notification);
		System.out.println("===> name : " + getStringProperty(notification, "name"));
	}
	
	
}
