package com.efxcode.crm.security

import com.vaadin.flow.server.ServletHelper
import com.vaadin.flow.shared.ApplicationConstants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.security.access.annotation.Secured
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.util.matcher.RequestMatcher
import javax.servlet.http.HttpServletRequest


@EnableWebSecurity
@Configuration
class SecurityConfiguration : WebSecurityConfigurerAdapter() {
    companion object {
        const val LOGIN_PROCESSING_URL = "/login"
        const val LOGIN_FAILURE_URL = "/login?error"
        const val LOGIN_URL = "/login"
        const val LOGOUT_SUCCESS_URL = "/login"

    }

    @Autowired lateinit var requestCache:CustomRequestCache

    override fun configure(http: HttpSecurity) {
        http.csrf()
                .disable()
                .requestCache()
                .requestCache(requestCache)
                .and().authorizeRequests()
                .requestMatchers(RequestMatcher(HttpServletRequest::isFrameworkInternalRequest)).permitAll()
                .anyRequest().authenticated()
                .and().formLogin()
                .loginPage(LOGIN_URL).permitAll()
                .loginProcessingUrl(LOGIN_PROCESSING_URL)
                .failureUrl(LOGIN_FAILURE_URL)
                .and().logout()
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl(LOGOUT_SUCCESS_URL)
    }

    @Bean
    override fun userDetailsService(): UserDetailsService {
        val user = User.withUsername("Kotlin").password("{noop}Java")
                .roles("user").build()
        val admin = User.withUsername("Admin").password("{noop}Admin").roles("admin").build()
        return InMemoryUserDetailsManager(user,admin)
    }

    override fun configure(web: WebSecurity?) {
        web?.ignoring()?.antMatchers(
                "/VAADIN/**", "/favicon.ico", "/robots.txt", "/manifest.webmanifest",
                "/sw.js", "/offline.html", "/icons/**", "/images/**", "/styles/**", "/h2-console/**"
        )
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Bean
    fun requestCache():CustomRequestCache {
        return CustomRequestCache()
    }

}

fun HttpServletRequest.isFrameworkInternalRequest(): Boolean {
    val paramValue: String? = this.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER)
    val requestTypes = ServletHelper.RequestType.values()
    return requestTypes.asSequence().any {
        it.identifier == paramValue
    }
}

fun isUserLoggedIn(): Boolean {
    val authentication: Authentication? = SecurityContextHolder.getContext().authentication
    return authentication != null && (authentication !is AnonymousAuthenticationToken) && authentication.isAuthenticated
}

fun isAccessGranted(securedClass: Class<*>):Boolean {
    val secured = AnnotationUtils.findAnnotation(securedClass, Secured::class.java) ?: return true
    val allowedRoles:Array<String> = secured.value
    val authentication = SecurityContextHolder.getContext().authentication
    return authentication.authorities.asSequence().map(GrantedAuthority::getAuthority).any(allowedRoles::contains)
}