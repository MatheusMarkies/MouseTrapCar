package com.matheusmarkies.manager.utilities;

/*
 * MIT License
 *
 * Copyright (c)2023 Matheus Markies
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import java.io.File;

import com.matheusmarkies.objects.RotationList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.matheusmarkies.manager.utilities.Save.ApplicationFolder;
import static com.matheusmarkies.manager.utilities.Save.ImportantDirectories;

public class XLS {
    public static void createXLSFile(RotationList rotationList) throws IOException {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet(" Rotation Details");
        Row row = sheet.createRow(0);

        Cell titleRotation= row.createCell(1);
        Cell titleTime = row.createCell(0);

        titleRotation.setCellValue("Rotacoes");
        titleTime.setCellValue("Tempo");

        int rowNum = 1;
        int colNum = 0;
System.out.println(rotationList.getRotationsArrayList().size());
        for (int i =0;i<rotationList.getRotationsArrayList().size();i++) {
            Row bodyRow = sheet.createRow(i+1);

            Cell rotation= bodyRow.createCell(1);
            Cell time = bodyRow.createCell(0);

            rotation.setCellValue(rotationList.getRotationsArrayList().get(i).getRotationValue());
            time.setCellValue((double) rotationList.getRotationsArrayList().get(i).getElapsedTime());

            colNum = 0;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        FileOutputStream outputStream = new FileOutputStream(ImportantDirectories[0] + "\\" + "Rotations_" +timeStamp+ ".xlsx");
        wb.write(outputStream);
        System.out.println(" Excel file generated");
    }
}
