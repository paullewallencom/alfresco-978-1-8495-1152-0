<?xml version="1.0"?>
<rss version="2.0">
   <channel>
      <title>Recent Documents</title>
      <link>http://localhost:8080/alfresco/service/recent</link>
      <description>Recently created or modified documents in the Guest Home space.</description>
      <generator>Alfresco ${server.edition?xml} ${server.version?xml}</generator>
      <#list results as item>
         <item>
            <title>${item.properties["cm:name"]?xml}</title>
            <link>${absurl(url.context + item.url)?xml}</link>
            <#if item.properties["cm:description"]??>
               <description>${item.properties["cm:description"]?xml}</description>
            </#if>
            <pubDate>${item.properties["cm:modified"]?datetime}</pubDate>
            <guid>${item.id?xml}</guid>
         </item>
      </#list>
</rss>
