package com.example.mediaorganizer.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.example.mediaorganizer.domain.DateSources;

public class DateUtils {

	public static LocalDate getFileSystemDate(Path path, DateSources dateType) {

		BasicFileAttributes attributes;
		LocalDate localDate = null;

		try {
			attributes = Files.readAttributes(path, BasicFileAttributes.class);
			FileTime fileTime = (dateType.equals(DateSources.FILE_MODIFICATION) ? attributes.lastModifiedTime()
					: attributes.creationTime());

			String pattern = "yyyy-MM-dd";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			String creationDate = simpleDateFormat.format(new Date(fileTime.toMillis()));

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
			localDate = LocalDate.parse(creationDate, formatter);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return localDate;

	}

}
