var isbn = url.templateArgs.isbn
var results = search.luceneSearch("@bs\\:isbn:" + isbn)
if (results.length <= 0) {
    status.code = 404 // Not Found
    status.message = "No book with ISBN " + isbn + " found."
    status.redirect = true
} else {
    if (json != undefined) {
        var book = results[0]
        var id = new Date().getTime()
        var review = book.createNode(id, "bs:review", [], "bs:reviews")
        review.mimetype = "text/plain"
        review.content = json.get("content")
        review.properties["cm:title"] = json.get("title")
        review.properties["bs:reviewer_name"] = json.get("author").get("name")
        review.properties["bs:reviewer_email"] = json.get("author").get("email")
        review.properties["bs:rating"] = json.get("rating")
        review.save()
        status.code = 201 // Created
        status.location = url.server + url.serviceContext + "/books/" + isbn + "/reviews/" + id + ".json";
        status.redirect = true
    } else {
        status.code = 400 // Bad Request
        status.message = "Format of request not recognized"
        status.redirect = true
    }
}
