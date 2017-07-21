package com.vuze.itunes.impl.osx.cocoa.applescript;

import com.vuze.itunes.impl.osx.cocoa.ASValue;
import com.vuze.itunes.impl.osx.cocoa.ASValueVisitor;

/**
 * Null value wrapper
 *
 * @author olemarchand
 */
public enum ASNull implements ASValue {
    instance;

    @Override
    public String toString() {
        return "null (as)";
    }


    public void visit( ASValueVisitor visitor ) {
        visitor.handleNull();
    }
}
