package com.portable.microservices.ms_inventory.kardex.domain.ports.in;

import org.springframework.core.io.Resource;

public interface ExportKardexPortIn {
    enum ExportFormat {
        EXCEL, PDF
    }

    Resource exportToFormat(ExportFormat format);
}
