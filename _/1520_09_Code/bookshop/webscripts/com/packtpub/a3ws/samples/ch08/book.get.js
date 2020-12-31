var isbn = url.templateArgs.isbn
var results = search.luceneSearch("@bs\\:isbn:" + isbn)
if (results.length <= 0) {
    status.code = 404 // Not Found
    status.message = "No book with ISBN " + isbn + " found."
    status.redirect = true
} else {
    model.book = results[0]
    var etag = model.book.id
    if (model.book.versionHistory != undefined) {
        etag += " [" + model.book.versionHistory[0].label + "]"
    }
    cache.ETag = etag
}
