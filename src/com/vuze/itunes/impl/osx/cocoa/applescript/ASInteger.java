package com.vuze.itunes.impl.osx.cocoa.applescript;

import com.vuze.itunes.impl.osx.cocoa.ASValue;
import com.vuze.itunes.impl.osx.cocoa.ASValueVisitor;

/**
 * wrapper around a int value
 *
 * @author olemarchand
 */
public class ASInteger implements ASValue {
    private final int value;

    public ASInteger( int value ) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value + "(int)";
    }

    public void visit( ASValueVisitor visitor ) {
        visitor.handleInteger( value );
    }
}
