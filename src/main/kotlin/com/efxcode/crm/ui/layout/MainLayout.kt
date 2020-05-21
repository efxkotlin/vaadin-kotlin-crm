package com.efxcode.crm.ui.layout

import com.efxcode.crm.AppConfig
import com.efxcode.crm.ui.view.DashboardView
import com.efxcode.crm.ui.view.ListView
import com.efxcode.crm.ui.view.ProfileView
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.HighlightConditions
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.RouterLayout
import com.vaadin.flow.router.RouterLink


@CssImport("./styles/shared-styles.css")
@Route
class MainLayout(private val appConfig: AppConfig):AppLayout(){

    init {
        createHeader()
        createDrawer()
    }

    private fun createHeader() {
        val logo = H1(getTranslation("app.title"))
        logo.addClassName("logo")

        val header = HorizontalLayout(DrawerToggle(),logo, Anchor("/logout",getTranslation("link.logout")))
        with(header) {
            defaultVerticalComponentAlignment = FlexComponent.Alignment.CENTER
            width = "100%"
            addClassName("header")
            expand(logo)
        }
        addToNavbar(header)
    }

    private fun createDrawer() {
        val listLink = RouterLink("List", ListView::class.java)
        listLink.highlightCondition = HighlightConditions.sameLocation()
        addToDrawer(VerticalLayout(
                listLink,
                RouterLink("Dashboard", DashboardView::class.java),
                RouterLink("Profile", ProfileView::class.java)
        ))
    }

}