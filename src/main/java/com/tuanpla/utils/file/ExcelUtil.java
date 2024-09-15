/*
 *  Copyright 2022 by Tuanpla
 *  https://tuanpla.com
 */
package com.tuanpla.utils.file;

import com.tuanpla.utils.common.LogUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Centurion
 */
public class ExcelUtil {

    private static Logger logger = LogManager.getLogger(ExcelUtil.class);

    public static String normalizeCellType(Cell cell) {
        String value = null;
        if (cell != null) {
//            cell.setCellType(Cell.CELL_TYPE_STRING);
            value = cell.getStringCellValue();
        }
        return value;
    }

    /**
     * Doc file XSL Từ file Source
     *
     * @param path
     * @param sheetNum
     * @return
     */
    public static ArrayList<ArrayList<String>> readXsl(String path, int sheetNum) {
        ArrayList<ArrayList<String>> allRow = new ArrayList<>();
        try {
            FileInputStream file = new FileInputStream(new File(path));
            //Get the workbook instance for XLS file
            HSSFWorkbook workbook = new HSSFWorkbook(file);
            //Get first sheet from the workbook
            HSSFSheet oneSheet = workbook.getSheetAt(sheetNum);
            for (Row _oneRow : oneSheet) {
                ArrayList<String> allVal_Row = new ArrayList<>();
                // Duyet qua tung dong cua mot sheet
                for (Cell oneCell : _oneRow) {
                    // Duyet qua tung cell cua mot dong
                    String val = normalizeCellType(oneCell);
                    allVal_Row.add(val);
                }
                allRow.add(allVal_Row);
            }
        } catch (Exception ex) {
            logger.error(LogUtils.getLogMessage(ex));
        }
        return allRow;
    }

    /**
     * Doc file XSL Từ file InputStream
     *
     * @param ipt
     * @param sheetNum
     * @return
     */
    public static ArrayList<ArrayList<String>> readXsl(InputStream ipt, int sheetNum) {
        ArrayList<ArrayList<String>> allRow = new ArrayList<>();
        try {
            //Get the workbook instance for XLS file
            HSSFWorkbook workbook = new HSSFWorkbook(ipt);
            //Get first sheet from the workbook
            HSSFSheet oneSheet = workbook.getSheetAt(sheetNum);
            for (Row _oneRow : oneSheet) {
                ArrayList<String> allVal_Row = new ArrayList<>();
                // Duyet qua tung dong cua mot sheet
                // Duyet qua tung cell cua mot dong
                String val = normalizeCellType(_oneRow.getCell(0));
                allVal_Row.add(val);
                allRow.add(allVal_Row);
            }
        } catch (Exception ex) {
            logger.error(LogUtils.getLogMessage(ex));
        }
        return allRow;
    }

    public static HSSFSheet readXsl(InputStream ipt) {
        HSSFSheet oneSheet = null;
        try {
            //Get the workbook instance for XLS file
            HSSFWorkbook workbook = new HSSFWorkbook(ipt);
            //Get first sheet from the workbook
            oneSheet = workbook.getSheetAt(0);
        } catch (Exception ex) {
            logger.error(LogUtils.getLogMessage(ex));
        }
        return oneSheet;
    }

    /**
     * *
     * Doc file XSLX Từ file Source
     *
     * @param path
     * @param sheetNum
     * @return
     */
    public static ArrayList<ArrayList<String>> readXslx(String path, int sheetNum) {
        ArrayList<ArrayList<String>> allRow = new ArrayList<>();
        try {
            //..
            FileInputStream file = new FileInputStream(new File(path));
            //Get the workbook instance for XLS file
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            //Get first sheet from the workbook
            XSSFSheet oneSheet = workbook.getSheetAt(sheetNum);
            for (Row _oneRow : oneSheet) {
                ArrayList<String> allVal_Row = new ArrayList<>();
                // Duyet qua tung dong cua mot sheet
                // Duyet qua tung cell cua mot dong
                String val = normalizeCellType(_oneRow.getCell(0));
                allVal_Row.add(val);
                allRow.add(allVal_Row);
            }
        } catch (Exception ex) {
            logger.error(LogUtils.getLogMessage(ex));
        }
        return allRow;
    }

    /**
     * Doc file XSLX Từ file InputStream
     *
     * @param ipt
     * @param sheetNum
     * @return
     */
    public static ArrayList<ArrayList<String>> readXslx(InputStream ipt, int sheetNum) {
        ArrayList<ArrayList<String>> allRow = new ArrayList<>();
        try {
            //Get the workbook instance for XLS file
            XSSFWorkbook workbook = new XSSFWorkbook(ipt);
            //Get first sheet from the workbook
            XSSFSheet oneSheet = workbook.getSheetAt(sheetNum);

            for (Row _oneRow : oneSheet) {
                ArrayList<String> allVal_Row = new ArrayList<>();
                // Duyet qua tung dong cua mot sheet
                for (Cell oneCell : _oneRow) {
                    // Duyet qua tung cell cua mot dong
                    String val = normalizeCellType(oneCell);
                    allVal_Row.add(val);
                }
                allRow.add(allVal_Row);
            }
        } catch (Exception ex) {
            logger.error(LogUtils.getLogMessage(ex));
        }
        return allRow;
    }

    public static XSSFSheet readXslx(InputStream ipt) {
        XSSFSheet oneSheet = null;
        try {
            //Get the workbook instance for XLS file
            XSSFWorkbook workbook = new XSSFWorkbook(ipt);
            //Get first sheet from the workbook
            oneSheet = workbook.getSheetAt(0);
        } catch (Exception ex) {
            logger.error(LogUtils.getLogMessage(ex));
        }
        return oneSheet;
    }

    public static void createExcel() {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Sample sheet");

        Map<String, Object[]> data = new HashMap<String, Object[]>();
        data.put("1", new Object[]{"Emp No.", "Name", "Salary"});
        data.put("2", new Object[]{1d, "John", 1500000d});
        data.put("3", new Object[]{2d, "Sam", 800000d});
        data.put("4", new Object[]{3d, "Dean", 700000d});

        Set<String> keyset = data.keySet();
        int rownum = 0;
        for (String key : keyset) {
            Row row = sheet.createRow(rownum++);
            Object[] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                Cell cell = row.createCell(cellnum++);
                if (obj instanceof Date) {
                    cell.setCellValue((Date) obj);
                } else if (obj instanceof Boolean) {
                    cell.setCellValue((Boolean) obj);
                } else if (obj instanceof String) {
                    cell.setCellValue((String) obj);
                } else if (obj instanceof Double) {
                    cell.setCellValue((Double) obj);
                }
            }
        }

        try {
            FileOutputStream out = new FileOutputStream(new File("C:\\new.xls"));
            workbook.write(out);
            out.close();
            logger.debug("Excel written successfully..");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
