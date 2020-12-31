var BS_NS_URI = "http://www.packtpub.com/a3ws/samples/bookshop"
var BS_NS_PREFIX = "bs"
var isbnQName = new Packages.javax.xml.namespace.QName(BS_NS_URI, "isbn", BS_NS_PREFIX)
var publisherQName = new Packages.javax.xml.namespace.QName(BS_NS_URI, "publisher", BS_NS_PREFIX)
var priceQName = new Packages.javax.xml.namespace.QName(BS_NS_URI, "price", BS_NS_PREFIX)

/*
 * Return all categories under a given category
 */
function getChildCategories(parentName) {
    var topcats = classification.getRootCategories("cm:generalclassifiable")
    for (var i in topcats) {
        if (topcats[i].name == parentName) {
            return topcats[i].subCategories
        }
    }
    return []
}

function findBookCategory(name) {
    var cats = getChildCategories("Books")
    for (var i in cats) {
        if (cats[i].name.equals(name)) {
            return cats[i]
        }
    }
    return null
}

function putBook() {
    if (entry != null) {
        var isbn = url.templateArgs.isbn
        var results = search.luceneSearch("@bs\\:isbn:" + isbn)
        if (results.length <= 0) {
            status.code = 404 // Not Found
            status.message = "No book with ISBN " + isbn + " found."
            status.redirect = true
            return
        }
        var book = results[0]

        var currentVersion = '"' + book.id + ' [' + book.versionHistory[0].label + ']"' 
        var requestedVersion = headers["If-Match"]
        
        logger.log("currentVersion = " + currentVersion)
        logger.log("requestedVersion = " + requestedVersion)

        if (currentVersion != requestedVersion) {
            status.code = 412 // Precondition failed
            status.redirect = true
            return
        }
        
        book.properties.title = entry.title
        book.properties.description = entry.summary
        book.properties["bs:isbn"] = isbn
        
        var publisher = entry.getExtension(publisherQName)
        if (publisher != null) {
            book.properties["bs:publisher"] = publisher.text
        }

        var price = entry.getExtension(priceQName)
        if (price != null) {
            book.properties["bs:price"] = price.text
        }
        
        var authors = []
        for (var i = 0 ; i < entry.authors.size() ; ++i) {
            authors[i] = entry.authors.get(i).name
        }
        book.properties["bs:author"] = authors

        var categories = []
        for (var i = 0 ; i < entry.categories.size() ; ++i) {
            var category = findBookCategory(entry.categories.get(i).term)
            if (category != null) {
                categories.push(category)
            }
        }
        book.properties["cm:categories"] = categories
        book.save()
    } else {
        status.code = 400 // Bad Request
        status.message = "Format of request not recognized"
        status.redirect = true
    }
}

putBook()
