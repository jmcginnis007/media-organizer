package com.example.fileorganizer.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "fileorganizer")
public class FileOrganizerConfig {
	
	private String sourcedir;
	private String destdir;
	private Boolean testmode;
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
	
	@Override
	public String toString() {
		return "FileOrganizerConfig [sourcedir=" + sourcedir + ", destdir=" + destdir + ", testMode=" + testmode
				+ ", extensions=" + extensions + "]";
	}
}
