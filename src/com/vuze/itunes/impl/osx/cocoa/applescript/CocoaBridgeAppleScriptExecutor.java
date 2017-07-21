package com.vuze.itunes.impl.osx.cocoa.applescript;

import com.apple.cocoa.foundation.*;
import com.vuze.itunes.impl.osx.cocoa.Script;
import com.vuze.itunes.impl.osx.cocoa.ASValue;
import com.vuze.itunes.impl.osx.cocoa.AppleScriptExecutor;
import com.vuze.itunes.impl.osx.cocoa.AppleScriptException;

import java.util.ArrayList;
import java.util.List;

/**
 * implementation that relies on Apple's java-cocoa bridge to execute applescript scripts
 *
 *
 * IMPORTANT: cocoa-java bridge is deprecated and works only with java5 and previous versions of java.
 * it is 32bits only and java6 on the mac is 64bits only.
 * There is a good chance this code will not work in next versions of Mac OS X (including Snow Leopard)
 *
 * @author olemarchand
 */
@SuppressWarnings( { "deprecation" } )
public class CocoaBridgeAppleScriptExecutor implements AppleScriptExecutor {

    public ASValue execute( Script script ) throws AppleScriptException {
        assert script != null;

        int pool = NSAutoreleasePool.push();


        final String sourceCode = script.getSourceCode();

        NSAppleScript scp = new NSAppleScript( sourceCode );
        final NSMutableDictionary nsMutableDictionary = new NSMutableDictionary();
        NSAppleEventDescriptor result =  scp.execute( nsMutableDictionary );

        if (nsMutableDictionary.count()!=0){
            throw new AppleScriptException(nsMutableDictionary, sourceCode );
        }

        NSAutoreleasePool.pop(pool);

        return toASValue( result );
    }

    private ASValue toASValue(NSAppleEventDescriptor descriptor) {
        if( descriptor == null ) {
            return ASNull.instance;
        }

        final int code = descriptor.descriptorType();
        DescriptorType type = DescriptorType.fromCode( code );
        if (type == DescriptorType.UNKNOWN){
            System.err.println( "object type:["+DescriptorType.getStringFromType( code ) +"]" );
            System.err.println( "descriptor.stringValue() = " + descriptor.stringValue() );
            System.err.println( "descriptor = " + descriptor );
        }
        
        switch( type ){
            case Boolean:
            case True:
            case False:
                return new ASBoolean(descriptor.booleanValue());

            case Char:
            case UTF16ExternalRepresentation:
            case UTF8Text:
            case UnicodeText:
            case CString:
            case PString:
                return new ASString( descriptor.stringValue() );

            case Null:
                return ASNull.instance;

            case UInt16:
            case SInt16:
            case UInt32:
            case SInt32:
                return new ASInteger( descriptor.int32Value() );

            case UInt64:
            case SInt64:
                return new ASLong( Long.parseLong(descriptor.stringValue()) );

            case AERecord:
            case AEList:
                List<ASValue> list = new ArrayList<ASValue>();
                for(int i = 1 ; i <= descriptor.numberOfItems() ; i++) {
                    NSAppleEventDescriptor desc = descriptor.descriptorAtIndex(i);
                    list.add( toASValue(desc));
                }

                return new ASList( list );

            case Enumerated:
                //It's easier to read Enums from their String representation
    		    return new ASString( descriptor.stringValue() );

            case ObjectType:
                int value = descriptor.enumCodeValue();
                final String fromType = DescriptorType.getStringFromType( value );
                return new ASObjectType( fromType );

            case Object:
            case UNKNOWN:
            default:
                return new ASString( descriptor.stringValue() );
        }

    }
}
