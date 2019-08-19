package com.example.mediaorganizer.domain;

public enum DateSources {
	
	METADATA_ONLY,
	FILE_MODIFICATION,
	FILE_CREATION,
	METADATA_THEN_FILE_CREATION,
	METADATA_THEN_FILE_MODIFICATION
}
