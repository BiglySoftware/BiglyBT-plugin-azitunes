package com.vuze.itunes.impl.osx.cocoa.applescript;

import com.vuze.itunes.impl.osx.cocoa.Script;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * implementation of the {@link com.vuze.itunes.impl.osx.cocoa.Script} interface that builds the source code of the
 * script using {@link java.text.MessageFormat#format(String, Object[])}.
 *
 * @author olemarchand
 */
public class MessageFormatScript extends AbstractParametrizedScript {

    public MessageFormatScript( String sourceCodeTemplate, Object... parameters ) {
        super( sourceCodeTemplate, parameters );
    }

    public String getSourceCode() {
        return MessageFormat.format( sourceCodeTemplate, parameters );
    }
}
