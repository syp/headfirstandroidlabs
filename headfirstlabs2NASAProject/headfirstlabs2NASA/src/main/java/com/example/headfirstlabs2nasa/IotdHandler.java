package com.example.headfirstlabs2nasa;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.http.HttpConnection;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;

/**
 * Created by stephen on 13-6-27.
 */
public class IotdHandler extends DefaultHandler {
    private String url = "http://www.nasa.gov/rss/image_of_the_day.rss";
    private boolean inTitle = false;
    private boolean inDesc = false;
    private boolean inDate = false;
    private boolean inItem = false;
    private Bitmap image = null;
    private String title = "";
    private String desc = "";
    private String date = "";

    private Bitmap getImage(String imageURL){
        if(imageURL == null){
            return null;
        }
        try{
            HttpURLConnection connection = (HttpURLConnection)new URL(imageURL).openConnection();
            connection.setDoInput(true);
            connection.connect();

            InputStream input = connection.getInputStream();
            Bitmap image = BitmapFactory.decodeStream(input);
            input.close();
            return image;
        }catch (IOException ioe){
            return null;
        }
    }

    public Bitmap getImage(){
        return image;
    }

    public String getDate() {
        return date;
    }

    public String getDesc() {
        return desc;
    }

    public String getTitle() {
        return title;
    }

    public void proceedFeed(){
        try{
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            reader.setContentHandler(this);
            InputStream input = new URL(url).openStream();
            reader.parse(new InputSource(input));
            input.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if("item".equals(localName)){
            inItem = true;
        }else if(inItem){
            if("title".equals(localName)){
                inTitle = true;
            }

            if("description".equals(localName)){
                inDesc = true;
            }

            if("pubDate".equals(localName)){
                inDate = true;
            }

            if("enclosure".equals(localName)){
                String url = attributes.getValue("url");
                image = getImage(url);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if("item".equals(localName)){
            inItem = false;
        }else if(inItem){
            if("title".equals(localName)){
                inTitle = false;
            }

            if("description".equals(localName)){
                inDesc = false;
            }

            if("pubDate".equals(localName)){
                inDate = false;
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String chars = new String(ch, start, length);
        if(inTitle){
            title += chars;
        }else if(inDesc){
            desc += chars;
        }else if(inDate){
            date += chars;
        }
    }


}
