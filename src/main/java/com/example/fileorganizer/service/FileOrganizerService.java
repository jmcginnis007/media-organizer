package com.example.fileorganizer.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
import com.example.fileorganizer.config.FileOrganizerConfig;
import com.example.fileorganizer.domain.MonthToFolderMapper;
import com.example.fileorganizer.domain.Results;

@Service
public class FileOrganizerService {
	
	private int total = 0;
	private int success = 0;
	
	@Autowired
	private FileOrganizerConfig config;
	
	public Results organizeFiles(String sourceDir, String destDir, List<String> extensions) throws IOException {
				
		for (String extension : extensions) {
			processFiles(sourceDir, destDir, extension);
		}
		
		return new Results(success, total, config.getTestmode() ? 0 : (total - success));
	}
	
	private void processFiles(String sourceDir, String destDir, String extension) throws IOException {
		Files.walk(Paths.get(sourceDir))
		 .filter(Files::isRegularFile) //filters out directories, leaving just files
		 .filter(p -> p.toString().endsWith(extension))
	     .forEach(p -> processFile(p, destDir));
		
	}
	
	private void processFile(Path file, String destDirRoot) {
		String destinationPath = "";
		total++;
		LocalDate creationDate = getFileCreationDate(file);
		LocalDate metaCreationDate = getImageDateTaken(file);
		
		// only move files that i found meta creation date in
		if (config.getMetadataonly() && metaCreationDate != null)
		{
			destinationPath = destDirRoot + getDestinationPath(metaCreationDate) + file.getFileName();
			moveFile(file, destinationPath);
		} 
		else if (!config.getMetadataonly()) {
			// choose meta creation date if we have it, otherwise fall back to file creation date
			if (metaCreationDate != null) {
				destinationPath = destDirRoot + getDestinationPath(metaCreationDate) + file.getFileName();
			}
			else {
				destinationPath = destDirRoot + getDestinationPath(creationDate) + file.getFileName();
			}
			moveFile(file, destinationPath);
		}		
	}
	
	private LocalDate getFileCreationDate(Path file) {
		BasicFileAttributes attributes;
		LocalDate localDate = null;
		
		try {
			attributes = Files.readAttributes(file,  BasicFileAttributes.class);
			FileTime fileTime = attributes.creationTime();
			String pattern = "yyyy-MM-dd";
		    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		    String creationDate = simpleDateFormat.format( new Date( fileTime.toMillis() ) );
		    
		    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
	        localDate = LocalDate.parse(creationDate, formatter);
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return localDate;
	}
	
	private String getDestinationPath(LocalDate fileCreationDate) {
		int month = fileCreationDate.getMonthValue();
		int year = fileCreationDate.getYear();
		
		String monthFolder = MonthToFolderMapper.getFolderName(new Integer(month));
		String destinationPath = "\\" + Integer.toString(year) + "\\" + monthFolder + "\\";
		
		return destinationPath;
	}
	
	private void moveFile(Path file, String destinationPath) {
		if (config.getShowalltags()) printAllImageTags(file); 
		
		if (config.getTestmode()) {
			System.out.println("[TEST] " + file.toString() + " ---> " + destinationPath + "[" + getImageDateTaken(file) + "]");
			return;
		}
		
		try {
			FileUtils.moveFile(
				      FileUtils.getFile(file.toString()), 
				      FileUtils.getFile(destinationPath));
			success++;
			System.out.println("[SUCCESS] " + file.toString() + " ---> " + destinationPath);
		} catch (IOException e) {
			System.out.println("[ERROR] Unable to move file " + file.toString());
		}
	}
	
	private LocalDate getImageDateTaken(Path path) {
		//https://github.com/drewnoakes/metadata-extractor
		File imageFile = new File(path.toString());
		Date dateTaken = null;
		LocalDate metaDateTaken = null;
		
		try {
			Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
			// obtain the Exif directory
			ExifSubIFDDirectory directory
			    = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

			// query the tag's value
			if (directory != null) {
				dateTaken = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
				if (dateTaken == null) dateTaken = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_DIGITIZED);
				if (dateTaken == null) dateTaken = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME);
			}
		} catch (ImageProcessingException | IOException e) {
			e.printStackTrace();
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
		} catch (ImageProcessingException | IOException e) {
			e.printStackTrace();
		}
	}
}
