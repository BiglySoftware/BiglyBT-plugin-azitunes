package com.vuze.itunes.impl.osx.cocoa;

/**
 * Generic interface for an object that can execute AppleScript scripts.
 *
 * @author olemarchand
 */
public interface AppleScriptExecutor {
    public ASValue execute(Script script) throws AppleScriptException;
}
