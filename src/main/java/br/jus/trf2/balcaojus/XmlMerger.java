package br.jus.trf2.balcaojus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

public class XmlMerger {
	private final XMLInputFactory factory = XMLInputFactory.newInstance();

	public void parse(InputStream[] ais) throws IOException, XMLStreamException {
		XMLOutputFactory xmlof = XMLOutputFactory.newInstance();
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			XMLEventWriter xmlw = xmlof.createXMLEventWriter(baos);
			XMLEventFactory eventFactory = XMLEventFactory.newInstance();
			XMLEvent newLine = eventFactory.createDTD("\n");
			xmlw.add(eventFactory.createStartDocument());
			xmlw.add(newLine);

			for (InputStream is : ais) {
				final XMLEventReader reader = factory.createXMLEventReader(is);
				while (reader.hasNext()) {
					final XMLEvent event = reader.nextEvent();
					if (event.isStartElement())
						xmlw.add(event);
					if (event.isEndElement())
						xmlw.add(event);
				}
			}
			xmlw.add(newLine);
			xmlw.add(eventFactory.createEndDocument());
			xmlw.close();
		}
	}
}
