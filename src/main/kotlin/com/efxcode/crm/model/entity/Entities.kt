package com.efxcode.crm.model.entity

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

@MappedSuperclass
abstract class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id", updatable = false, nullable = false)
    var id: Long? = null

    @Transient
    fun isPersisted():Boolean = id != null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AbstractEntity

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}

@Entity
class Contact(
        @NotEmpty var firstName: String,
        @NotEmpty var lastName: String,
        @NotEmpty @Email var email: String,
        @Enumerated(EnumType.STRING) var status: Contact.Status,
        @ManyToOne @JoinColumn(name="company_id") var company: Company?
) : AbstractEntity(), Cloneable {

    enum class Status {
        ImportedLead, NotContacted, Contacted, Customer, ClosedLost;
    }

    override fun toString(): String = "$firstName-$lastName-$email"

    companion object {
        fun newContact():Contact = Contact("","","",Status.Customer,null)
    }
}

@Entity
class Company(
    _name:String,
    @OneToMany(mappedBy = "company",fetch = FetchType.EAGER) var employees:MutableList<Contact>
) : AbstractEntity() {

    var name:String = _name

    constructor(_name: String):this(_name, mutableListOf())

    override fun toString(): String {
        return "name, total employees:[${employees.size}]"
    }
}
