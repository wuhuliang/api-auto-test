package com.lemon.api.auto.util;

import com.lemon.api.auto.pojo.Case;
import com.lemon.api.auto.pojo.RestApi;
import com.lemon.api.auto.pojo.WriteBackData;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtil {
    public static Map<String,Integer> caseIdAndRowNum = new HashMap<String, Integer>();
    public static Map<String,Integer> cellNameAndCellNum = new HashMap<String, Integer>();
    public static List<WriteBackData> writeBackDatas = new ArrayList<WriteBackData>();

    static{
        loadRownumAndCellNumMapping("src\\test\\resources\\cases_v7.xlsx","用例");
    }

    /***
     * 获取caseId对应行索引
     * 获取cellName对应列索引
     * @param excelPath
     * @param sheetName
     */
    private static void loadRownumAndCellNumMapping(String excelPath, String sheetName) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(excelPath));
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheet(sheetName);
            //获取标题行
            Row titleRow = sheet.getRow(0);
            int lastCellNum = titleRow.getLastCellNum();//获取的是列号，比列索引大1
            for (int i = 0; i < lastCellNum; i++) {
                Cell cell = titleRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellType(CellType.STRING);
                String title = cell.getStringCellValue();
                title = title.substring(0, title.indexOf("("));
                int cellNum = cell.getAddress().getColumn();
                cellNameAndCellNum.put(title, cellNum);
            }
            //从第二行开始，获取所有数据行
            int lastRownum = sheet.getLastRowNum();
            for (int i = 1; i <= lastRownum; i++) {
                Row datarow = sheet.getRow(i);
                Cell firstCellOfRow = datarow.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                firstCellOfRow.setCellType(CellType.STRING);
                String caseId = firstCellOfRow.getStringCellValue();
                int rowNum = datarow.getRowNum();
                caseIdAndRowNum.put(caseId,rowNum);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }

    }

    private static boolean isEmptyRow(Row dataRow) {
        int lastCellNum = dataRow.getLastCellNum();
        for (int i = 0; i < lastCellNum; i++) {
            Cell cell = dataRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellType(CellType.STRING);
            String value = cell.getStringCellValue();
            if (value!=null&&value.trim().length()>0){
                return false;
            }
        }
        return true;
    }

    /**
     *通过起始和结束行号列号定位数据
     * @param excelPath
     * @param startRow   开始行索引
     * @param endRow
     * @param startCell  开始列索引
     * @param endCell
     * @return
     */
    public static Object[][] datas(String excelPath,int startRow,int endRow,int startCell,int endCell){
        Object[][] datas = null;
        try {
            //获取workboook对象
            Workbook workbook = WorkbookFactory.create(new File(excelPath));
            //获取sheet对象
            Sheet sheet = workbook.getSheet("Sheet1");
            datas = new Object[endRow-startRow+1][endCell-startCell+1];
            //获取行
            for (int i = startRow; i <= endRow ; i++) {
                Row row = sheet.getRow(i); //getRow(行号)，索引从0开始代表第一行
                for (int j = startCell; j <= endCell ; j++) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK); //getCell(列号)，索引从0开始代表第一列
                    //将列设置为字符串类型
                    cell.setCellType(CellType.STRING) ;
                    String value = cell.getStringCellValue();
                    //将取到的值放入数组
                    datas[i-startRow][j-startCell] = value;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }

    /***
     * 通过具体行号和列号操作数据
     * @param excelPath  文件路径
     * @param rows    行号数组
     * @param cells   列号数组
     * @return
     */
    public static Object[][] datas(String excelPath,String sheetName, int[] rows, int[] cells){
        Object[][] datas = null;
        try {
            //获取workboook对象
            Workbook workbook = WorkbookFactory.create(new File(excelPath));
            //获取sheet对象
            Sheet sheet = workbook.getSheet(sheetName);
            //定义保存数据的数组
            datas = new Object[rows.length][cells.length];
            //获取行
            for (int i = 0; i < rows.length ; i++) {
                Row row = sheet.getRow(rows[i]-1); //getRow(行号)，索引从0开始代表第一行
                //获取列
                for (int j = 0; j < cells.length ; j++) {
                    Cell cell = row.getCell(cells[j]-1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK); //getCell(列号)，索引从0开始代表第一列
                    //将列设置为字符串类型
                    cell.setCellType(CellType.STRING) ;
                    String value = cell.getStringCellValue();
                    //将取到的值放入数组
                    datas[i][j] = value;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }

    /***
     * 解析指定excel表单数据，封装为对象
     * @param excelPath
     * @param sheetName
     * @param clazz
     */
    public static <T> void load(String excelPath, String sheetName, Class<T> clazz) {
        try {
            //创建workbook对象
            Workbook workbook = WorkbookFactory.create(new File(excelPath));
            //创建sheet对象
            Sheet sheet = workbook.getSheet(sheetName);
            //获取首行，用例信息字段字段的介绍行
            Row titleRow = sheet.getRow(0);
            //获取列的数量,最后一列的列号
            int lastCellNum = titleRow.getLastCellNum();
            //循环处理每一列,取出每一列里面的字段名，保存到数组
            String[] fields = new String[lastCellNum];
            for (int i = 0; i < lastCellNum ; i++) {
                //根据列索引获取对应列
                Cell cell = titleRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                //设置列的类型为字符串
                cell.setCellType(CellType.STRING);
                //获取列的值
                String titleAll = cell.getStringCellValue();
                //截取CaseId(用例编号)中的英文字符串
                String title = titleAll.substring(0,titleAll.indexOf("("));
                //放入数组
                fields[i] = title;
            }
            //获取行的数量，最后一行的行号
            int lastRowNum = sheet.getLastRowNum();
            //循环处理每一数据行,从索引1开始
            for (int i = 1; i <= lastRowNum; i++) {
                //每一行对应一个Case对象
                Object object = clazz.newInstance();
                //获取一个数据行
                Row dataRow = sheet.getRow(i);
                //循环处理数据行每一列,把数据封装到cs对象
                for (int j = 0; j < lastCellNum; j++) {
                    Cell cell = dataRow.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    cell.setCellType(CellType.STRING);
                    String value = cell.getStringCellValue();
                    //获取要反射的方法名
                    String methodName = "set"+fields[j];
                    //获取要反射的方法对象
                    Method method = clazz.getMethod(methodName, String.class);
                    //通过反射调用方法
                    method.invoke(object, value);
                }
                //把每一行封装好的cs对象添加到保存用例对象的CaseUtil类中的cases列表
                if (object instanceof Case){
                    Case cs = (Case) object;
                    CaseUtil.cases.add(cs);
                }else if (object instanceof RestApi){
                    RestApi restApi = (RestApi) object;
                    RestUtil.rests.add(restApi);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 回写接口响应报文
     * @param excelPath
     * @param sheetName
     * @param caseId
     * @param cellName
     * @param result
     */
    public static void writeBackData(String excelPath, String sheetName, String caseId, String cellName, String result) {
        System.out.println("读写操作");
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(new File(excelPath));
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheet(sheetName);
            int rownum = caseIdAndRowNum.get(caseId);
            Row row = sheet.getRow(rownum);
            int cellnum = cellNameAndCellNum.get(cellName);
            Cell cell = row.getCell(cellnum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellType(CellType.STRING);
            cell.setCellValue(result);
            outputStream = new FileOutputStream(excelPath);
            workbook.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }

    }

    /***
     * 批量回写数据方法
     * @param excelPath
     */
    public static void batchWriteBackDatas(String excelPath) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(new File(excelPath));
            Workbook workbook = WorkbookFactory.create(inputStream);
            for (WriteBackData writeBackData:writeBackDatas) {
                //获取sheetName
                String sheetName = writeBackData.getSheetName();
                Sheet sheet = workbook.getSheet(sheetName);
                //获取行
                String caseId = writeBackData.getCaseId();
                int rownum = caseIdAndRowNum.get(caseId);
                Row row = sheet.getRow(rownum);
                //获取列
                String cellName = writeBackData.getCellName();
                int cellnum = cellNameAndCellNum.get(cellName);
                Cell cell = row.getCell(cellnum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellType(CellType.STRING);
                //获取结果数据
                String result = writeBackData.getResult();
                //往指定单元格设置数据
                cell.setCellValue(result);
            }
            outputStream = new FileOutputStream(new File(excelPath));
            workbook.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }


}
