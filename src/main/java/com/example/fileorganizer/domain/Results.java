package com.example.fileorganizer.domain;

public class Results {
	
	private int filesMoved;
	private int filesProcessed;
	private int filesErrored;
	
	public Results(int filesMoved, int filesProcessed, int filesErrored) {
		super();
		this.filesMoved = filesMoved;
		this.filesProcessed = filesProcessed;
		this.filesErrored = filesErrored;
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

	@Override
	public String toString() {
		return "Results [filesMoved=" + filesMoved + ", filesProcessed=" + filesProcessed + ", filesErrored="
				+ filesErrored + "]";
	}
}
