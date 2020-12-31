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

function postBook() {
    if (entry != null) {
        var books = companyhome.childByNamePath("Books")
        var isbn = entry.getExtension(isbnQName).text
        var book = books.createNode(isbn + ".pdf", "bs:book", [], "cm:contains")
        book.addAspect("cm:generalclassifiable")
        book.addAspect("cm:versionable")
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
        model.book = book
        status.code = 201 // Created
        status.location = url.server + url.serviceContext + "/books/" + isbn + ".atom"
        status.redirect = true
    } else {
        status.code = 400 // Bad Request
        status.message = "Format of request not recognized"
        status.redirect = true
    }
}

postBook()
