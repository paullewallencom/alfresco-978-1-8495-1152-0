<?xml version="1.0" ?>
<app:categories
    xmlns:app="http://www.w3.org/2007/app"
    xmlns:atom="http://www.w3.org/2005/Atom"
    fixed="yes" scheme="http://www.packtpub.com/a3ws/bookshop/categories">
  <#list categories as cat>
     <atom:category term="${cat.name?xml}"/>
  </#list>
</app:categories>
