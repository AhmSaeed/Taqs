package com.iti.mad41.taqs.data.source.preferences

interface PreferencesDataSource {
    fun saveSelectedAccessLocationType(type: String)
    fun getSelectedAccessLocationType(default: String): String?

    fun saveSelectedLanguage(lang: String)
    fun getSelectedLanguage(default: String): String?
}