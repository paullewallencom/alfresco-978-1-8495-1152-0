package com.packtpub.a3ws.samples.bookshop.rest.client;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Random;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.ExtensibleElement;
import org.apache.abdera.protocol.client.AbderaClient;
import org.apache.abdera.protocol.client.ClientResponse;
import org.apache.abdera.protocol.client.RequestOptions;
import org.apache.commons.httpclient.UsernamePasswordCredentials;

public class CreateNewBook {

    public static void main(String[] args) throws Exception {
        String serviceURL = args[0];
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
        // POST entry
        ClientResponse response = client.post(serviceURL, bookEntry);
        // Upload file
        Document<Entry> doc = response.getDocument();
        Entry entry = doc.getRoot();
        String link = entry.getLink("edit-media").getHref().toString();
        InputStream in = new FileInputStream("book.pdf");
        RequestOptions options = new RequestOptions();
        options.setContentType("application/pdf");
        response = client.put(link, in, options);
    }

}
