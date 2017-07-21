package com.vuze.itunes.impl.osx.cocoa.applescript;

import com.vuze.itunes.impl.osx.cocoa.ASValue;
import com.vuze.itunes.impl.osx.cocoa.ASValueVisitor;

import java.util.List;
import java.util.Collections;
import java.util.Iterator;

/**
 * wrapper around a List value
 *
 * @author olemarchand
 */
public class ASList implements ASValue, Iterable<ASValue> {
    private final List<ASValue> values;

    public ASList( List<ASValue> values ) {
        this.values = Collections.unmodifiableList( values );
    }

    public List<ASValue> getValues() {
        return values;
    }

    public Iterator<ASValue> iterator() {
        return values.iterator();
    }

    public ASValue get(int index){
        return values.get( index );
    }

    public String getString(int index){
        return ( ( ASString ) values.get( index ) ).getValue();
    }

    public String getObjectTypeString(int index){
        return ( ( ASObjectType ) values.get( index ) ).getValue();
    }

    public int getInt(int index){
        final ASValue value = values.get( index );
        if (value instanceof ASString){
            ASString asString = ( ASString ) value;
            return Integer.parseInt( asString.getValue() );
        }else{
            return ( ( ASInteger ) value ).getValue();
        }
    }

    public boolean getBoolean(int index){
        return ( ( ASBoolean ) values.get( index ) ).getValue();
    }

    public long getLong(int index){
        final ASValue value = values.get( index );
        if (value instanceof ASString){
            ASString asString = ( ASString ) value;
            try{
            	return Long.parseLong( asString.getValue() );
            } catch(Throwable e) {
            	try {
            		return (long) Double.parseDouble(asString.getValue());
            	} catch (Throwable e2) {
					return 0l;
				}
            }
        }else if (value instanceof ASInteger){
            return ( ( ASInteger ) value ).getValue();                 
        }else{
            return ( ( ASLong ) value ).getValue();
        }
    }

    public ASList getList(int index){
        return ( ASList ) values.get( index );
    }

    public int size(){
        return values.size();
    }

    @Override
    public String toString() {
        return String.valueOf( values );
    }

    public void visit( ASValueVisitor visitor ) {
        visitor.handleList( values );
    }
}
