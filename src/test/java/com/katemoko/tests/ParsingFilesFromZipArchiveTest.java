package com.katemoko.tests;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.codeborne.pdftest.PDF.containsExactText;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Feature("FilesTesting")
@Story("Files in zip testing")
@Tag("files")
public class ParsingFilesFromZipArchiveTest {

    private ClassLoader cl = ParsingFilesFromZipArchiveTest.class.getClassLoader();

    @Test
    @DisplayName("Проверка PDF")
    void pdfTest() throws Exception {
        try (InputStream is = cl.getResourceAsStream("files.zip");
             ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals("ISTQB_Syllabus_v3.1.1.pdf")) {
                    PDF pdf = new PDF(zis);
                    assertEquals(93, pdf.numberOfPages);
                    assertEquals("SJSI", pdf.author);
                    assertThat(pdf, containsExactText("Foundation Level (CTFL) Syllabus"));
                    assertThat(pdf, containsExactText("Version 2018 v3.1.1"));
                }
            }
        }
    }

    @Test
    @DisplayName("Проверка XLSX")
    void xlsxTest() throws Exception {
        try (InputStream is = cl.getResourceAsStream("files.zip");
             ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals("customer_requests.xlsx")) {
                    XLS xls = new XLS(zis);
                    assertEquals("ref156", xls.excel.getSheetAt(0).getRow(1).getCell(0).getStringCellValue());
                    assertEquals("Новая", xls.excel.getSheetAt(0).getRow(1).getCell(1).getStringCellValue());
                    assertEquals(1, xls.excel.getNumberOfSheets());
                }
            }
        }
    }

    @Test
    @DisplayName("Проверка CSV")
    void csvTest() throws Exception {
        try (InputStream is = cl.getResourceAsStream("files.zip");
             ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals("users.csv")) {
                    CSVReader csvReader = new CSVReader(new InputStreamReader(zis));
                    List<String[]> csvContent = csvReader.readAll();
                    assertArrayEquals(new String[]{"Egidio Monti", "lucia.desantis@libero.it"}, csvContent.get(0));
                    assertArrayEquals(new String[]{"Olo Fontana", "cassiopea.sanna@gmail.com"}, csvContent.get(1));
                    assertArrayEquals(new String[]{"Damiana Barone", "nunzia.guerra@hotmail.com"}, csvContent.get(2));
                    assertArrayEquals(new String[]{"Oretta Leone", "giancarlo.giordano@libero.it"}, csvContent.get(3));
                }
            }
        }
    }
}