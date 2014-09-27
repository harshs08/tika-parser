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
				"/Users/harshsingh/Documents/Codes/IR/tika-parser/src/main/resources/computrabajo-ar-20121106.tsv");

		parser.parse(new FileInputStream(f), h, metadata, new ParseContext());

		// System.out.println(h.toString());

		String fileName = "/Users/harshsingh/Desktop/run2.xhtml";
		// int count = 1;

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
