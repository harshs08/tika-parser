package com.parse.tika;

 
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
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
 
    private static final Set<MediaType> SUPPORTED_TYPES =
        Collections.singleton(MediaType.TEXT_PLAIN);
 
    public Set<MediaType> getSupportedTypes(ParseContext context) {
        return SUPPORTED_TYPES;
    }
    
    public String header = "postedDate	location	department	title	salary	start	duration	jobtype	applications	company	contactPerson	phoneNumber	faxNumber:	location	latitude	longitude	firstSeenDate	url	lastSeenDate";
 
    public void parse(
            InputStream stream, ContentHandler handler,
            Metadata metadata, ParseContext context)
    throws IOException, SAXException, TikaException {
 
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
        //metadata.CONVENTIONS.;
        
 
        try {
        	BufferedReader reader =
                new BufferedReader(new InputStreamReader(stream, encoding));
 
            // TIKA-240: Drop the BOM when extracting plain text
            reader.mark(1);
            int bom = reader.read();
            if (bom != '\ufeff') { // zero-width no-break space
                reader.reset();
            }
 
            System.out.println(metadata.toString());
            XHTMLContentHandler xhtml =
                new XHTMLContentHandler(handler, metadata);
           
            xhtml.startDocument();
            
            
            xhtml.startElement("table");
            /*
            System.out.println(handler.toString());
            System.out.println("****");
            String s = handler.toString();
            String sArr[] = s.split("<meta");
            String newS = sArr[0] + "<meta"+sArr[1] + "</meta>";
            String sub1 = sArr[2].substring(0, sArr[2].indexOf(">")+1);
            newS += "<meta"+sub1 + "</meta>" + sArr[2].substring(sArr[2].indexOf(">")+1);
            System.out.println(newS);
            handler.;
            */
            //System.out.println(handler.toString() +"bhag");
            processLine(xhtml,header);
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {               
                xhtml.startElement("tr");
                processLine(xhtml,line);
                xhtml.endElement("tr");                
             }            
            xhtml.endElement("table");
 
            xhtml.endDocument();
        } catch (UnsupportedEncodingException e) {
            throw new TikaException(
                    "Unsupported text encoding: " + encoding, e);
        }
    }
 
    private void processLine(XHTMLContentHandler xhtml, String line) {
    	String[] params = line.split("\\t+");
    	char[] buffer = null;

        try {
        	for (int i = 0; i < params.length; i++) {
        		 buffer = params[i].toCharArray();
        		 xhtml.startElement("th");
     			 xhtml.characters(buffer, 0, buffer.length);
                 xhtml.endElement("th"); 
			}
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
     * @deprecated This method will be removed in Apache Tika 1.0.
     */
    public void parse(
            InputStream stream, ContentHandler handler, Metadata metadata)
    throws IOException, SAXException, TikaException {
        parse(stream, handler, metadata, new ParseContext());
    }
 
}
