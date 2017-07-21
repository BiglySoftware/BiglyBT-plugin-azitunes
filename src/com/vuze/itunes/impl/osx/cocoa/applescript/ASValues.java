package com.vuze.itunes.impl.osx.cocoa.applescript;

import com.vuze.itunes.impl.osx.cocoa.ASValue;

import java.util.List;

/**
 * utility class to coerce {@link com.vuze.itunes.impl.osx.cocoa.ASValue}s into java native types
 *
 * @author olemarchand
 */
public final class ASValues {
    public static int getInt( ASValue v ) {
        return ( ( ASInteger ) v ).getValue();
    }

    public static String getString( ASValue v ) {
        return ( ( ASString ) v ).getValue();
    }

    public static long getLong( ASValue v ) {
        return ( ( ASLong ) v ).getValue();
    }

    public static boolean getBoolean( ASValue v ) {
        return ( ( ASBoolean ) v ).getValue();
    }

    public static List<ASValue> getList( ASValue v ) {
        return ( ( ASList ) v ).getValues();
    }

    public static boolean isNull(ASValue v){
        return ASNull.instance == v;
    }







    private ASValues() throws IllegalAccessException {
        throw new IllegalAccessException("this class should not be instanciated");
    }
}
