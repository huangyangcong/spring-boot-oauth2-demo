package com.luangeng.oauthclient.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
public class PhotoServiceImpl implements PhotoService {

    @Value("${photoListURL}")
    private String photoListURL;

    @Value("${photoURLPattern}")
    private String photoURLPattern;

    @Value("${trustedMessageURL}")
    private String trustedMessageURL;

    @Autowired
    private RestOperations photoRestTemplate;

    public List<String> getPhotoIds() {
        try {
            byte[] data = photoRestTemplate.getForObject(URI.create(photoListURL), byte[].class);
            InputStream photosXML = new ByteArrayInputStream(data);

            final List<String> photoIds = new ArrayList<String>();
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            parserFactory.setValidating(false);
            parserFactory.setXIncludeAware(false);
            parserFactory.setNamespaceAware(false);
            SAXParser parser = parserFactory.newSAXParser();
            parser.parse(photosXML, new DefaultHandler() {
                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes)
                        throws SAXException {
                    if ("photo".equals(qName)) {
                        photoIds.add(attributes.getValue("id"));
                    }
                }
            });
            return photoIds;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } catch (SAXException e) {
            throw new IllegalStateException(e);
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }

    public InputStream loadPhoto(String id) {
        byte[] data = photoRestTemplate.getForObject(URI.create(String.format(photoURLPattern, id)), byte[].class);
        return new ByteArrayInputStream(data);
    }

    public void setPhotoRestTemplate(RestOperations photoRestTemplate) {
        this.photoRestTemplate = photoRestTemplate;
    }


}
