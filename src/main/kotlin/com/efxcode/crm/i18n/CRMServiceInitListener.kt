package com.efxcode.crm.i18n

import com.vaadin.flow.server.ServiceInitEvent
import com.vaadin.flow.server.VaadinServiceInitListener
import java.lang.System.setProperty

class CRMServiceInitListener:VaadinServiceInitListener {

    override fun serviceInit(event: ServiceInitEvent?) {
        setProperty("vaadin.i18n.provider", CRMi18nProvider::class.java.name)
    }
}