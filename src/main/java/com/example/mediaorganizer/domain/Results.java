package com.example.mediaorganizer.domain;

public class Results {

	private int filesMoved;
	private int filesProcessed;
	private int filesErrored;
	private int duplicatesDeleted;

	public Results(int filesMoved, int filesProcessed, int filesErrored, int duplicatesDeleted) {
		super();
		this.filesMoved = filesMoved;
		this.filesProcessed = filesProcessed;
		this.filesErrored = filesErrored;
		this.duplicatesDeleted = duplicatesDeleted;
	}

	public int getFilesMoved() {
		return filesMoved;
	}

	public int getFilesProcessed() {
		return filesProcessed;
	}

	public int getFilesErrored() {
		return filesErrored;
	}

	public int getDuplicatesDeleted() {
		return duplicatesDeleted;
	}

	@Override
	public String toString() {
		return "Results [filesMoved=" + filesMoved + ", filesProcessed=" + filesProcessed + ", filesErrored="
				+ filesErrored + ", duplicatesDeleted=" + duplicatesDeleted + "]";
	}
}
