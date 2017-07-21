package com.vuze.itunes.impl.osx.cocoa.applescript;

import com.vuze.itunes.impl.osx.cocoa.ASValue;
import com.vuze.itunes.impl.osx.cocoa.ASValueVisitor;

/**
 * wrapper around a boolean value
 *
 * @author olemarchand
 */
public class ASBoolean implements ASValue {
    private final boolean value;

    public ASBoolean( boolean value ) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value + "(boolean)";
    }

    public void visit( ASValueVisitor visitor ) {
        visitor.handleBoolean(value);
    }
}
