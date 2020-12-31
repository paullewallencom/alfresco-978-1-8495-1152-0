var isbn = url.templateArgs.isbn
var results = search.luceneSearch("@bs\\:isbn:" + isbn)
if (results.length <= 0) {
    status.code = 404 // Not Found
    status.message = "No book with ISBN " + isbn + " found."
    status.redirect = true
} else {
    var book = results[0]
    book.properties.content.mimetype = requestbody.mimetype
    book.properties.content.write(requestbody)
}
