package com.vuze.itunes.impl.osx.cocoa.applescript;

import com.vuze.itunes.impl.osx.cocoa.ASValue;
import com.vuze.itunes.impl.osx.cocoa.ASValueVisitor;

/**
 * value obtained for an apple script "class" property
 *
 * @author olemarchand
 */
public class ASObjectType implements ASValue{
    private final String value;

    public ASObjectType( String value ) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value + "(class)";
    }

    public void visit( ASValueVisitor visitor ) {
        visitor.handleObjectType( value );
    }


}
