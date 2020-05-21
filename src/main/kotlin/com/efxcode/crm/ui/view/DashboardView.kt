package com.efxcode.crm.ui.view

import com.efxcode.crm.AppConfig
import com.efxcode.crm.service.CompanyService
import com.efxcode.crm.service.ContactService
import com.efxcode.crm.ui.layout.MainLayout
import com.github.appreciated.apexcharts.ApexChartsBuilder
import com.github.appreciated.apexcharts.config.builder.ChartBuilder
import com.github.appreciated.apexcharts.config.builder.LegendBuilder
import com.github.appreciated.apexcharts.config.builder.ResponsiveBuilder
import com.github.appreciated.apexcharts.config.chart.Type
import com.github.appreciated.apexcharts.config.legend.Position
import com.github.appreciated.apexcharts.config.responsive.builder.OptionsBuilder
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.HasDynamicTitle
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import org.springframework.security.core.context.SecurityContextHolder

@Route("dashboard",layout = MainLayout::class)
class DashboardView(private val contactService: ContactService,
                    private val companyService: CompanyService,private val appConfig: AppConfig) : VerticalLayout(),HasDynamicTitle {

    init {
        addClassName("dashboard-view")
        defaultHorizontalComponentAlignment = FlexComponent.Alignment.CENTER
        add(getContactStats(), getCompaniesChart())
    }

    private fun getContactStats():Span {
        val stats = Span("${contactService.count()} ${getTranslation("text.contacts")}")
        stats.addClassName("contact-stats")
        return stats
    }

    private fun getCompaniesChart(): Div{
        val companyStats = companyService.getStats()
        val pieChart = ApexChartsBuilder.get()
                .withChart(ChartBuilder.get().withType(Type.pie).build())
                .withLabels(*companyStats.keys.toTypedArray())
                .withLegend(LegendBuilder.get().withPosition(Position.right).build())
                .withSeries(*companyStats.values.toTypedArray())
                .withResponsive(ResponsiveBuilder.get()
                        .withBreakpoint(480.0)
                        .withOptions(OptionsBuilder.get()
                                .withLegend(LegendBuilder.get()
                                        .withPosition(Position.bottom).build()).build()
                        ).build()
                ).build() //TODO convert to dsl style
        val div = Div()
        div.add(pieChart)

        return div
    }

    override fun getPageTitle(): String {
        return "${getTranslation("app.dashboardView")} | ${SecurityContextHolder.getContext().authentication.name}"
    }
}
