[
   <#list categories as cat>
		"${jsonUtils.encodeJSONString(cat.name)}"<#if cat_has_next>,</#if>
	</#list>
]