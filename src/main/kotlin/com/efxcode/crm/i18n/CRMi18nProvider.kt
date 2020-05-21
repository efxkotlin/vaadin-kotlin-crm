package com.efxcode.crm.i18n

import com.vaadin.flow.i18n.I18NProvider
import java.util.*

class CRMi18nProvider : I18NProvider {

    companion object {
        private const val RESOURCE_BUNDLE_NAME = "crm_app"
        val RESOURCE_BUNDLE_EN: ResourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME,Locale.ENGLISH)
        val RESOURCE_BUNDLE_FR: ResourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME,Locale.FRENCH)
        val LOCALES = listOf<Locale>(Locale.ENGLISH,Locale.FRENCH)
    }

    override fun getTranslation(key: String, locale: Locale, vararg params: Any): String {
        return when (locale) {
            Locale.FRENCH -> RESOURCE_BUNDLE_FR.getString(key) ?: key
            Locale.ENGLISH -> RESOURCE_BUNDLE_EN.getString(key) ?: key
            else -> key
        }
    }

    override fun getProvidedLocales(): List<Locale> = LOCALES
}