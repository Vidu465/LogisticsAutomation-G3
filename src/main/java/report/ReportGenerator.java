package report;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;

public class ReportGenerator {
    public static void generateMonthlyReport(String filePath, String reportContent) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();
            document.add(new Paragraph("Monthly Report", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph(reportContent));
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 
