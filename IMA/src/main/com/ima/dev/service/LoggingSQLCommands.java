package main.com.ima.dev.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import main.com.ima.dev.dto.LoggingPath;

public class LoggingSQLCommands {
	
	public void writeLog(String user, String msg)
	{
		FileWriter fstream;
		BufferedWriter out;
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		try {
			File file = new File(LoggingPath.getLpath());
			if(file.exists())
			{
				fstream = new FileWriter(LoggingPath.getLpath(),true);
				out = new BufferedWriter(fstream);
				out.write("date: " + dateFormat.format(date) + " "+user+": " + msg);
				out.newLine();
				out.close();
			}else
			{
				file.createNewFile();
			}			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
