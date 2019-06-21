package com.dicoding.naufal.mtoshokan.utils

object ConstantValue {
    class Database {
        companion object {
            const val Books: String = "books"
            const val BookTypes: String = "bookTypes"
            const val Collections: String = "collections"
            const val SearchHistories: String = "searchHistories"
            const val BorrowingBooks: String = "borrowingBooks"
            const val Users: String = "users"
        }
    }

    class Storage {
        companion object{
            const val UserPhoto: String = "userPhoto"
            const val BookCover: String = "bookCover"
        }
    }

    class ApiKey {
        companion object {
            const val AlgoliaApplicationId = "Q2TLQ722G5"
            const val AlgoliaApiKey = "80f36a15cb4c4783b5276d99070f8ceb"
        }
    }
}