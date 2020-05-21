package com.efxcode.crm.search

class SearchData(
        val name: String?,
        val email: String?
) {

    fun isEmpty(): Boolean {
        return name.isNullOrEmpty() && email.isNullOrEmpty()
    }
}