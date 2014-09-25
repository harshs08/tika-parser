package com.parse.tika;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

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

		// .getResourceAsStream("/computrabajo-ar-20121106.tsv");
		// parser.parse(content, h, metadata, new ParseContext());

		// System.out.println(h.toString());
		File f = new File(
				"C:\\USCFall2014\\Information Retreival and Search Engines\\HW\\IR_Data\\computrabajo-ar-20121106.tsv");

		parser.parse(new FileInputStream(f), h, metadata, new ParseContext());

		// System.out.println(h.toString());

		String fileName = "C:\\USCFall2014\\Information Retreival and Search Engines\\HW\\Output\\run1.html";
		//int count = 1;

		PrintWriter writer = null;
		try {
			writer = new PrintWriter(fileName, "UTF-8");

			writer.println(h.toString());
		}

		catch (Exception e) {
			// TODO: handle exception
		}

	}
}
