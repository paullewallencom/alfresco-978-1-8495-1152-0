<import resource="classpath:alfresco/extension/templates/webscripts/com/packtpub/a3ws/samples/ch07/bookshop/reviewLib.js">

function postReview() {
    var isbn = url.templateArgs.isbn
    var results = search.luceneSearch("@bs\\:isbn:" + isbn)
    if (results.length <= 0) {
        status.code = 404 // Not Found
        status.message = "No book with ISBN " + isbn + " found."
        status.redirect = true
        return
    }
    if (entry != null) {
        var email = entry.author.email
        var book = results[0]
        if (checkDuplicateReview(book, email)) {
            status.code = 400 // Bad Request
            status.message = "Attempt to post a duplicate review detected"
            status.redirect = true
            return
        }
        var id = new Date().getTime()
        var review = book.createNode(id, "bs:review", [], "bs:reviews")
        review.mimetype = "text/plain"
        review.content = entry.content
        review.properties["cm:title"] = entry.title
        review.properties["bs:reviewer_name"] = entry.author.name
        review.properties["bs:reviewer_email"] = email
        var ratingQName = new Packages.javax.xml.namespace.QName("http://www.packtpub.com/a3ws/samples/bookshop", "rating", "bs")
        var rating = entry.getExtension(ratingQName);
        if (rating != null) {
            review.properties["bs:rating"] = rating.text
        }
        review.save()
        status.code = 201 // Created
        status.location = url.server + url.serviceContext + "/books/" + isbn + "/reviews/" + id + ".atom";
        status.redirect = true
    } else {
        status.code = 400 // Bad Request
        status.message = "Format of request not recognized"
        status.redirect = true
    }
}

postReview()
