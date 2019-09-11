package com.example.mediaorganizer.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.example.mediaorganizer.config.Config;
import com.example.mediaorganizer.domain.DateSources;
import com.example.mediaorganizer.domain.Results;
import com.example.mediaorganizer.util.DateUtils;
import com.example.mediaorganizer.util.MonthToFolderMapper;

@Service
public class QuarterlyFolderService implements ServiceInterface {

	private int total;
	private int success;
	private int duplicatesDeleted;

	@Autowired
	private Config config;

	int duplicateCounter;

	public Results organize() throws IOException {

		String sourceDir = config.getSourceDirectory();
		String destDir = config.getQuarterlyFoldersConfig().getDestinationDirectory();
		List<String> extensions = config.getQuarterlyFoldersConfig().getFileExtensions();

		for (String extension : extensions) {
			processFiles(sourceDir, destDir, extension);
		}

		return new Results(success, total, config.isTestMode() ? 0 : (total - success), duplicatesDeleted);
	}

	private void processFiles(String sourceDir, String destDir, String extension) throws IOException {
		Files.walk(Paths.get(sourceDir)).filter(Files::isRegularFile) // filters out directories, leaving just files
				.filter(p -> p.toString().endsWith(extension)).forEach(p -> processFile(p, destDir));
	}

	private void processFile(Path file, String destDirRoot) {
		System.out.println("[PROCESSING] " + file.toString());
		total++;

		String destinationPath = getDestinationPath(file);

		if (destinationPath == null) {
			if (config.getQuarterlyFoldersConfig().getDateSource().equals(DateSources.METADATA_ONLY)) {
				System.out.println(
						"[NOMETA] " + file.toString() + " doesn't have any readable metadata [metadataonly= true]");
			} else {
				System.out
						.println("[ERROR] " + file.toString() + " failed to read file system information, skipping...");
			}
		} else {
			moveFile(file, destinationPath);
		}
	}

	private String getDestinationPath(Path file) {
		String destinationPath = null;
		LocalDate fileDate = getFileDate(file);

		if (fileDate == null) {
			if (config.getQuarterlyFoldersConfig().getDateSource().equals(DateSources.METADATA_ONLY)) {
				System.out.println(
						"[NOMETA] " + file.toString() + " doesn't have any readable metadata [metadataonly= true]");
			} else {
				System.out.println("[ERROR] " + file.toString() + " failed to read file system date, skipping...");
			}
		} else {
			destinationPath = config.getQuarterlyFoldersConfig().getDestinationDirectory() + assembleDestinationPath(fileDate)
					+ file.getFileName();
		}

		return destinationPath;
	}

	private LocalDate getFileDate(Path file) {
		LocalDate fileDate = null;

		switch (config.getQuarterlyFoldersConfig().getDateSource()) {
		case FILE_CREATION: {
			fileDate = DateUtils.getFileSystemDate(file, DateSources.FILE_CREATION);
		}
		case FILE_MODIFICATION: {
			fileDate = DateUtils.getFileSystemDate(file, DateSources.FILE_MODIFICATION);
		}
		case METADATA_ONLY: {
			fileDate = getImageDateTaken(file);
		}
		case METADATA_THEN_FILE_CREATION: {
			fileDate = getImageDateTaken(file);

			if (fileDate == null) {
				fileDate = DateUtils.getFileSystemDate(file, DateSources.FILE_CREATION);
			}
		}
		case METADATA_THEN_FILE_MODIFICATION: {
			fileDate = getImageDateTaken(file);

			if (fileDate == null) {
				fileDate = DateUtils.getFileSystemDate(file, DateSources.FILE_MODIFICATION);
			}
		}
		}
		return fileDate;
	}

	private String assembleDestinationPath(LocalDate fileCreationDate) {
		int month = fileCreationDate.getMonthValue();
		int year = fileCreationDate.getYear();

		String monthFolder = MonthToFolderMapper.getFolderName(new Integer(month));
		String destinationPath = "\\" + Integer.toString(year) + "\\" + monthFolder + "\\";

		return destinationPath;
	}

	private void moveFile(Path path, String destinationPath) {
		if (config.getQuarterlyFoldersConfig().isLogAllTags())
			printAllImageTags(path);

		if (config.isTestMode()) {
			System.out
					.println("[TEST] " + path.toString() + " ---> " + destinationPath + "[" + getFileDate(path) + "]");
			return;
		}

		try {
			FileUtils.moveFile(FileUtils.getFile(path.toString()), FileUtils.getFile(destinationPath));
			success++;
			System.out.println("[SUCCESS] " + path.toString() + " ---> " + destinationPath);
		} catch (IOException e) {
			System.out.println("[ERROR] Unable to move file " + path.toString() + " -- " + e.getMessage());
			deleteDuplicate(path, destinationPath);
		}
	}

	private LocalDate getImageDateTaken(Path path) {
		// see https://github.com/drewnoakes/metadata-extractor
		File imageFile = new File(path.toString());
		Date dateTaken = null;
		LocalDate metaDateTaken = null;

		try {
			Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
			// obtain the Exif directory
			ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

			// query the tag's value
			if (directory != null) {
				dateTaken = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
				if (dateTaken == null)
					dateTaken = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_DIGITIZED);
				if (dateTaken == null)
					dateTaken = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME);
			}
		} catch (ImageProcessingException | IOException e) {
			e.printStackTrace();
		} catch (Exception npe) {
			System.out.println("[ERROR] Cannot process " + path.toString());
		}

		if (dateTaken != null) {
			metaDateTaken = dateTaken.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		}
		return metaDateTaken;
	}

	private void printAllImageTags(Path path) {
		File imageFile = new File(path.toString());

		try {
			Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
			for (Directory directory : metadata.getDirectories()) {
				for (Tag tag : directory.getTags()) {
					System.out.println(tag);
				}
			}
			System.out.println("===================================================================================");
		} catch (ImageProcessingException | IOException e) {
			e.printStackTrace();
		}
	}

	private void deleteDuplicate(Path path, String destinationPath) {
		if (!config.getQuarterlyFoldersConfig().isDeleteDuplicates())
			return;
		long sourceFileSize = getFileSize(path);
		long destFileSize = getFileSize(Paths.get(destinationPath));

		if (sourceFileSize == destFileSize) {
			System.out.println("[DUP] source file is a duplicate so deleting " + path.toString() + "...");
			try {
				Files.delete(path);
				duplicatesDeleted++;
				System.out.println("[SUCCESS] successfully deleted " + path.toString());
			} catch (IOException e) {
				System.out.println("[ERROR]...unable to delete" + path.toString());
			}
		} else
			System.out.println("[NODUP] source file (" + sourceFileSize + ") is NOT a duplicate (" + destFileSize
					+ ") so leaving " + path.toString() + "...");
	}

	private long getFileSize(Path path) {
		File imageFile = new File(path.toString());
		return imageFile.length();
	}
}
