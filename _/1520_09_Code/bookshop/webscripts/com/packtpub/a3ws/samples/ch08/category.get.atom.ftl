<#include "atomlib.ftl">
<?xml version="1.0" ?>

<feed
    xmlns="http://www.w3.org/2005/Atom"
    xmlns:app="http://www.w3.org/2007/app"
    xmlns:bs="http://www.packtpub.com/a3ws/samples/bookshop">
   <title>${categoryDesc?xml}</title>
   <link href="${absurl(url.full)?xml}"/>
   <updated>${updated?string("yyyy-MM-dd'T'HH:mm:ssZ")}</updated>
   <id>${absurl(url.full)?xml}</id>
   <@generator/>
   <#list books as book>
      <@bookToAtomEntry book />
   </#list>
</feed>
