package com.vuze.itunes.impl.osx.cocoa.applescript;

import com.vuze.itunes.impl.osx.cocoa.ASValue;
import com.vuze.itunes.impl.osx.cocoa.ASValueVisitor;

/**
 * wrapper around a int value
 *
 * @author olemarchand
 */
public class ASLong implements ASValue {
    private final long value;

    public ASLong( long value ) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value + "(long)";
    }

    public void visit( ASValueVisitor visitor ) {
        visitor.handleLong( value );
    }
}
