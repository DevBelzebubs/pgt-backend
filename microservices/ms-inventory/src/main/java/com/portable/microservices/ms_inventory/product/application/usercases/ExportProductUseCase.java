package com.portable.microservices.ms_inventory.product.application.usercases;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.portable.microservices.ms_inventory.product.domain.model.Product;
import com.portable.microservices.ms_inventory.product.domain.ports.in.ExportProductPortIn;
import com.portable.microservices.ms_inventory.product.domain.ports.out.ProductPersistencePortOut;
import com.portable.microservices.ms_inventory.shared.application.service.ExcelGeneratorService;
import com.portable.microservices.ms_inventory.shared.application.service.PdfGeneratorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExportProductUseCase implements ExportProductPortIn {
    private final ProductPersistencePortOut productPersistence;
    private final ExcelGeneratorService excelGenerator;
    private final PdfGeneratorService pdfGenerator;
    @Override
    public Resource exportToExcel() {
        List<Product> products = productPersistence.findAll();
        return excelGenerator.generateProductExcel(products);
    }
    @Override
    public Resource exportToFormat(ExportFormat format) {
        List<Product> products = productPersistence.findAll();
        
        return switch (format) {
            case EXCEL -> excelGenerator.generateProductExcel(products);
            case PDF -> pdfGenerator.generateProductPdf(products);
        };
    }
}
