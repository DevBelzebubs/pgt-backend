package com.portable.microservices.ms_inventory.product.domain.ports.in;

import org.springframework.core.io.Resource;

public interface ExportProductPortIn {
    
    enum ExportFormat {
        EXCEL, PDF
    }
    
    Resource exportToFormat(ExportFormat format);
    
    @Deprecated
    default Resource exportToExcel() {
        return exportToFormat(ExportFormat.EXCEL);
    }
}