package com.efxcode.crm

import com.efxcode.crm.model.entity.Company
import com.efxcode.crm.model.entity.Contact
import com.efxcode.crm.model.repository.CompanyRepository
import com.efxcode.crm.model.repository.ContactRepository
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.Ordered
import org.springframework.stereotype.Component
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.annotation.PostConstruct
import kotlin.properties.Delegates
import kotlin.random.Random

@SpringBootApplication(exclude = [ErrorMvcAutoConfiguration::class])
class EntryPoint: SpringBootServletInitializer()

@Configuration
@PropertySource(value = ["classpath:application.properties"])
@ConfigurationProperties(prefix = "crm.app")
class AppConfig {

}

@EnableWebMvc
@Component
class WebConfig:WebMvcConfigurer {

    override fun addViewControllers(registry: ViewControllerRegistry) {
       registry.addViewController("/login").setViewName("login")
       registry.setOrder(Ordered.HIGHEST_PRECEDENCE)
    }
}


fun main() {
    SpringApplication.run(EntryPoint::class.java)
}

@Component
class Initializer(val companyRepository: CompanyRepository,val contactRepository: ContactRepository) {

    @PostConstruct
    fun populate() {
        if(companyRepository.count() == 0L) {
            companyRepository.saveAll(
                 sequenceOf("efxCode","Microsoft","RedHat","Oracle")
                         .map(::Company)
                         .toList()
            )
        }

        val companies = companyRepository.findAll().toList()
        val randomCompanyIndicator = Random(0)
        contactRepository.saveAll(
        sequenceOf("Jack Nicholson", "Marlon Brando","Al Pacino","John Travolta",
                "Donald Duck", "Mickey Mouse","Minnie Mouse","WoodyWood Pecker","Ozzy Osbourne","Pink Floyd",
                "Dire Straits","Bugs Bunny","Daffy Duck", "Tom Jerry","Porky Pig","Popeye Sailor",
                "Pluto Dog","Led Zeppelin","John Lennon","Bruce Wayne")
                .map {
                    val names = it.split(" ".toRegex())
                    val randomCompanyIndex = randomCompanyIndicator.nextInt(0,4)
                    val company = companies[randomCompanyIndex]
                    val companyName = company.name.compressedName()
                    val email = "${names[0]}.${names[1]}@$companyName.com"
                    val status = Contact.Status.values()[randomCompanyIndicator.nextInt(0,Contact.Status.values().size)]
                    return@map Contact(names[0],names[1],email,status,company)
                }.toList()
        )

    }
}

//Just to showcase the extension function capabilities
fun String.compressedName():String = this.replace("\\s-".toRegex(),"")