package com.efxcode.crm.security

import org.springframework.security.web.savedrequest.HttpSessionRequestCache
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class CustomRequestCache:HttpSessionRequestCache() {

    override fun saveRequest(request: HttpServletRequest?, response: HttpServletResponse?) {
        if(!request!!.isFrameworkInternalRequest()) super.saveRequest(request, response)
    }

}