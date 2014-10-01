package com.parse.tika;

import java.io.OutputStream;

import org.apache.tika.sax.ToTextContentHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ToJsonContentHandler extends ToTextContentHandler {

	public ToJsonContentHandler(OutputStream stream) {
		super(stream);
	}

	protected void write(final String string) throws SAXException {
		characters(string.toCharArray(), 0, string.length());
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		write("\"");
		super.characters(ch, start, length);
		write("\"");
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		characters(ch, start, length);
	}

	@Override
	public void startDocument() throws SAXException {
		write("{");
		write("\"data\"");
		write(":{");
	}

	@Override
	public void endDocument() throws SAXException {
		write("} }");
		super.endDocument();
	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes atts) throws SAXException {
		super.startElement(null, name, name, null);
		write("\"");
		write(atts.getValue("id"));
		write("\":");

	}

	public void endElement(String uri, String localName, String name)
			throws SAXException {
		super.endElement(null, name, name);

	}
}
