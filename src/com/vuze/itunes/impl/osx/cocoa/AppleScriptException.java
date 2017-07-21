package com.vuze.itunes.impl.osx.cocoa;

import com.apple.cocoa.foundation.*;

/**
 * generic exception thrown by {@link AppleScriptExecutor}s when something wrong happens.
 * Causes can be: unable to find itunes, time out encountered during execution, bad script, etc.
 *
 * @author olemarchand
 */
public class AppleScriptException extends Exception{
    public static final String APP_NAME = "NSAppleScriptErrorAppName";
    public static final String ERROR_CODE = "NSAppleScriptErrorNumber";
    public static final String BRIEF_MESSAGE = "NSAppleScriptErrorBriefMessage";
    public static final String MESSAGE = "NSAppleScriptErrorMessage";
    public static final String ERROR_RANGE = "NSAppleScriptErrorRange";

    private final String application;
    private final String briefMessage;
    private final int errorCode;
    private final String script;
    private final int location;
    private final int length;

    public AppleScriptException( NSDictionary dictionary, String script ) {
        super( ( String ) dictionary.objectForKey( MESSAGE ) );
        this.application = ( String ) dictionary.objectForKey( "NSAppleScriptErrorAppName" );
        this.briefMessage = ( String ) dictionary.objectForKey( BRIEF_MESSAGE );
        this.errorCode = (Integer)dictionary.objectForKey( ERROR_CODE );
        NSRange range = ( NSRange ) dictionary.objectForKey( ERROR_RANGE );
        this.location = range.location();
        this.length = range.length();
        this.script = script;
        
    }

    public String getApplication() {
        return application;
    }

    public String getBriefMessage() {
        return briefMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getScript() {
        return script;
    }

    public int getLength() {
        return length;
    }

    public int getLocation() {
        return location;
    }
}
