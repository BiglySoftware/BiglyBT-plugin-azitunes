package com.vuze.itunes.impl.osx.cocoa;

/**
 * abstract class to define AppleScript aware objects.
 * Each subclass will have access to an {@link AppleScriptExecutor} and the persistent id of the object inside
 * the target application.
 *
 * @author gudy
 */
public abstract class AppleScriptObject {
    private final String persistenId;
    private final AppleScriptExecutor executor;

    protected AppleScriptObject( AppleScriptExecutor appleScriptExecutor, String persistentId ) {
        this.executor = appleScriptExecutor;
        this.persistenId = persistentId;
    }

    public String getPersistenId() {
        return persistenId;
    }

    protected ASValue execute(Script script) throws AppleScriptException {
        return executor.execute( script );
    }

    protected AppleScriptExecutor getExecutor(){
        return executor;
    }
}
