package com.parse.tika;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.ToHTMLContentHandler;
import org.xml.sax.SAXException;

public class App {
	public static void main(final String[] args) throws IOException,
			SAXException, TikaException {
		Parser parser = new CustomParser();
		Metadata metadata = new Metadata();
		String fileName = "/Users/harshsingh/Desktop/run2.html";
		File f = new File("/tika-parser/src/main/resources/input/computrabajo-ar-20121106.tsv");
		
		ToHTMLContentHandler h = new ToHTMLContentHandler(new FileOutputStream(fileName), "UTF-8");
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		Calendar cal = Calendar.getInstance();
		System.out.println("Parse start time: "+dateFormat.format(cal.getTime()));
		parser.parse(new FileInputStream(f), h, metadata, new ParseContext());
		
		//System.out.println(h.toString());

		
		//PrintWriter writer = null;
		try {
			//FileWriter fw = new FileWriter(fileName);
			//writer = new PrintWriter(fileName, "UTF-8");
			//BufferedWriter bw = new BufferedWriter(fw);
			//bw.write(h.toString());
			//bw.close();
			//writer.println(h.toString());
			DateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
			Calendar cal2 = Calendar.getInstance();
			System.out.println("Parse end time: "+dateFormat2.format(cal2.getTime()));
		}

		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
}
