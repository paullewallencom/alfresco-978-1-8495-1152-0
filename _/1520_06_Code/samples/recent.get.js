var results =
    search.luceneSearch(
        'PATH:"/app:company_home/app:guest_home/*"',
        "@cm:modified",
        false)
if (results.length > 0) {
    var top = results[0]
    cache.ETag = top.id
    cache.lastModified = top.properties["cm:modified"]

    // Check if feed needs regenerating
    var etag = headers["If-None-Match"]

    // We must strip off extra quotes    
    if (etag != undefined && etag.replace(/\"/g,"") == top.id) {
        if (headers["If-Modified-Since"] != undefined) {

            // Truncate time values to the second
            var ims = Math.floor(Date.parse(
                    headers["If-Modified-Since"]) / 1000)
            var lastmodified = Math.floor(
                top.properties["cm:modified"].getTime() / 1000)
            if (lastmodified <= ims) {
                status.code = 304; // Not Modified
                status.redirect = true;
            }
        }
    }
}
model.results = results
