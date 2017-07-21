package com.vuze.itunes.impl.osx.cocoa;

import java.util.List;

/**
 * Visitor interface to simulate dynamic dispatch of {@link ASValue}s
 *
 * @author olemarchand
 */
public interface ASValueVisitor {
    void handleBoolean( boolean value );

    void handleInteger( int value );

    void handleList( List<ASValue> values );

    void handleLong( long value );

    void handleNull();

    void handleString( String value );

    void handleObjectType( String typeName );
}
