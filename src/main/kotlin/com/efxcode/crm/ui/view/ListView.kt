package com.efxcode.crm.ui.view

import com.efxcode.crm.AppConfig
import com.efxcode.crm.model.entity.Contact
import com.efxcode.crm.search.SearchData
import com.efxcode.crm.service.CompanyService
import com.efxcode.crm.service.ContactService
import com.efxcode.crm.ui.layout.MainLayout
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode
import com.vaadin.flow.router.HasDynamicTitle
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import org.springframework.security.core.context.SecurityContextHolder

@Route("", layout = MainLayout::class)
class ListView(private val contactService: ContactService,
               private val companyService: CompanyService,
               private val appConfig:AppConfig) : VerticalLayout(),HasDynamicTitle {

    private val grid = Grid(Contact::class.java)
    private val filterByName = TextField()
    private val filterByEmail = TextField()
    private lateinit var contactForm: ContactForm

    init {
        addClassName("list-view")
        setSizeFull()
        configureMainGrid()
        buildContactFormAndRegisterListener() // need a shorter name?
        add(buildToolBar(), buildContentArea())
        updateGridData()
    }

    private fun buildToolBar(): HorizontalLayout {
        val horizontalLayout = HorizontalLayout()
        configureFilter(filterByName, getTranslation("label.name"), horizontalLayout)
        configureFilter(filterByEmail, getTranslation("label.email"), horizontalLayout)
        val addContact = Button(getTranslation("button.add_contact"))
        addContact.addClickListener { addContact() }
        horizontalLayout.add(addContact)
        horizontalLayout.addClassName("toolbar")
        return horizontalLayout
    }

    /**
     * Defines the contact form and registers the listeners
     */
    private fun buildContactFormAndRegisterListener() {
        contactForm = ContactForm(companyService.findAll())
        with(contactForm) {
            addListener(ContactForm.SaveEvent::class.java, this@ListView::saveContact)
            addListener(ContactForm.DeleteEvent::class.java, this@ListView::deleteContact)
            addListener(ContactForm.CloseEvent::class.java) { closeEditor() }
        }
        closeEditor()
    }

    /**
     * Defines the layout of the main view. Contains a contact grid and an editable contact form
     */
    private fun buildContentArea(): Div {
        val content = Div(grid, contactForm)
        content.addClassName("content")
        content.setSizeFull()
        return content
    }

    /**
     * Defines the filter and the value change listener
     */
    private fun configureFilter(textField: TextField, type: String, horizontalLayout: HorizontalLayout) {
        with(textField) {
            placeholder = "${getTranslation("text.filter_by")} $type.."
            isClearButtonVisible = true
            valueChangeMode = ValueChangeMode.LAZY
            addValueChangeListener { this@ListView.updateGridData() }
        }
        horizontalLayout.add(textField)
    }

    /**
     * Defines the main grid, set columns and value change listener
     */
    private fun configureMainGrid() {
        with(grid) {
            addClassName("contact-grid")
            setSizeFull()
            setColumns("firstName", "lastName", "email", "status")
            addColumn {
                return@addColumn it.company?.name ?: "-"
            }.setHeader(getTranslation("label.company")).isSortable = true
            columns.applyAutoWidth()
            asSingleSelect().addValueChangeListener { editContact(it.value) }
        }
    }

    private fun updateGridData() {
        val searchData = SearchData(filterByName.value, filterByEmail.value)
        grid.setItems(contactService.findAll(searchData).toList())
    }

    private fun editContact(contact: Contact?) {
        if (contact == null) closeEditor()
        else {
            contactForm.setContact(contact)
            contactForm.isVisible = true
            addClassName("editing")
        }
    }

    private fun deleteContact(event: ContactForm.DeleteEvent) {
        event.contact?.let {
            contactService.delete(event.contact)
            updateGridData()
            closeEditor()
        }
    }

    private fun saveContact(event: ContactForm.SaveEvent) {
        event.contact?.let {
            contactService.save(event.contact)
            updateGridData()
            closeEditor()
        }
    }

    private fun closeEditor() {
        contactForm.setContact(null)
        contactForm.isVisible = false
        removeClassName("editing")
    }

    private fun addContact() {
        grid.asSingleSelect().clear()
        editContact(Contact.newContact())
    }

    override fun getPageTitle(): String {
        return "${getTranslation("app.listView")} | ${SecurityContextHolder.getContext().authentication.name}"
    }

}

//extension function
fun <T> MutableList<Grid.Column<T>>.applyAutoWidth() {
    this.forEach {
        it.setAutoWidth(true)
    }
}


