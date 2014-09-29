package com.parse.tika;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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
		ToHTMLContentHandler h = new ToHTMLContentHandler();
		// InputStream content = App.class
		// .getResourceAsStream("computrabajo-ar-20121106.tsv");

		File f = new File(
				"/Users/harshsingh/Documents/Codes/cs572/baron.pagemewhen.com/~chris/employment/subset/computrabajo-ar-20121106.tsv");
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		Calendar cal = Calendar.getInstance();
		System.out.println("Parse start time: "+dateFormat.format(cal.getTime()));
		parser.parse(new FileInputStream(f), h, metadata, new ParseContext());
		
		//System.out.println(h.toString());

		String fileName = "/Users/harshsingh/Desktop/run2.html";
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(fileName, "UTF-8");
			writer.println(h.toString());
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
