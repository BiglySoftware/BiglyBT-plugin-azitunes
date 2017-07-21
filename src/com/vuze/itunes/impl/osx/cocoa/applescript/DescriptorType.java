package com.vuze.itunes.impl.osx.cocoa.applescript;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

public enum DescriptorType {
    AEList( Type.typeAEList ),
    AERecord( Type.typeAERecord ),
    True( Type.typeTrue ),
    False( Type.typeFalse ),
    Boolean( Type.typeBoolean ),
    Char( Type.typeChar ),
    UTF16ExternalRepresentation( Type.typeUTF16ExternalRepresentation ),
    UTF8Text( Type.typeUTF8Text ),
    UnicodeText( Type.typeUnicodeText ),
    CString( Type.typeCString ),
    PString( Type.typePString ),
    SInt16( Type.typeSInt16 ),
    UInt16( Type.typeUInt16 ),
    SInt32( Type.typeSInt32 ),
    UInt32( Type.typeUInt32 ),
    SInt64( Type.typeSInt64 ),
    UInt64( Type.typeUInt64 ),
    IEEE32BitFloatingPoint( Type.typeIEEE32BitFloatingPoint ),
    IEEE64BitFloatingPoint( Type.typeIEEE64BitFloatingPoint ),
    Enumerated( Type.typeEnumerated ),
    Null( Type.typeNull ),
    Object( Type.typeObj ),
    ObjectType( Type.typeType ),
    UNKNOWN(null);


    private final Type type;

    DescriptorType( Type type ) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public static DescriptorType fromName(String name){
        final DescriptorType type = fromTypeName.get( name );
        if (type == null){
            return UNKNOWN;
        }else{
            return type;
        }
    }

    public static DescriptorType fromCode(int code){
        final DescriptorType type = fromTypeCode.get( code );
        if (type == null){
            System.out.println( "UNKNOWN ==> "+getStringFromType( code ));
            return UNKNOWN;
        }else{
            return type;
        }
    }

    private static final Map<Integer,DescriptorType> fromTypeCode;
    private static final Map<String,DescriptorType> fromTypeName;

    static {
            Map<String, DescriptorType> fromName = new HashMap<String, DescriptorType>();
            Map<Integer, DescriptorType> fromCode = new HashMap<Integer, DescriptorType>();
            for( DescriptorType descriptor : values() ) {
                if( descriptor.type!=null ) {
                    fromName.put( descriptor.type.name, descriptor );
                    fromCode.put( descriptor.type.code, descriptor );
                }
            }
            fromTypeCode = Collections.unmodifiableMap( fromCode );
            fromTypeName = Collections.unmodifiableMap( fromName );
        }

    /**
     * Utility method to get a type descriptor name from its code
     * @param typeCode the code of the type
     * @return the name of the type
     */
    public static String getStringFromType( int typeCode ) {
        byte[] bytes = new byte[4];
        for( int i = 0; i < 4 ; i++ ) {
            bytes[3 - i] = ( byte ) ( typeCode & 0x000000FF );
            typeCode = typeCode / 256;
        }

        return new String( bytes );
    }

    //TODO refactor out this method maybe into AppleScriptExecutor ?
        public static int getIntCodeFromString( String input ) {
            int n = 0;
            byte[] bytes = input.getBytes();
            for( byte aByte : bytes ) {
                int b = 0x000000FF & ( int ) aByte;
                n = n * 256 + b;
            }

            return n;

        }

    public enum Type {
        typeAEList( "list" ),

        typeAERecord( "reco" ),

        typeTrue( "true" ),
        typeFalse( "fals" ),
        typeBoolean( "bool" ),

        typeChar( "TEXT" ),
        typeUTF16ExternalRepresentation( "ut16" ), /* big-endian 16 bit unicode with optional byte-order-mark, or little-endian 16 bit unicode with required byte-order-mark. */
        typeUTF8Text( "utf8" ),
        typeUnicodeText( "utxt" ), /* native byte ordering, optional BOM */
        typeCString( "cstr" ), /* MacRoman characters followed by a NULL byte */
        typePString( "pstr" ), /* Unsigned length byte followed by MacRoman characters */

        typeSInt16( "shor" ), /* SInt16 : signed; 16 bit integer */
        typeUInt16( "ushr" ), /* UInt16 : unsigned; 16 bit integer */
        typeSInt32( "long" ), /* SInt32 : signed; 32 bit integer */
        typeUInt32( "magn" ), /* UInt32 : unsigned; 32 bit integer */
        typeSInt64( "comp" ), /* SInt64 : signed; 64 bit integer */
        typeUInt64( "ucom" ), /* UInt64 : unsigned; 64 bit integer */
        typeIEEE32BitFloatingPoint( "sing" ), /* float */
        typeIEEE64BitFloatingPoint( "doub" ), /* double */

        typeNull( "null" ),

        typeObj( "obj " ),
        typeType( "type" ),

        typeEnumerated( "enum" );

        private final String name;
        private final int code;

        Type( String name ) {
            this.name = name;
            this.code = getIntCodeFromString( name );
        }

        public String getName() {
            return name;
        }

        public int getCode() {
            return code;
        }


    }
}