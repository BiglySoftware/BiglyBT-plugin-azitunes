package com.vuze.itunes;

public enum ITunesPlaylistKind {
	
	ITunesPlaylistKindUnknown(0,""),
	ITunesPlaylistKindLibrary(1,"cLiP"),
	ITunesPlaylistKindUser(2,"cUsP"),
	ITunesPlaylistKindCD(3,"cCDP"),
	ITunesPlaylistKindDevice(4,"cDvP"),
	ITunesPlaylistKindRadioTuner(5,"cRTP");
	
	ITunesPlaylistKind(int value, String kindName) {
		this.value = value;
        this.kindName = kindName;
    }

    public boolean hasValue(int value){
        return this.value == value;
    }

    public boolean hasKind(String kindName){
        return this.kindName.equals( kindName );
    }

    private int value;
    private String kindName;

	public static ITunesPlaylistKind getITunesPlaylistKind(int value){
		
		for(ITunesPlaylistKind kind : ITunesPlaylistKind.values()) {
			if(kind.hasValue( value ) ) {
				return kind;
			}
		}
		
		return ITunesPlaylistKind.ITunesPlaylistKindUnknown;
	}

	public static ITunesPlaylistKind getITunesPlaylistKind(String kindName){

		for(ITunesPlaylistKind kind : ITunesPlaylistKind.values()) {
			if(kind.hasKind( kindName ) ) {
				return kind;
			}
		}

		return ITunesPlaylistKind.ITunesPlaylistKindUnknown;
	}

    public static boolean isLibrary( String kindName ) {
        return ITunesPlaylistKindLibrary.hasKind( kindName );
    }
}
