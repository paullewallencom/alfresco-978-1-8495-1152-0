var category = url.templateArgs.category
var bookType = "{http://www.packtpub.com/a3ws/samples/bookshop}book"
model.books = []
model.categoryDesc = "Unknown category"
model.updated = new Date()
var topcats = classification.getRootCategories("cm:generalclassifiable")
for (var i in topcats) {
    if (topcats[i].name == "Books") {
        var cats = topcats[i].subCategories
        for (var j in cats) {
            if (cats[j].name == category) {
                model.categoryDesc = 
                    cats[j].properties.description
                for (var k in cats[j].categoryMembers) {
                    var b = cats[j].categoryMembers[k]
                    if (b.type == bookType) {
                        model.books.push(b)
                    }
                }
            }
        }
        break;
    }
}
