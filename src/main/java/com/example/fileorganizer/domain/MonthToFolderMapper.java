package com.example.fileorganizer.domain;

import java.util.HashMap;
import java.util.Map;

public class MonthToFolderMapper {
	
	private static final String Q1 = "jan-mar";
	private static final String Q2 = "apr-jun";
	private static final String Q3 = "jul-sep";
	private static final String Q4 = "oct-dec";
	
	private static Map<Integer, String> monthFolderMap = new HashMap<Integer, String>();
	
	static {
		monthFolderMap.put(1,  Q1);
		monthFolderMap.put(2,  Q1);
		monthFolderMap.put(3,  Q1);
		monthFolderMap.put(4,  Q2);
		monthFolderMap.put(5,  Q2);
		monthFolderMap.put(6,  Q2);
		monthFolderMap.put(7,  Q3);
		monthFolderMap.put(8,  Q3);
		monthFolderMap.put(9,  Q3);
		monthFolderMap.put(10, Q4);
		monthFolderMap.put(11, Q4);
		monthFolderMap.put(12, Q4);
	}
	
	public static String getFolderName(Integer month) {
		return monthFolderMap.get(month);
	}

}
