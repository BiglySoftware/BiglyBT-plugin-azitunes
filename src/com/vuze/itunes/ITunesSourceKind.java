package com.vuze.itunes;

import com.vuze.itunes.impl.osx.cocoa.applescript.DescriptorType;

public enum ITunesSourceKind {
	
	ITunesSourceKindUnknown(0,"kUnk"),
	ITunesSourceKindLibrary(1,"kLib"),
	ITunesSourceKindIPod(2,"kPod"),
	ITunesSourceKindAudioCD(3,"kACD"),
	ITunesSourceKindMP3CD(4,"kMCD"),
	ITunesSourceKindDevice(5,"kDev"),
	ITunesSourceKindRadioTuner(6,"kTun"),
	ITunesSourceKindSharedLibrary(7,"kShd");
	
	ITunesSourceKind(int value, String kindName) {
		this.value = value;
        this.kindName = kindName;
    }
	
	private int value;
	private String kindName;

	public static ITunesSourceKind getITunesSourceKind(int value){
		
		for(ITunesSourceKind kind : ITunesSourceKind.values()) {
			if(kind.value ==  value) {
				return kind;
			}
		}
		
		return ITunesSourceKind.ITunesSourceKindUnknown;
	}

    public static ITunesSourceKind fromTypeString( String kindName ) {
        for(ITunesSourceKind kind : ITunesSourceKind.values()) {
			if(kind.kindName.equals( kindName )) {
				return kind;
			}
		}

		return ITunesSourceKind.ITunesSourceKindUnknown;
    }
}
