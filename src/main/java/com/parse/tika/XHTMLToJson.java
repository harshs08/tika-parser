package com.parse.tika;

import java.io.OutputStream;

import org.apache.tika.sax.ToTextContentHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class XHTMLToJson extends ToTextContentHandler{
	
	public XHTMLToJson(final OutputStream stream) {
		super(stream);
	}
	
	@Override
	public void startDocument() throws SAXException {
		write("{");
		write("\"data\"");
		write(":{");
	}
	
	protected void write(final String string) throws SAXException {
		super.characters(string.toCharArray(), 0, string.length());
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
		write(",");
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		write("\"");
		super.characters(ch, start, length);
		write("\"");
	}

	@Override
	public void endDocument() throws SAXException {
		write("} }");
		super.endDocument();
	}
}
