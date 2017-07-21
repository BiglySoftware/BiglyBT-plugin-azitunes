package com.vuze.itunes.impl.osx.cocoa.applescript;

import com.vuze.itunes.impl.osx.cocoa.ASValue;
import com.vuze.itunes.impl.osx.cocoa.ASValueVisitor;

/**
 * wrapper around a String value
 *
 * @author olemarchand
 */
public class ASString implements ASValue {
    private final String value;

    public ASString( String value ) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value + "(String)";
    }

    public void visit( ASValueVisitor visitor ) {
        visitor.handleString( value );
    }
}
