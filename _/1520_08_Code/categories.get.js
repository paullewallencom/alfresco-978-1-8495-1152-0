var topcats = classification.getRootCategories("cm:generalclassifiable")
for (var i in topcats) {
    if (topcats[i].name == "Books") {
        model.categories = topcats[i].subCategories
        break;
    }
}