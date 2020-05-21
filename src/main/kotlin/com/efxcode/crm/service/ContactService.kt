package com.efxcode.crm.service

import com.efxcode.crm.model.entity.Contact
import com.efxcode.crm.model.repository.ContactRepository
import com.efxcode.crm.search.SearchData
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import javax.persistence.criteria.Predicate

@Service
class ContactService(val contactRepository: ContactRepository) {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(ContactService::class.java)
    }

    fun findAll():Iterable<Contact> {
        return contactRepository.findAll()
    }

    fun findAll(searchData: SearchData):Iterable<Contact> {
        if (searchData.isEmpty()) {
            return contactRepository.findAll()
        } else {
            return contactRepository.findAll { root, _, cb ->
                val list = mutableListOf<Predicate>()
                if (searchData.name != null && searchData.name.isNotEmpty()) {
                    val p1 = cb.like(cb.lower(root.get("firstName")), "%${searchData.name.toLowerCase()}%")
                    val p2 = cb.like(cb.lower(root.get("lastName")), "%${searchData.name.toLowerCase()}%")
                    list += cb.or(p1, p2)
                }
                if (searchData.email != null && searchData.email.isNotEmpty()) {
                    list += cb.equal(cb.lower(root.get("email")), searchData.email.toLowerCase())
                }
                return@findAll cb.or(*list.toTypedArray())
            }
        }
    }

    fun count():Long {
        return contactRepository.count()
    }

    fun delete(contact:Contact) {
        contactRepository.delete(contact)
    }

    fun save(contact: Contact?) {
        logger.info("Contact received to save. $contact")
        contact?.let {
            contactRepository.save(contact)
        }
    }

}