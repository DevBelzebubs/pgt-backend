package com.portable.microservices.ms_inventory.shared.application.service;

import com.portable.microservices.ms_inventory.kardex.application.dto.KardexExportRow;
import com.portable.microservices.ms_inventory.product.domain.model.Product;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfGeneratorService {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final Color COLOR_HEADER_BG = new Color(129, 0, 10);
    private static final Color COLOR_HEADER_TEXT = Color.WHITE;
    private static final Color COLOR_ROW_ALT = new Color(250, 250, 250);
    private static final Color COLOR_TEXT_DARK = new Color(40, 40, 40);
    private static final Color COLOR_TEXT_GRAY = new Color(100, 100, 100);
    private static final Color COLOR_POSITIVE = new Color(0, 120, 0);

    private static final float MARGIN = 50f;
    private static final float FONT_SIZE_TITLE = 18f;
    private static final float FONT_SIZE_SUBTITLE = 10f;
    private static final float FONT_SIZE_TABLE = 9f;

    public ByteArrayResource generateProductPdf(List<Product> products) {
        try (PDDocument document = new PDDocument();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PDRectangle landscapeA4 = new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth());
            PDPage page = new PDPage(landscapeA4);
            document.addPage(page);
            float effectivePageWidth = landscapeA4.getWidth();
            float effectivePageHeight = landscapeA4.getHeight();
            float contentWidth = effectivePageWidth - (2 * MARGIN);

            float yPosition = effectivePageHeight - MARGIN;
            PDType1Font fontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            PDType1Font fontRegular = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            PDType1Font fontItalic = new PDType1Font(Standard14Fonts.FontName.HELVETICA_OBLIQUE);
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

                yPosition = drawTitle(contentStream, effectivePageWidth, yPosition, fontBold, fontRegular);
                yPosition -= 25;
                float[] columnWidths = { 65, 65, 240, 75, 75, 55, 80 };
                float tableWidth = 0;
                for (float w : columnWidths)
                    tableWidth += w;
                float tableX = MARGIN + (contentWidth - tableWidth) / 2;
                yPosition = drawTableHeader(contentStream, tableX, yPosition, columnWidths, fontBold);

                for (Product p : products) {
                    if (yPosition < MARGIN + 50) {
                        contentStream.close();
                        PDPage newPage = new PDPage(landscapeA4);
                        document.addPage(newPage);
                        yPosition = landscapeA4.getHeight() - MARGIN;
                        try (PDPageContentStream newCs = new PDPageContentStream(document, newPage)) {
                            yPosition = drawTableHeader(newCs, tableX, yPosition, columnWidths, fontBold);
                            yPosition = drawTableRow(newCs, p, tableX, yPosition, columnWidths, fontRegular, fontBold,
                                    products.indexOf(p) % 2 == 1);
                        }
                        yPosition -= 2;
                    } else {
                        yPosition = drawTableRow(contentStream, p, tableX, yPosition, columnWidths, fontRegular,
                                fontBold, products.indexOf(p) % 2 == 1);
                        yPosition -= 2;
                    }
                }
                // ========== CORRECCIÓN 3: Footer en posición segura ==========
                if (document.getPages().getCount() == 1) {
                    yPosition = Math.min(yPosition, MARGIN + 40);
                    drawFooter(contentStream, effectivePageWidth - MARGIN - 120, yPosition, fontItalic,
                            products.size());
                }
            }
            document.save(outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Error generando PDF: " + e.getMessage(), e);
        }
    }

    private float drawTitle(PDPageContentStream cs, float pageWidth, float y,
            PDType1Font fontBold, PDType1Font fontRegular) throws IOException {

        String title = "REPORTE DE INVENTARIO - PRODUCTOS";
        float titleWidth = fontBold.getStringWidth(title) / 1000 * FONT_SIZE_TITLE;
        float titleX = (pageWidth - titleWidth) / 2;

        cs.setNonStrokingColor(COLOR_HEADER_BG);
        cs.beginText();
        cs.setFont(fontBold, FONT_SIZE_TITLE);
        cs.newLineAtOffset(titleX, y);
        cs.showText(title);
        cs.endText();
        y -= 25;

        String subtitle = "Generado el: " + java.time.LocalDateTime.now().format(DATE_FORMATTER);
        float subWidth = fontRegular.getStringWidth(subtitle) / 1000 * FONT_SIZE_SUBTITLE;
        float subX = (pageWidth - subWidth) / 2;

        cs.setNonStrokingColor(COLOR_TEXT_GRAY);
        cs.beginText();
        cs.setFont(fontRegular, FONT_SIZE_SUBTITLE);
        cs.newLineAtOffset(subX, y);
        cs.showText(subtitle);
        cs.endText();
        return y;
    }

    private float drawTableHeader(PDPageContentStream cs, float x, float y,
            float[] widths, PDType1Font font) throws IOException {
        String[] headers = { "SKU", "Cód. Anexo", "Producto", "Precio Compra", "Precio Venta", "Estado",
                "Fecha Creación" };
        float rowHeight = 25f;

        float currentX = x;
        for (int i = 0; i < headers.length; i++) {
            cs.setNonStrokingColor(COLOR_HEADER_BG);
            cs.addRect(currentX, y - rowHeight, widths[i], rowHeight);
            cs.fill();

            String text = headers[i];
            float textWidth = font.getStringWidth(text) / 1000 * FONT_SIZE_TABLE;
            float textX = currentX + (widths[i] - textWidth) / 2;
            float textY = y - (rowHeight / 2) - 3;

            cs.setNonStrokingColor(COLOR_HEADER_TEXT);
            cs.beginText();
            cs.setFont(font, FONT_SIZE_TABLE);
            cs.newLineAtOffset(textX, textY);
            cs.showText(text);
            cs.endText();

            currentX += widths[i];
        }

        return y - rowHeight;
    }

    private float drawTableRow(PDPageContentStream cs, Product p, float x, float y,
            float[] widths, PDType1Font fontRegular, PDType1Font fontBold,
            boolean isAlt) throws IOException {
        float rowHeight = 22f;

        String[] valores = {
                p.codProd() != null ? p.codProd() : "",
                p.codAnexo() != null ? p.codAnexo() : "",
                p.descripcion() != null ? truncate(p.descripcion(), 32) : "",
                p.preCom() != null ? String.format("$ %,1.2f", p.preCom().doubleValue()) : "$ 0.00",
                p.preVen() != null ? String.format("$ %,1.2f", p.preVen().doubleValue()) : "$ 0.00",
                p.estado() ? "ACTIVO" : "INACTIVO",
                p.fecCreacion() != null ? p.fecCreacion().format(DATE_FORMATTER) : ""
        };
        if (isAlt) {
            float totalWidth = 0;
            for (float w : widths)
                totalWidth += w;
            cs.setNonStrokingColor(COLOR_ROW_ALT);
            cs.addRect(x, y - rowHeight, totalWidth, rowHeight);
            cs.fill();
        }
        float currentX = x;
        for (int i = 0; i < valores.length; i++) {
            String text = valores[i];
            PDType1Font fontToUse = (i == 5) ? fontBold : fontRegular;
            Color colorToUse = COLOR_TEXT_DARK;

            if (i == 5) {
                colorToUse = p.estado() ? COLOR_POSITIVE : COLOR_TEXT_GRAY;
            } else if (i == 3 || i == 4) {
                colorToUse = COLOR_POSITIVE;
            }

            float textWidth = fontToUse.getStringWidth(text) / 1000 * FONT_SIZE_TABLE;
            float textX = currentX + 5;
            float textY = y - (rowHeight / 2) - 3;

            if (i == 3 || i == 4) {
                textX = currentX + widths[i] - textWidth - 5;
            } else if (i == 5 || i == 6) {
                textX = currentX + (widths[i] - textWidth) / 2;
            }

            cs.setNonStrokingColor(colorToUse);
            cs.beginText();
            cs.setFont(fontToUse, FONT_SIZE_TABLE);
            cs.newLineAtOffset(textX, textY);
            cs.showText(text);
            cs.endText();

            currentX += widths[i];
        }

        return y - rowHeight;
    }

    private void drawFooter(PDPageContentStream cs, float x, float y,
            PDType1Font fontItalic, int total) throws IOException {
        String text = "Total de productos: " + total;
        cs.setNonStrokingColor(COLOR_TEXT_GRAY);
        cs.beginText();
        cs.setFont(fontItalic, 8f);
        cs.newLineAtOffset(x, y);
        cs.showText(text);
        cs.endText();
    }

    private String truncate(String text, int maxLen) {
        if (text == null || text.length() <= maxLen)
            return text;
        return text.substring(0, maxLen - 3) + "...";
    }

    public ByteArrayResource generateKardexPdf(List<KardexExportRow> rows) {
        try (PDDocument document = new PDDocument();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PDRectangle landscapeA4 = new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth());
            PDPage page = new PDPage(landscapeA4);
            document.addPage(page);
            float pageWidth = landscapeA4.getWidth();
            float pageHeight = landscapeA4.getHeight();
            float margin = 50f;
            float yPosition = pageHeight - margin;
            PDType1Font fontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            PDType1Font fontRegular = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            PDType1Font fontItalic = new PDType1Font(Standard14Fonts.FontName.HELVETICA_OBLIQUE);
            try (PDPageContentStream cs = new PDPageContentStream(document, page)) {
                // Título
                String title = "KARDEX - MOVIMIENTOS DE INVENTARIO";
                float titleWidth = fontBold.getStringWidth(title) / 1000 * 18f;
                cs.setNonStrokingColor(COLOR_HEADER_BG);
                cs.beginText();
                cs.setFont(fontBold, 18f);
                cs.newLineAtOffset((pageWidth - titleWidth) / 2, yPosition);
                cs.showText(title);
                cs.endText();
                yPosition -= 25;
                String subtitle = "Generado el: " + java.time.LocalDateTime.now().format(DATE_FORMATTER);
                float subWidth = fontRegular.getStringWidth(subtitle) / 1000 * 10f;
                cs.setNonStrokingColor(COLOR_TEXT_GRAY);
                cs.beginText();
                cs.setFont(fontRegular, 10f);
                cs.newLineAtOffset((pageWidth - subWidth) / 2, yPosition);
                cs.showText(subtitle);
                cs.endText();
                yPosition -= 25;
                // Columnas
                float[] colWidths = { 85, 170, 60, 60, 70, 50, 50, 50, 50, 70 };
                float tableWidth = 0;
                for (float w : colWidths)
                    tableWidth += w;
                float tableX = margin + ((pageWidth - 2 * margin) - tableWidth) / 2;
                String[] headers = { "Fecha", "Producto", "SKU", "Tipo", "Doc. Ref.",
                        "Stk Ant", "Ingr.", "Sal.", "Stk Act", "Costo Prom" };
                float rowH = 22f;
                // Header
                float cx = tableX;
                for (int i = 0; i < headers.length; i++) {
                    cs.setNonStrokingColor(COLOR_HEADER_BG);
                    cs.addRect(cx, yPosition - rowH, colWidths[i], rowH);
                    cs.fill();
                    String h = headers[i];
                    float tw = fontBold.getStringWidth(h) / 1000 * 9f;
                    cs.setNonStrokingColor(COLOR_HEADER_TEXT);
                    cs.beginText();
                    cs.setFont(fontBold, 9f);
                    cs.newLineAtOffset(cx + (colWidths[i] - tw) / 2, yPosition - rowH + 6);
                    cs.showText(h);
                    cs.endText();
                    cx += colWidths[i];
                }
                yPosition -= rowH;
                // Filas
                for (int idx = 0; idx < rows.size(); idx++) {
                    if (yPosition < margin + 40) {
                        cs.close();
                        PDPage np = new PDPage(landscapeA4);
                        document.addPage(np);
                        yPosition = pageHeight - margin;
                        try (PDPageContentStream ncs = new PDPageContentStream(document, np)) {
                            cx = tableX;
                            for (int i = 0; i < headers.length; i++) {
                                ncs.setNonStrokingColor(COLOR_HEADER_BG);
                                ncs.addRect(cx, yPosition - rowH, colWidths[i], rowH);
                                ncs.fill();
                                String h = headers[i];
                                float tw = fontBold.getStringWidth(h) / 1000 * 9f;
                                ncs.setNonStrokingColor(COLOR_HEADER_TEXT);
                                ncs.beginText();
                                ncs.setFont(fontBold, 9f);
                                ncs.newLineAtOffset(cx + (colWidths[i] - tw) / 2, yPosition - rowH + 6);
                                ncs.showText(h);
                                ncs.endText();
                                cx += colWidths[i];
                            }
                            yPosition -= rowH;
                            yPosition = drawKardexRow(ncs, rows.get(idx), tableX, yPosition, colWidths, fontRegular,
                                    idx % 2 == 1);
                        }
                        yPosition -= 2;
                    } else {
                        yPosition = drawKardexRow(cs, rows.get(idx), tableX, yPosition, colWidths, fontRegular,
                                idx % 2 == 1);
                        yPosition -= 2;
                    }
                }
                if (document.getPages().getCount() == 1) {
                    drawFooter(cs, pageWidth - margin - 120, Math.min(yPosition, margin + 40), fontItalic, rows.size());
                }
            }
            document.save(outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Error generando PDF de kardex: " + e.getMessage(), e);
        }
    }

    private float drawKardexRow(PDPageContentStream cs, KardexExportRow r, float x, float y,
            float[] widths, PDType1Font font, boolean alt) throws IOException {
        float rowH = 20f;
        if (alt) {
            float tw = 0;
            for (float w : widths)
                tw += w;
            cs.setNonStrokingColor(COLOR_ROW_ALT);
            cs.addRect(x, y - rowH, tw, rowH);
            cs.fill();
        }
        String[] vals = {
                r.fecha(), truncate(r.producto(), 28), r.sku(), r.tipoMovimiento(),
                r.documentoRef(), String.valueOf(r.stockAnterior()), String.valueOf(r.cantIngreso()),
                String.valueOf(r.cantSalida()), String.valueOf(r.stockActual()),
                r.costoPromedio() != null ? String.format("$ %,1.2f", r.costoPromedio().doubleValue()) : "$ 0.00"
        };
        float cx = x;
        for (int i = 0; i < vals.length; i++) {
            String text = vals[i];
            float tw = font.getStringWidth(text) / 1000 * 9f;
            float tx = cx + 3;
            if (i >= 5)
                tx = cx + widths[i] - tw - 3;
            cs.setNonStrokingColor(COLOR_TEXT_DARK);
            cs.beginText();
            cs.setFont(font, 9f);
            cs.newLineAtOffset(tx, y - rowH + 5);
            cs.showText(text);
            cs.endText();
            cx += widths[i];
        }
        return y - rowH;
    }
}