package com.portable.microservices.ms_inventory.shared.application.service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import java.io.IOException;
import com.portable.microservices.ms_inventory.product.domain.model.Product;

@Service
public class ExcelGeneratorService {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
     public ByteArrayResource generateProductExcel(List<Product> products) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Inventario Productos");
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle moneyStyle = createMoneyStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);
            String[] headers = {
                "SKU", "Cód. Anexo", "Producto", "Precio Compra", 
                "Precio Venta", "Estado", "Fecha Creación", "Categoría ID", "Marca ID"
            };
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            int rowNum = 1;
            for (Product p : products) {
                Row row = sheet.createRow(rowNum++);
                
                row.createCell(0).setCellValue(p.codProd() != null ? p.codProd() : "");
                row.createCell(1).setCellValue(p.codAnexo() != null ? p.codAnexo() : "");
                row.createCell(2).setCellValue(p.descripcion() != null ? p.descripcion() : "");
                
                Cell preComCell = row.createCell(3);
                preComCell.setCellValue(p.preCom() != null ? p.preCom().doubleValue() : 0.0);
                preComCell.setCellStyle(moneyStyle);
                
                Cell preVenCell = row.createCell(4);
                preVenCell.setCellValue(p.preVen() != null ? p.preVen().doubleValue() : 0.0);
                preVenCell.setCellStyle(moneyStyle);
                
                row.createCell(5).setCellValue(p.estado() ? "ACTIVO" : "INACTIVO");
                
                Cell fechaCell = row.createCell(6);
                if (p.fecCreacion() != null) {
                    fechaCell.setCellValue(p.fecCreacion().format(DATE_FORMATTER));
                    fechaCell.setCellStyle(dateStyle);
                }
                
                row.createCell(7).setCellValue(p.categoryId() != null ? p.categoryId() : 0);
                row.createCell(8).setCellValue(p.brandId() != null ? p.brandId() : 0);
            }
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            sheet.setColumnWidth(2, 15000);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Error generando Excel: " + e.getMessage(), e);
        }
    }
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_RED.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        font.setColor(IndexedColors.WHITE.getIndex());
        return style;
    }
    private CellStyle createMoneyStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("$#,##0.00"));
        return style;
    }
    private CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }
}
