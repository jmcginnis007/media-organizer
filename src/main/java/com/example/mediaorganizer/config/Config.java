package com.example.mediaorganizer.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.example.mediaorganizer.domain.DateSources;
import com.example.mediaorganizer.domain.Modes;

@Configuration
@ConfigurationProperties(prefix = "media-organizer")
public class Config {

	private String sourceDirectory;
	private boolean testMode; // if true, won't actually change the file system
	private Modes mode;
	private QuarterlyFoldersConfig quarterlyFoldersConfig = new QuarterlyFoldersConfig();
	private DuplicateHunterConfig duplicateHunterConfig = new DuplicateHunterConfig();
	private RenameFilesConfig renameFilesConfig = new RenameFilesConfig();

	public static class QuarterlyFoldersConfig {
		private String destinationDirectory;
		private DateSources dateSource;
		private boolean deleteDuplicates;
		private boolean logAllTags; // if true it will print out all meta tags for each file
		private List<String> fileExtensions;

		public String getDestinationDirectory() {
			return destinationDirectory;
		}

		public void setDestinationDirectory(String destinationDirectory) {
			this.destinationDirectory = destinationDirectory;
		}

		public DateSources getDateSource() {
			return dateSource;
		}

		public void setDateSource(DateSources dateSource) {
			this.dateSource = dateSource;
		}

		public boolean isDeleteDuplicates() {
			return deleteDuplicates;
		}

		public void setDeleteDuplicates(boolean deleteDuplicates) {
			this.deleteDuplicates = deleteDuplicates;
		}

		public boolean isLogAllTags() {
			return logAllTags;
		}

		public void setLogAllTags(boolean logAllTags) {
			this.logAllTags = logAllTags;
		}

		public List<String> getFileExtensions() {
			return fileExtensions;
		}

		public void setFileExtensions(List<String> fileExtensions) {
			this.fileExtensions = fileExtensions;
		}

		@Override
		public String toString() {
			return "QuarterlyFolders [destinationDirectory=" + destinationDirectory + ", dateSource=" + dateSource
					+ ", deleteDuplicates=" + deleteDuplicates + ", logAllTags=" + logAllTags + ", fileExtensions="
					+ fileExtensions + "]";
		}
	}

	public static class DuplicateHunterConfig {

		private List<String> fileExtensions;

		public List<String> getFileExtensions() {
			return fileExtensions;
		}

		public void setFileExtensions(List<String> fileExtensions) {
			this.fileExtensions = fileExtensions;
		}

		@Override
		public String toString() {
			return "DuplicateHunter [fileExtensions=" + fileExtensions + "]";
		}

	}

	public static class RenameFilesConfig {
		private String replace;
		private String replaceWith;

		public String getReplace() {
			return replace;
		}

		public void setReplace(String replace) {
			this.replace = replace;
		}

		public String getReplaceWith() {
			return replaceWith;
		}

		public void setReplaceWith(String replaceWith) {
			this.replaceWith = replaceWith;
		}

		@Override
		public String toString() {
			return "RenameFilesConfig [replace=" + replace + ", replaceWith=" + replaceWith + "]";
		}

	}

	public String getSourceDirectory() {
		return sourceDirectory;
	}

	public void setSourceDirectory(String sourceDirectory) {
		this.sourceDirectory = sourceDirectory;
	}

	public boolean isTestMode() {
		return testMode;
	}

	public void setTestMode(boolean testMode) {
		this.testMode = testMode;
	}

	public Modes getMode() {
		return mode;
	}

	public void setMode(Modes mode) {
		this.mode = mode;
	}

	public QuarterlyFoldersConfig getQuarterlyFoldersConfig() {
		return quarterlyFoldersConfig;
	}

	public void setQuarterlyFoldersConfig(QuarterlyFoldersConfig quarterlyFoldersConfig) {
		this.quarterlyFoldersConfig = quarterlyFoldersConfig;
	}

	public DuplicateHunterConfig getDuplicateHunterConfig() {
		return duplicateHunterConfig;
	}

	public void setDuplicateHunterConfig(DuplicateHunterConfig duplicateHunterConfig) {
		this.duplicateHunterConfig = duplicateHunterConfig;
	}

	public RenameFilesConfig getRenameFilesConfig() {
		return renameFilesConfig;
	}

	public void setRenameFilesConfig(RenameFilesConfig renameFilesConfig) {
		this.renameFilesConfig = renameFilesConfig;
	}

	@Override
	public String toString() {
		return "Config [sourceDirectory=" + sourceDirectory + ", testMode=" + testMode + ", mode=" + mode
				+ ", quarterlyFoldersConfig=" + quarterlyFoldersConfig + ", duplicateHunterConfig="
				+ duplicateHunterConfig + ", renameFilesConfig=" + renameFilesConfig + "]";
	}
	
}
