package com.vuze.itunes.impl.osx.cocoa.applescript;

import com.vuze.itunes.impl.osx.cocoa.Script;

/**
 * simple implementation of the {@link com.vuze.itunes.impl.osx.cocoa.Script} interface
 * that consists of only a string
 *
 * @author olemarchand
 */
public class RawScript implements Script {
    private final String sourceCode;

    public RawScript( String sourceCode ) {
        this.sourceCode = sourceCode;
    }

    public String getSourceCode() {
        return sourceCode;
    }


    public String toString() {
        return "RawScript{\n" +
               sourceCode.trim() + '\n' +
               '}';
    }
}
