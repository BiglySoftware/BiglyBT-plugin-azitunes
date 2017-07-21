package com.vuze.itunes.impl.osx.cocoa.applescript;

/**
 * insert docs here :dawa:
 *
 * @author olemarchand
 */
public class StringFormatScript extends AbstractParametrizedScript{

    public StringFormatScript( String sourceCodeTemplate, Object... parameters ) {
        super( sourceCodeTemplate, parameters );
    }

    public String getSourceCode() {
        return String.format( sourceCodeTemplate, parameters );
    }
}
