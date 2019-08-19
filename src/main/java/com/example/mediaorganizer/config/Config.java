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
	private QuarterlyFolders quarterlyFolders = new QuarterlyFolders();
	private DuplicateHunter duplicateHunter = new DuplicateHunter();

	public static class QuarterlyFolders {
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
	
	public static class DuplicateHunter {
		
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

	public QuarterlyFolders getQuarterlyFolders() {
		return quarterlyFolders;
	}

	public void setQuarterlyFolders(QuarterlyFolders quarterlyFolders) {
		this.quarterlyFolders = quarterlyFolders;
	}

	public DuplicateHunter getDuplicateHunter() {
		return duplicateHunter;
	}

	public void setDuplicateHunter(DuplicateHunter duplicateHunter) {
		this.duplicateHunter = duplicateHunter;
	}

	@Override
	public String toString() {
		return "Config [sourceDirectory=" + sourceDirectory + ", testMode=" + testMode + ", mode=" + mode
				+ ", quarterlyFolders=" + quarterlyFolders + "]";
	}
}
