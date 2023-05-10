package com.cs.helper;

import com.cs.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserExcelExporter {
    private User user;
    XSSFWorkbook workbook=new XSSFWorkbook();
    XSSFSheet sheet=workbook.createSheet("Users");

    private void writeHeaderRow(){ // To add the header for excel file first row
        Row row=sheet.createRow(0);
        Cell cell=row.createCell(0);
        cell.setCellValue("Name");

        cell=row.createCell(1);
        cell.setCellValue("Email");

        cell=row.createCell(2);
        cell.setCellValue("Aadhar No");

        cell=row.createCell(3);
        cell.setCellValue("Role");

    }

    private void writeDataRow(){ // To add the data to the excel file
        Row row=sheet.createRow(1);
        Cell cell = row.createCell(0);
        cell.setCellValue(user.getName());

        cell = row.createCell(1);
        cell.setCellValue(user.getEmail());

        cell = row.createCell(2);
        cell.setCellValue(user.getAadhaar());

        cell = row.createCell(3);
        cell.setCellValue(user.getRole());


    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderRow();
        writeDataRow();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
