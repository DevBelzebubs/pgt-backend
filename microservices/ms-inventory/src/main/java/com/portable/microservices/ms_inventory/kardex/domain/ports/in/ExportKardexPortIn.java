package com.portable.microservices.ms_inventory.kardex.domain.ports.in;

import org.springframework.core.io.Resource;

import com.portable.microservices.ms_inventory.kardex.domain.model.MetodoCosto;

public interface ExportKardexPortIn {
    enum ExportFormat {
        EXCEL, PDF
    }

    Resource exportToFormat(ExportFormat format, MetodoCosto metodo);
}
