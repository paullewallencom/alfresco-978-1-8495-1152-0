package com.packtpub.a3ws.samples.bookshop.rest.client;

import org.apache.abdera.model.Document;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.protocol.client.AbderaClient;
import org.apache.abdera.protocol.client.ClientResponse;
import org.apache.commons.httpclient.UsernamePasswordCredentials;

public class RESTClient {

    public static void main(String[] args) throws Exception {
        String serviceURL = args[0];
        AbderaClient client = new AbderaClient();
        client.addCredentials(serviceURL, null, null, new UsernamePasswordCredentials("admin", "admin"));
        ClientResponse response = client.get(serviceURL);
        Document<Feed> doc = response.getDocument();
        Feed feed = doc.getRoot();
        for (Entry entry : feed.getEntries()) {
            System.out.println(entry.getTitle());
        }
    }

}
