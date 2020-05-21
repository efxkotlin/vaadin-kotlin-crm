package com.efxcode.crm.ui.view

import com.efxcode.crm.AppConfig
import com.efxcode.crm.security.CustomRequestCache
import com.vaadin.flow.component.Tag
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.login.LoginOverlay
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.*
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.context.SecurityContextHolder

@Tag("sa-login-view")
@Route("login")
class LoginView(private val appConfig: AppConfig,
                private val authenticationManager: AuthenticationManager,
                private val customRequestCache: CustomRequestCache) : VerticalLayout(),BeforeEnterListener,HasDynamicTitle {

    private val login = LoginOverlay()

    init {
        addClassName("login-view")
        setSizeFull()
        alignItems = FlexComponent.Alignment.CENTER
        justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        with(login) {
            action = "login"
            isOpened = true;
            setTitle(getTranslation("app.title"));
            description = "Full Stack Application powered by Spring Boot";
        }
        add(H1(getTranslation("app.title")), login)
    }

    override fun beforeEnter(event: BeforeEnterEvent?) {
        if (!event?.location?.queryParameters?.parameters?.getOrDefault("error", mutableListOf())?.isEmpty()!!) {
            login.isError = true
        }
    }

    override fun getPageTitle(): String {
        return "${getTranslation("app.loginView")} | ${SecurityContextHolder.getContext().authentication.name}"
    }

}