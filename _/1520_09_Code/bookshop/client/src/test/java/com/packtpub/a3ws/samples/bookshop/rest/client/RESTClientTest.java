package com.packtpub.a3ws.samples.bookshop.rest.client;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Random;

import javax.xml.namespace.QName;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Element;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.ExtensibleElement;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;
import org.apache.abdera.protocol.client.AbderaClient;
import org.apache.abdera.protocol.client.ClientResponse;
import org.apache.abdera.protocol.client.RequestOptions;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class RESTClientTest {

    static final String serviceURL = "http://localhost:8080/alfresco/service/books/";

    private static final Log log = LogFactory.getLog(RESTClientTest.class);
    
    @Test
    public void testListCategory() throws Exception {
        AbderaClient client = new AbderaClient();
        client.addCredentials(serviceURL, null, null, new UsernamePasswordCredentials("admin", "admin"));
        ClientResponse response = client.get(serviceURL + "category/fiction");
        Document<Feed> doc = response.getDocument();
        Feed feed = doc.getRoot();
        for (Entry entry : feed.getEntries()) {
            System.out.println(entry.getTitle());
        }
        response.release();
    }

    @Test
    public void testCreateNewBook() throws Exception {
        final String BOOKSHOP_NS_URI = "http://www.packtpub.com/a3ws/samples/bookshop"; 
        Abdera abdera = new Abdera();
        AbderaClient client = new AbderaClient(abdera);
        client.addCredentials(serviceURL, null, null, new UsernamePasswordCredentials("admin", "admin"));
        // Generate random ISBN
        int rnd = new Random().nextInt(1000000000);
        String rnds = String.format("%010d", rnd);
        String isbn = "999-" + rnds;
        // Set up entry 
        Entry bookEntry = abdera.newEntry();
        bookEntry.setId("urn:isbn:" + isbn);
        bookEntry.setUpdated(new Date());
        bookEntry.setTitle("Book " + isbn);
        bookEntry.addAuthor("Anonymous");
        bookEntry.addCategory(BOOKSHOP_NS_URI + "/categories" , "fiction", null);
        ExtensibleElement e1 = bookEntry.addExtension(BOOKSHOP_NS_URI, "isbn", "bs");
        e1.setText(isbn);
        ExtensibleElement e2 = bookEntry.addExtension(BOOKSHOP_NS_URI, "publisher", "bs");
        e2.setText("PACKT");
        ExtensibleElement e3 = bookEntry.addExtension(BOOKSHOP_NS_URI, "price", "bs");
        e3.setText("12.99");
        // POST entry
        ClientResponse response = client.post(serviceURL, bookEntry);
        response.release();
        assertEquals(201, response.getStatus());
        // Upload file
        Document<Entry> doc = response.getDocument();
        Entry entry = doc.getRoot();
        String link = entry.getLink("edit-media").getHref().toString();
        InputStream in = new FileInputStream("book.pdf");
        RequestOptions options = new RequestOptions();
        options.setContentType("application/pdf");
        response = client.put(link, in, options);
        response.release();
        assertEquals(200, response.getStatus());
    }
    

    @Test
    public void testUpdateBook() throws Exception {
        final String BOOKSHOP_NS_URI = "http://www.packtpub.com/a3ws/samples/bookshop";
        final QName BOOKSHOP_PRICE_QNAME = new QName(BOOKSHOP_NS_URI, "price");
        
        Abdera abdera = new Abdera();
        AbderaClient client = new AbderaClient(abdera);
        client.addCredentials(serviceURL, null, null, new UsernamePasswordCredentials("admin", "admin"));
        
        ClientResponse response = client.get(serviceURL + "category/fiction");
        Document<Feed> doc = response.getDocument();
        Feed feed = doc.getRoot();
        Entry entry = feed.getEntries().get(0);
        response.release();
        
        response = client.get(entry.getSelfLink().getHref().toString());
        Document<Entry> entryDoc = response.getDocument(); 
        entry = entryDoc.getRoot();
        String etag = response.getHeader("ETag");
        
        Element priceEl = entry.getExtension(BOOKSHOP_PRICE_QNAME);
        priceEl.setText("15.99");

        Link editLink = entry.getEditLink();
        if (editLink == null) {
            editLink = entry.getSelfLink();
        }
        response.release();

        RequestOptions options = client.getDefaultRequestOptions();
        options.setHeader("If-Match", etag);
        response = client.put(editLink.getHref().toString(), entry, options);
        response.release();
        assertEquals("200 OK expected", 200, response.getStatus());

        options = client.getDefaultRequestOptions();
        options.setHeader("If-Match", etag);
        response = client.put(editLink.getHref().toString(), entry, options);
        response.release();
        assertEquals("412 Precondition failed expected", 412, response.getStatus());
    }

}
