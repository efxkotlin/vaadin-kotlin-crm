package com.efxcode.crm.service

import com.efxcode.crm.model.entity.Company
import com.efxcode.crm.model.repository.CompanyRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CompanyService(val companyRepository: CompanyRepository) {

    fun findAll():List<Company> {
        return companyRepository.findAll().toList()
    }

    fun getStats(): Map<String,Double> {
        return findAll().asSequence().groupBy{it.name}.mapValues {entry ->
            entry.value.sumByDouble {
                it.employees.size.toDouble()
            }
        }
    }
}