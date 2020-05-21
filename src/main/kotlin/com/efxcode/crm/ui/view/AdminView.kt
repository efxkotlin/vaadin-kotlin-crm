package com.efxcode.crm.ui.view

import com.efxcode.crm.AppConfig
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.HasDynamicTitle
import com.vaadin.flow.router.Route
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.context.SecurityContextHolder

@Route("admin")
@Secured("ROLE_admin")
class AdminView(private val appConfig: AppConfig):VerticalLayout(),HasDynamicTitle {

    init {
        add(H1("This is an Admin View"))
    }

    override fun getPageTitle(): String {
        return "${getTranslation("app.adminView")} | ${SecurityContextHolder.getContext().authentication.name}"
    }
}