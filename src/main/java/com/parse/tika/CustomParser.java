package com.parse.tika;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Set;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.txt.CharsetDetector;
import org.apache.tika.parser.txt.CharsetMatch;
import org.apache.tika.sax.XHTMLContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * Plain text parser. The text encoding of the document stream is
 * automatically detected based on the byte patterns found at the
 * beginning of the stream. The input metadata key
 * {@link org.apache.tika.metadata.HttpHeaders#CONTENT_ENCODING} is used
 * as an encoding hint if the automatic encoding detection fails.
 * <p>
 * This parser sets the following output metadata entries:
 * <dl>
 *   <dt>{@link org.apache.tika.metadata.HttpHeaders#CONTENT_TYPE}</dt>
 *   <dd><code>text/plain</code></dd>
 *   <dt>{@link org.apache.tika.metadata.HttpHeaders#CONTENT_ENCODING}</dt>
 *   <dd>The detected text encoding of the document.</dd>
 *   <dt>
 *     {@link org.apache.tika.metadata.HttpHeaders#CONTENT_LANGUAGE} and
 *     {@link org.apache.tika.metadata.DublinCore#LANGUAGE}
 *   </dt>

 * </dl>
 */
@SuppressWarnings("serial")
public class CustomParser implements Parser {


	private static final Set<MediaType> SUPPORTED_TYPES = Collections
			.singleton(MediaType.TEXT_PLAIN);

	public Set<MediaType> getSupportedTypes(ParseContext context) {
		return SUPPORTED_TYPES;
	}

	public String header = "postedDate	location	department	title	salary	start	duration	jobtype	applications	company	contactPerson	phoneNumber	faxNumber:	location	latitude	longitude	firstSeenDate	url	lastSeenDate";

	public void parse(InputStream stream, ContentHandler handler,
			Metadata metadata, ParseContext context) throws IOException,
			SAXException, TikaException {

		// CharsetDetector expects a stream to support marks
		if (!stream.markSupported()) {
			stream = new BufferedInputStream(stream);
		}

		// Detect the content encoding (the stream is reset to the beginning)
		CharsetDetector detector = new CharsetDetector();
		String incomingCharset = metadata.get(Metadata.CONTENT_ENCODING);
		String incomingType = metadata.get(Metadata.CONTENT_TYPE);
		if (incomingCharset == null && incomingType != null) {
			// TIKA-341: Use charset in content-type
			MediaType mt = MediaType.parse(incomingType);
			if (mt != null) {
				incomingCharset = mt.getParameters().get("charset");
			}
		}

		if (incomingCharset != null) {
			detector.setDeclaredEncoding(incomingCharset);
		}

		detector.setText(stream);
		for (CharsetMatch match : detector.detectAll()) {
			if (Charset.isSupported(match.getName())) {
				metadata.set(Metadata.CONTENT_ENCODING, match.getName());
				break;
			}
		}

		String encoding = metadata.get(Metadata.CONTENT_ENCODING);
		if (encoding == null) {
			throw new TikaException(
					"Text encoding could not be detected and no encoding"
							+ " hint is available in document metadata");
		}

		// TIKA-341: Only stomp on content-type after we're done trying to
		// use it to guess at the charset.
		metadata.set(Metadata.CONTENT_TYPE, "text/plain");

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					stream, encoding));

			// TIKA-240: Drop the BOM when extracting plain text
			reader.mark(1);
			int bom = reader.read();
			if (bom != '\ufeff') { // zero-width no-break space
				reader.reset();
			}

			XHTMLContentHandler xhtml = new XHTMLContentHandler(handler,
					metadata);
			xhtml.startDocument();
			xhtml.startElement("table");
			int count = 0;
			processLine(xhtml, header, "" + count++);
			for (String line = reader.readLine(); line != null; line = reader
					.readLine()) {
				xhtml.startElement("tr");
				processLine(xhtml, line, "" + count++);
				xhtml.endElement("tr");
			}
			xhtml.endElement("table");

			xhtml.endDocument();
		} catch (UnsupportedEncodingException e) {
			throw new TikaException("Unsupported text encoding: " + encoding, e);
		}
	}

	private void processLine(XHTMLContentHandler xhtml, String line,
	/* parameter for line num */String count) {
		String[] params = line.split("\\t+");
		String[] withoutMissingEntriesArray = new String[19];
		
		if (params.length < 19) {
			boolean postedDateMissing = true, phoneNumMissing = true, faxNumMissing = true, latMissing = true, longMissing = true, firstSeenDateMissing = true, urlMissing = true, lastSeenDateMissing = true;
			boolean textOnly = true;
			int counter = 0;
			ArrayList<String> withoutMissingEntries = new ArrayList<String>();
			int i = 0;
			if (postedDateMissing)
				withoutMissingEntries.add("N/A");
			else {
				withoutMissingEntries.add(params[counter++]);
				i++;
			}

			for (; i < 11; i++) {
				if (textOnly)
					withoutMissingEntries.add(i, params[counter++]);
				else 
					break;
			}
			for (; i < 11; i++) {
				withoutMissingEntries.add("N/A");
			}

			if (phoneNumMissing) 
				withoutMissingEntries.add("N/A");
			else
				withoutMissingEntries.add(i++, params[counter++]);
			
			if (faxNumMissing)
				withoutMissingEntries.add("N/A");
			else
				withoutMissingEntries.add(i++, params[counter++]);
			
			if (textOnly)
				withoutMissingEntries.add(i++, params[counter++]);
			else
				withoutMissingEntries.add("N/A");

			if (latMissing)
				withoutMissingEntries.add("N/A");
			else
				withoutMissingEntries.add(i++, params[counter++]);

			if (longMissing)
				withoutMissingEntries.add("N/A");
			else
				withoutMissingEntries.add(i++, params[counter++]);
			
			if (firstSeenDateMissing)
				withoutMissingEntries.add("N/A");
			else
				withoutMissingEntries.add(i++, params[counter++]);

			if (urlMissing)
				withoutMissingEntries.add("N/A");
			else
				withoutMissingEntries.add(i++, params[counter++]);
			
			if (lastSeenDateMissing)
				withoutMissingEntries.add("N/A");
			else
				withoutMissingEntries.add(i++, params[counter++]);
			
			
			withoutMissingEntries.toArray(withoutMissingEntriesArray);
		}
		else
			withoutMissingEntriesArray = params;
		
		char[] buffer = null;
		/* line numbers start */
		buffer = count.toCharArray();
		try {
			xhtml.startElement("td");
			xhtml.characters(buffer, 0, buffer.length);
			xhtml.endElement("td");
		} catch (Exception e) {

		}/* line numbers end */
		try {
			for (int i = 0; i < 19; i++) {
				buffer = withoutMissingEntriesArray[i].toCharArray();
				xhtml.startElement("td");
				xhtml.characters(buffer, 0, buffer.length);
				xhtml.endElement("td");
			}
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void parse(InputStream stream, ContentHandler handler,
			Metadata metadata) throws IOException, SAXException, TikaException {
		parse(stream, handler, metadata, new ParseContext());
	}

}
