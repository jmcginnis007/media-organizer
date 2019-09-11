package com.example.mediaorganizer.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mediaorganizer.config.Config;
import com.example.mediaorganizer.domain.Results;

@Service
public class RenameFilesService implements ServiceInterface {
	
	@Autowired
	private Config config;
	
	private int errorCount;
	private int processedCount;
	private int duplicatesDeleted;

	@Override
	public Results organize() throws IOException {
		String sourceDir = config.getSourceDirectory();
		
		processFiles(sourceDir);

		Results results = new Results(0, processedCount, errorCount, duplicatesDeleted);
		return results;
	}
	
	private void processFiles(String sourceDir) throws IOException {
		Files.walk(Paths.get(sourceDir)).filter(Files::isRegularFile) // filters out directories, leaving just files
				.forEach(p -> processFile(p));
	}
	
	private void processFile(Path file) {
		//System.out.println("[PROCESSING] " + file.toString());
		renameFile(file);
		processedCount++;
	}
	
	private void renameFile(Path path) {
		boolean fileNameMatches = path.getFileName().toString().contains(config.getRenameFilesConfig().getReplace());
		if (fileNameMatches) {
			String newName = path.toString().replaceAll(config.getRenameFilesConfig().getReplace(), config.getRenameFilesConfig().getReplaceWith());
			System.out.println("[MATCH] source file contains replacement string: " + path.toString() + "...");
			System.out.println("[REPLACE] new name is: " + newName);
			
			if (!config.isTestMode()) {
				path.toFile().renameTo(new File(newName));
				duplicatesDeleted++;
			} else duplicatesDeleted++;
		}
	}

}
