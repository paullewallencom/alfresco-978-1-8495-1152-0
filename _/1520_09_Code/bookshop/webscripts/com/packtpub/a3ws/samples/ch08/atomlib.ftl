<#macro optelem elemname value="">
  <#if value != "">
  <${elemname}>${value?xml}</${elemname}>
  </#if>
</#macro>

<#macro bookToAtomEntry book>
<entry
    xmlns="http://www.w3.org/2005/Atom"
    xmlns:app="http://www.w3.org/2007/app"
    xmlns:bs="http://www.packtpub.com/a3ws/samples/bookshop">
   <id>${book.id}</id>
   <link rel="self"
      href="${absurl(url.serviceContext + '/books/' + book.properties['bs:isbn'])}"/>
   <#if book.mimetype??>
     <link rel="alternate" type="${book.mimetype?xml}" href="${absurl(url.context + book.url)}"/>
   </#if>
   <link rel="edit-media"
      href="${absurl(url.serviceContext + '/books/' + book.properties['bs:isbn'] + '/content')}"/>
   <updated>${book.properties.modified?string("yyyy-MM-dd'T'HH:mm:ssZ")}</updated>
	<@optelem "title" book.properties.title/>
	<@optelem "summary" book.properties.description/>
	<#list book.properties["bs:author"] as author>
	   <author><name>${author?xml}</name></author>
	</#list>
	<#list book.properties.categories as cat>
	   <category term="${cat.name?xml}" scheme="http://www.packtpub.com/a3ws/bookshop/categories" />
	</#list>
	<bs:isbn>${book.properties["bs:isbn"]?xml}</bs:isbn>
	<bs:publisher>${book.properties["bs:publisher"]?xml}</bs:publisher>
	<bs:price>${book.properties["bs:price"]?xml}</bs:price>
</entry>
</#macro>

<#macro generator>
<generator version="${server.version?xml}">Alfresco (${server.edition?xml})</generator>
</#macro>
