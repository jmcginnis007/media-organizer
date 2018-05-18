package com.example.fileorganizer.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "fileorganizer")
public class FileOrganizerConfig {
	
	private String sourcedir;
	private String destdir;
	private Boolean testmode; // if true, won't actually move the file
	private Boolean showalltags; // if true it will print out all meta tags for each file
	private Boolean metadataonly; // if true, only process a file that has date in the meta data
	private List<String> extensions;
	
	public String getSourcedir() {
		return sourcedir;
	}
	public void setSourcedir(String sourcedir) {
		this.sourcedir = sourcedir;
	}
	public String getDestdir() {
		return destdir;
	}
	public void setDestdir(String destdir) {
		this.destdir = destdir;
	}
	public Boolean getTestmode() {
		return testmode;
	}
	public void setTestmode(Boolean testmode) {
		this.testmode = testmode;
	}
	public List<String> getExtensions() {
		return extensions;
	}
	public void setExtensions(List<String> extensions) {
		this.extensions = extensions;
	}	
	public Boolean getShowalltags() {
		return showalltags;
	}
	public void setShowalltags(Boolean showalltags) {
		this.showalltags = showalltags;
	}
	public Boolean getMetadataonly() {
		return metadataonly;
	}
	public void setMetadataonly(Boolean metadataonly) {
		this.metadataonly = metadataonly;
	}
	@Override
	public String toString() {
		return "FileOrganizerConfig [sourcedir=" + sourcedir + ", destdir=" + destdir + ", testmode=" + testmode
				+ ", showalltags=" + showalltags + ", metadataonly=" + metadataonly + ", extensions=" + extensions
				+ "]";
	}
	
}
