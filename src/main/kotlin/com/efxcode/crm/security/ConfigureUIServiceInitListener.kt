package com.efxcode.crm.security

import com.efxcode.crm.ui.view.LoginView
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.NotFoundException
import com.vaadin.flow.server.ServiceInitEvent
import com.vaadin.flow.server.VaadinServiceInitListener
import org.springframework.stereotype.Component

@Component
class ConfigureUIServiceInitListener : VaadinServiceInitListener {

    override fun serviceInit(event: ServiceInitEvent?) {
        event?.source?.addUIInitListener {
            val ui = it.ui
            ui.addBeforeEnterListener(this::beforeEnter)
        }
    }

    private fun authenticateNavigation(event:BeforeEnterEvent) {
        if(event.navigationTarget != LoginView::class.java  && !isUserLoggedIn()) {
            event.rerouteTo(LoginView::class.java)
        }
    }

    private fun beforeEnter(event: BeforeEnterEvent) {
        if(!isAccessGranted(event.navigationTarget)) {
            when {
                isUserLoggedIn() -> event.rerouteToError(NotFoundException::class.java)
                else -> event.rerouteTo(LoginView::class.java)
            }
        }
    }
}