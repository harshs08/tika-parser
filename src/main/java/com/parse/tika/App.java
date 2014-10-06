package com.parse.tika;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.ToHTMLContentHandler;
import org.xml.sax.SAXException;

import com.parse.deduplication.Deduplication;

public class App {

	static ArrayList<String> fileNames;

	public static void main(final String[] args) throws IOException,
			SAXException, TikaException {
		//base folder
		final File folder = new File(args[0]);
		fileNames = listFilesForFolder(folder);
		
		//deduplication: 0 - without; 1 - with
		int task = Integer.parseInt(args[1]);
		
		
		
		Deduplication dedup = new Deduplication();
		
		
		
		long totalCount =0;
		StringBuffer output = new StringBuffer();

		switch (task) {
		case 0:
			//without deduplication
			for (int i = 0; i < fileNames.size(); i++) {
				try {

					CustomParser parser = new CustomParser();
					Metadata metadata = new Metadata();
					File f = new File(args[0] + "/" + fileNames.get(i));

					ToHTMLContentHandler h = new ToHTMLContentHandler();
					DateFormat dateFormat = new SimpleDateFormat(
							"yyyy/MM/dd HH:mm:ss.SSS");
					Calendar cal = Calendar.getInstance();
					System.out.println("Parse start time: "
							+ dateFormat.format(cal.getTime()));

					long count = parser.parseWithCount(new FileInputStream(f), h, metadata,
							new ParseContext(),dedup);
					totalCount = totalCount+count;

					DateFormat dateFormat2 = new SimpleDateFormat(
							"yyyy/MM/dd HH:mm:ss.SSS");
					Calendar cal2 = Calendar.getInstance();
					String out = "Parse end time: "
							+ dateFormat2.format(cal2.getTime())
							+" file count for "+fileNames.get(i)+" = "+count+" total files Count "+totalCount+
							" dedup count "+dedup.getCount();
					System.out.println(out);
					output.append(out+"\n");
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			dedup.findNearlyDuplicates();
			System.out.println("fucke me "+dedup.getNearlyDedupCount());
			PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
			writer.println(output);
			writer.close();
			break;
		case 1:
			//with deduplication
			break;
		default:
			System.out.println("Please enter a valid task number");
		}

	}

	public static ArrayList<String> listFilesForFolder(final File folder) {
		fileNames = new ArrayList<String>();
		for (final File fileEntry : folder.listFiles()) {
			fileNames.add(fileEntry.getName());
		}
		return fileNames;
	}
}
