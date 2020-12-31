function checkDuplicateReview(book, email) {
    var query = '@bs\\:reviewer_email:"' + email + '" AND PARENT:"' + book.nodeRef + '"'
    logger.log(query)
    var results = search.luceneSearch(query)
    return results.length > 0
}
