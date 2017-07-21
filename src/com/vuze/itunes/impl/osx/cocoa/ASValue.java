package com.vuze.itunes.impl.osx.cocoa;

/**
 * Interface to wrap the result of an AppleScript execution
 *
 * @author olemarchand
 */
public interface ASValue {
    public void visit( ASValueVisitor visitor);
}
