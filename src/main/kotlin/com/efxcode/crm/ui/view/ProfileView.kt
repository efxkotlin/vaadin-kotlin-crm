package com.efxcode.crm.ui.view

import com.efxcode.crm.AppConfig
import com.efxcode.crm.ui.layout.MainLayout
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.HasDynamicTitle
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import org.springframework.security.core.context.SecurityContextHolder

@Route("profile",layout = MainLayout::class)
class ProfileView(private val appConfig: AppConfig) : VerticalLayout(),HasDynamicTitle  {


    init {
        add(H1(getTranslation("text.profile_data")))
    }

    override fun getPageTitle(): String {
        return "${getTranslation("app.profileView")} | ${SecurityContextHolder.getContext().authentication.name}"
    }

}