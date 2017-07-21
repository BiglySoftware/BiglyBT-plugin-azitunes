package com.vuze.itunes;

/**
 * generic exception class to be used when an exception occurs when interacting with itunes
 * (jacob or applescript)
 *
 * @author olemarchand
 */
public class ITunesCommunicationException extends Exception{
    public ITunesCommunicationException( Throwable cause ) {
        super( cause );
    }

    public ITunesCommunicationException( String message ) {
        super( message );
    }

    public ITunesCommunicationException( String message, Throwable cause ) {
        super( message, cause );
    }
}
