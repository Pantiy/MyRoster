package cn.pantiy.myroster.utils;

import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * MyRoster
 * cn.pantiy.myroster.utils
 * Created by pantiy on 17-6-6.
 * Copyright © 2017 All rights Reserved by Pantiy
 */

public class ExcelUtil {

    private static final String TAG = "ExcelUtil";

    public static List<String[]> readExcel(File file) throws IOException, BiffException {

        Log.i(TAG, "readExcel()");

        List<String[]> excelContent = new ArrayList<>();

        if (!Check.isExcelFile(file)) {
            Log.i(TAG, "is not excel file");
            return null;
        }

        Workbook workbook = Workbook.getWorkbook(file);

        Sheet sheet = workbook.getSheet(0);
        if(sheet == null) {
            return null;
        }

        int rowCount = sheet.getRows();
//		int colCount = sheet.getColumns();

        for (int row = 0; row < rowCount; row++) {
            String[] cells = new String[2];
            for (int col = 0; col < 2; col++) {
                Cell cell = sheet.getCell(col, row);
                cells[col] = cell.getContents();
                if (cells[col].equals("")) {
                    break;
                }
            }
            excelContent.add(cells);
        }

        return excelContent;
    }

    private static class Check {

        private static boolean isExcelFile(File file) throws IOException {

            if (file == null) {
                throw new FileNotFoundException();
            }

            String fileName = file.getName();
            System.out.println(fileName);
            return endWith(fileName, "xls");
        }

        private static boolean endWith(String fileName, String end) {
            return fileName.substring(fileName.lastIndexOf(".") + 1).equals(end);
        }
    }
}
