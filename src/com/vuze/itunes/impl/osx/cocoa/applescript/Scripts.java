package com.vuze.itunes.impl.osx.cocoa.applescript;

import com.vuze.itunes.impl.osx.cocoa.Script;

/**
 * factory for {@link com.vuze.itunes.impl.osx.cocoa.Script} objects
 *
 * @author olemarchand
 */
public final class Scripts {

    public static Script fromSource(String src){
        return new RawScript( src );
    }

    public static Script fromTemplate(String template, Object... parameters){
        return new StringFormatScript(template, parameters );
    }


    private Scripts() throws InstantiationException {
        throw new InstantiationException("this class should not be instanciated");
    }
}
