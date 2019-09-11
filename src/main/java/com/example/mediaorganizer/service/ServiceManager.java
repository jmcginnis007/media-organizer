package com.example.mediaorganizer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mediaorganizer.config.Config;
import com.example.mediaorganizer.domain.Modes;
import com.example.mediaorganizer.domain.Results;

@Service
public class ServiceManager {

	@Autowired
	private Config config;

	@Autowired
	private QuarterlyFolderService quarterlyFolderService;

	@Autowired
	private DuplicateHunter duplicateHunter;
	
	@Autowired
	private RenameFilesService renameFileService;

	public void process() {
		Results results = null;

		try {
			switch (config.getMode()) {
				case DUPLICATE_HUNTER: {
					results = duplicateHunter.organize();
					break;
				}
				case TO_QUARTERLY_FOLDERS: {
					results = quarterlyFolderService.organize();
					break;
				}
				case RENAME_FILES: {
					results = renameFileService.organize();
					break;
				}
				default: {
					System.out.println("[);" + "[ERROR] Invalid mode specified in Config:  " + config.getMode());
					System.out.println("acceptable values: " + Modes.values());
				}
			}

			// TODO: here we should call a service that knows how to output the results
			// (could vary depending on the function)
			// for now, just toString() it...
			System.out.println(results);

		} catch (Exception e) {
			System.out.println("An unexpected error occurred.  Here are the details:");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

}
