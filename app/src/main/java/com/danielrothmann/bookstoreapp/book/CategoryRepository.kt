//package com.danielrothmann.bookstoreapp.book
//
//class CategoryRepository {
//    private val _categories = listOf(
//        "Bestsellers",
//        "Detective",
//        "Novels",
//        "Fiction",
//        "Science and Technology",
//        "Educational literature",
//        "For Children",
//        "IT",
//        "Recipes",
//        "Adventures",
//        "Comedy",
//        "Biography"
//    )
//
//    val allCategories: List<String> get() = _categories
//
//    fun searchCategories(query: String): List<String> {
//        return _categories.filter {
//            it.contains(query, ignoreCase = true)
//        }
//    }
//
//    fun getCategoriesCount(): Int = _categories.size
//
//    fun getCategoryAt(index: Int): String? {
//        return if (index in _categories.indices) _categories[index] else null
//    }
//}
