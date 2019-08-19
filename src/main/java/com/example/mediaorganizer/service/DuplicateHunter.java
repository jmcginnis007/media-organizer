package com.example.mediaorganizer.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.example.mediaorganizer.config.Config;
import com.example.mediaorganizer.domain.Results;

@Service
public class DuplicateHunter implements ServiceInterface {

	@Autowired
	private Config config;

	Map<String, List<Path>> possibleDuplicates = new HashMap<>();
	private int errorCount;
	private int processedCount;
	private int duplicatesDeleted;

	@Override
	public Results organize() throws IOException {

		String sourceDir = config.getSourceDirectory();
		List<String> extensions = config.getDuplicateHunter().getFileExtensions();

		for (String extension : extensions) {
			processFiles(sourceDir, extension);
		}

		possibleDuplicates.forEach((key, paths) -> {
			if (paths != null && paths.size() > 1)
				processDuplicates(key, paths);
		});

		Results results = new Results(0, processedCount, errorCount, duplicatesDeleted);
		return results;
	}

	private void processFiles(String sourceDir, String extension) throws IOException {
		Files.walk(Paths.get(sourceDir)).filter(Files::isRegularFile) // filters out directories, leaving just files
				.filter(p -> p.toString().endsWith(extension)).forEach(p -> processFile(p));
	}

	private void processFile(Path file) {
		System.out.println("[PROCESSING] " + file.toString());
		findDuplicates(file);
		processedCount++;
	}

	private void findDuplicates(Path path) {
		File imageFile = new File(path.toString());
		try {
			Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
			for (Directory directory : metadata.getDirectories()) {
				String key = "";
				for (Tag tag : directory.getTags()) {
					if (tag.getTagName().equals("File Size")) {
						key += tag.getDescription();
					} else if (tag.getTagName().equals("File Modified Date")) {
						key += tag.getDescription();
					}
				}
				if (!key.equals("")) {
					if (possibleDuplicates.containsKey(key)) {
						List<Path> currentList = possibleDuplicates.get(key);
						currentList.add(path);
						possibleDuplicates.put(key, currentList);
					} else {
						List<Path> currentList = new ArrayList<>();
						currentList.add(path);
						possibleDuplicates.put(key, currentList);
					}
				}
			}
		} catch (ImageProcessingException | IOException e) {
			errorCount++;
			e.printStackTrace();
		}
	}

	private void processDuplicates(String key, List<Path> paths) {
		if (config.isTestMode()) {
			printPossibleDuplicateInfo(key, paths);
		}
		deleteDuplicates(paths);
	}

	private void deleteDuplicates(List<Path> paths) {
		boolean keptOriginal = false;
		Path original = null;

		for (Path path : paths) {
			if (!keptOriginal) {
				original = path;
				keptOriginal = true;
			} else {
				deleteDuplicate(path, original);
			}
		}
	}

	private void deleteDuplicate(Path path, Path original) {
		if (config.isTestMode()) {
			System.out.println("[TEST] " + path + " is a duplicate of " + original);
			return;
		}

		System.out.println("[DUP] source file is a duplicate so deleting " + path.toString() + "...");
		try {
			Files.delete(path);
			duplicatesDeleted++;
			System.out.println("[SUCCESS] successfully deleted " + path.toString());
		} catch (IOException e) {
			System.out.println("[ERROR]...unable to delete" + path.toString());
		}
	}

	private void printPossibleDuplicateInfo(String key, List<Path> possibleDupes) {
		System.out.println("<tr> <td>" + key + "</td> <td><ul>");
		for (Path item : possibleDupes) {
			System.out.println("<li><a href=\"" + item + "\">" + item + "</a></li>");
		}
		System.out.println("</ul></td></tr>");
	}

}
