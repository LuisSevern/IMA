package main.com.ima.dev.dto;

import java.io.File;
import java.io.IOException;

public class LoggingPath {
	private static String lpath;

	public static String getLpath() {
		if (LoggingPath.lpath == null) {
			setLpath(null);
		}
		return lpath;
	}

	public static void setLpath(String lpath) {
		if( (lpath == null || lpath.trim().isEmpty()) && (LoggingPath.lpath == null|| LoggingPath.lpath.trim().isEmpty()) )  {
			File f = new File("/logs/logs.txt");
			f.getParentFile().mkdirs();
            try {
				f.createNewFile();
				lpath = f.getAbsolutePath();
			} catch (IOException e) {
				e.printStackTrace();
				lpath = null;
			}
		}
		LoggingPath.lpath = lpath;
	}
	
}
