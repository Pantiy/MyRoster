package cn.pantiy.myroster.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.pantiy.myroster.model.Affair;
import cn.pantiy.myroster.model.ClassmateInfo;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * MyRoster
 * cn.pantiy.myroster.utils
 * Created by pantiy on 17-6-6.
 * Copyright © 2017 All rights Reserved by Pantiy
 */

public final class ExcelUtil {

    private static final String TAG = "ExcelUtil";

    private static final int STUDENT_NUM_COL = 0;
    private static final int STUDENT_NAME_COL = 1;
    private static final int STATE_COL = 2;

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

    public static void writeExcel(Affair affair) throws IOException, WriteException{

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyRoster/";
        Log.i(TAG, "exportPath: " + path);
        File excelFile = new File(path, affair.getAffairName()
                + TimeUtil.format("_yyyy.MM.dd_HH:mm", affair.getCreateTime()) + ".xls");
        if (!excelFile.getParentFile().exists()) {
            excelFile.getParentFile().mkdir();
        }

        List<ClassmateInfo> classmateInfoList = affair.getClassmateInfoList();
        boolean[] stateArray = affair.getStateArray();

        WritableWorkbook workbook = Workbook.createWorkbook(excelFile);
        WritableSheet sheet = workbook.createSheet(affair.getAffairName(), 0);

        Label studentNum = new Label(STUDENT_NUM_COL, 0, "学号");
        sheet.addCell(studentNum);
        Label studentName = new Label(STUDENT_NAME_COL, 0, "姓名");
        sheet.addCell(studentName);
        Label state = new Label(STATE_COL, 0, "状态");
        sheet.addCell(state);

        for (int i = 0; i < classmateInfoList.size(); i++) {
            ClassmateInfo classmateInfo = classmateInfoList.get(i);
            studentNum = new Label(STUDENT_NUM_COL, i+1, classmateInfo.getStudentNum());
            sheet.addCell(studentNum);
            studentName = new Label(STUDENT_NAME_COL, i+1, classmateInfo.getStudentName());
            sheet.addCell(studentName);
        }

        for (int j = 0; j < stateArray.length; j++) {
            state = new Label(STATE_COL, j+1, stateArray[j] ? "√" : "×");
            sheet.addCell(state);
        }

        workbook.write();
        workbook.close();
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

    public interface ExportAffairFinishedListener {
        void onExportAffairFinished();
    }
}
