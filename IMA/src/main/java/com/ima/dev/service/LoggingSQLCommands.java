package main.java.com.ima.dev.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import main.java.com.ima.dev.dto.LoggingPath;

public class LoggingSQLCommands {

	public void writeLog(String user, String msg)  {
		FileWriter fstream = null;
		BufferedWriter out = null;
		final String formatDate = "yyyy/MM/dd HH:mm:ss";
		DateFormat dateFormat = new SimpleDateFormat(formatDate);
		Date date = new Date();
		try {
			File file = new File(LoggingPath.getLpath());
			if (file.exists()) {
				fstream = new FileWriter(LoggingPath.getLpath(), true);
				out = new BufferedWriter(fstream);
				out.write("date: " + dateFormat.format(date) + " " + user + ": " + msg);
				out.newLine();
				out.close();
			} else {
				boolean created = file.createNewFile();
				System.out.println(created);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
				}
			}
			if (fstream != null) {
				try {
					fstream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
				}
			}
		}
	}

}
