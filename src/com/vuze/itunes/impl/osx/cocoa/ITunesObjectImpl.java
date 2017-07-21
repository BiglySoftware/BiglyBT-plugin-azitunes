package com.vuze.itunes.impl.osx.cocoa;

import com.vuze.itunes.ITunesObject;

/**
 * defines a specialization of {@link AppleScriptObject} to reflect more closely itunes' AS exposed internal objects
 *
 * @author gudy
 * @author olemarchand
 */
public abstract class ITunesObjectImpl extends AppleScriptObject implements ITunesObject {
	private final long id;
    private final long index;
    private final String name;
    private final ITunesObject container;


    public ITunesObjectImpl( AppleScriptExecutor appleScriptExecutor, String persistentId, long id, int index, String name, ITunesObject container ) {
        super( appleScriptExecutor, persistentId );
        this.container = container;
        this.id = id;
        this.index = index;
        this.name = name;
    }
    public ITunesObjectImpl( AppleScriptExecutor appleScriptExecutor, String persistentId, long id, int index, String name ) {
        this(appleScriptExecutor, persistentId, id, index, name, null);
    }

    public ITunesObject getContainer() {
        return container;
    }

    public long getId() {
        return id;
    }

    public long getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public String toString() {
		return this.getClass().getName() + " : " + getName() + "(" + getId() + ")";
	}
	
}
