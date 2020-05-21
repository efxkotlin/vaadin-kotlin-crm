package com.efxcode.crm.ui.view

import com.efxcode.crm.model.entity.Company
import com.efxcode.crm.model.entity.Contact
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.ComponentEvent
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.EmailField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.BeanValidationBinder
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.shared.Registration

class ContactForm(private val companies:List<Company>) : FormLayout() {

    private val firstName: TextField = TextField(getTranslation("label.first_name"))
    private val lastName:TextField = TextField(getTranslation("label.last_name"))
    private val email:EmailField = EmailField(getTranslation("label.email"))
    private val status:ComboBox<Contact.Status> = ComboBox(getTranslation("label.status"))
    private val company:ComboBox<Company> = ComboBox(getTranslation("label.company"))
    private val save:Button = Button(getTranslation("button.save"))
    private val delete:Button = Button(getTranslation("button.delete"))
    private val close:Button = Button(getTranslation("button.close"))
    private val binder:Binder<Contact> = BeanValidationBinder(Contact::class.java)

    init {
        addClassName("contact-form")
        binder.bindInstanceFields(this)

        company.setItems(companies)
        company.setItemLabelGenerator(Company::name)
        status.setItems(*Contact.Status.values())

        add(firstName,lastName,email,status,company,createButtonsLayout())
    }

    private fun createButtonsLayout():HorizontalLayout {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR)
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY)
        save.addClickShortcut(Key.ENTER)
        close.addClickShortcut(Key.ESCAPE)

        save.addClickListener(this::validateAndSave)
        delete.addClickListener {
            fireEvent(DeleteEvent(this, binder.bean))
        }
        close.addClickListener { fireEvent(CloseEvent(this)) }

        binder.addStatusChangeListener{
            save.isEnabled = binder.isValid
        }
        return HorizontalLayout(save,delete,close)
    }

    private fun validateAndSave(event:ClickEvent<Button>) {
        if (binder.isValid) {
            fireEvent(SaveEvent(this, binder.bean))
        }
    }

    fun setContact(contact: Contact?) {
        binder.bean = contact
    }

    abstract class ContactFormEvent(source: ContactForm, val contact:Contact?):ComponentEvent<ContactForm>(source,false)

    class SaveEvent(source: ContactForm, contact:Contact): ContactFormEvent(source, contact)

    class DeleteEvent(source: ContactForm, contact:Contact): ContactFormEvent(source, contact)

    class CloseEvent(source: ContactForm, contact:Contact?=null): ContactFormEvent(source, null)

    public override fun <T : ComponentEvent<*>?> addListener(eventType: Class<T>?, listener: ComponentEventListener<T>?): Registration {
        return eventBus.addListener(eventType,listener)
    }
}
