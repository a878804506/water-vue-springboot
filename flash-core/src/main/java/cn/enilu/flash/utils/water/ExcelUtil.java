package cn.enilu.flash.utils.water;

import cn.enilu.flash.bean.entity.water.WaterBill;
import cn.enilu.flash.utils.HttpUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.poi.ss.usermodel.CellType.STRING;

public class ExcelUtil {

    // 匹配"{exp}"
    private static final String REG = "\\{([a-zA-Z_]+)\\}";
    // 匹配"{xxx.exp}"
    private static final String REG_LIST = "\\{([a-zA-Z_]+)\\.([a-zA-Z_]+)\\}";

    // 匹配 ${demo}
    private final static String REG_ = "\\$\\{([a-z,A-Z_]+)\\}";

    private static final java.util.regex.Pattern PATTERN = java.util.regex.Pattern.compile(REG);
    private static final java.util.regex.Pattern PATTERN_LIST = Pattern.compile(REG_LIST);

    /**
     * 根据模板生成Excel文件
     *
     * @param context 表头或表尾数据集合
     * @return
     */
    public static byte[] writeExcel(Map<String, Object> context, boolean monthlyPayment) throws Exception {
        String excelTemplatePath = "templates" + File.separator + (monthlyPayment ? "water_monthly_payment_template.xlsx" : "water_template.xlsx");
        ClassPathResource classPathResource = new ClassPathResource(excelTemplatePath);
        InputStream inputStream = classPathResource.getInputStream();

        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);// 获取配置文件sheet 页
            int listStartRowNum = -1;
            for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    for (int j = 0; j < row.getLastCellNum(); j++) {
                        Cell cell = row.getCell(j);
                        if (cell != null && Objects.equals(cell.getCellType(), STRING)) {
                            String cellValue = cell.getStringCellValue();
                            // 获取到列表数据所在行
                            if (listStartRowNum == -1 && cellValue.matches(REG_LIST)) {
                                listStartRowNum = i;
                            }

                            Object newValue = cellValue;
                            Matcher matcher = PATTERN.matcher(cellValue);
                            Matcher matcher1 = PATTERN_LIST.matcher(cellValue);
                            while (matcher.find()) {
                                String replaceExp = matcher.group();// 匹配到的表达式
                                String key = matcher.group(1);// 获取key
                                Object replaceValue = context.get(key);
                                if (replaceValue == null) {
                                    replaceValue = "";
                                }
                                if (replaceExp.equals(cellValue)) {// 单元格是一个表达式
                                    newValue = replaceValue;
                                } else {// 以字符串替换
                                    newValue = ((String) newValue).replace(replaceExp, replaceValue.toString());
                                }
                            }
                            while (matcher1.find()) {
                                String replaceExp = matcher1.group();// 匹配到的表达式
                                String key = matcher1.group(1) + "." + matcher1.group(2);// 获取key
                                Object replaceValue = context.get(key);
                                if (replaceValue == null) {
                                    replaceValue = "";
                                }
                                if (replaceExp.equals(cellValue)) {// 单元格是一个表达式
                                    newValue = replaceValue;
                                } else {// 以字符串替换
                                    newValue = ((String) newValue).replace(replaceExp, replaceValue.toString());
                                }
                            }
                            setCellValueOld(cell, newValue);

                        }

                    }

                }
            }
            // 公式生效
//            sheet.setForceFormulaRecalculation(true);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void setCellValueOld(Cell cell, Object value) {
        if (value instanceof Number) {// 如果是数字类型的设置为数值
            cell.setCellValue(Double.parseDouble(value.toString()));
        } else if (value instanceof Date) {// 如果为时间类型的设置为时间
            cell.setCellValue((Date) value);
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue(value.toString());
        }
    }

    public static void fileDownload(WaterBill waterBill, byte[] bytes) throws Exception {

        // 判断用户是要单月开票还是连月（1-2月）开票
        String excelFileName = waterBill.getCid() + "-" + waterBill.getCname() + "的" + waterBill.getTimes() + "收据.xlsx";

        HttpServletResponse response = HttpUtil.getResponse();
        response.setCharacterEncoding("UTF-8");
        // 第一步：设置响应类型
        response.setContentType("application/force-download");// 应用程序强制下载
        // 设置响应头，对文件进行url编码
        excelFileName = URLEncoder.encode(excelFileName, "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + excelFileName);
        response.setContentLength(bytes.length);
        // 第三步：老套路，开始copy
        OutputStream out = response.getOutputStream();
        out.write(bytes);
        out.flush();
        out.close();
    }


    /**
     * 导出excel：月度统计分组
     */
    public static void ExportExcel(Map<String, List<Map<?, ?>>> data, List<Map<String, Object>> groupCosts, String startDate, String endDate, String userNike) {
        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        Resource resource = resourceResolver.getResource("templates" + File.separator + "water_Statistics.xlsx");
        try (
                InputStream inputStream = resource.getInputStream();
                XSSFWorkbook xb = new XSSFWorkbook(inputStream);
        ) {
            data.forEach((sheetName, list) -> {
                int listRowsNum = 1;
                XSSFSheet sheetAt = xb.getSheetAt(1);
                XSSFSheet sheet = xb.createSheet(sheetName);
                ExcelUtil.copySheet(xb, sheetAt, sheet, sheetAt.getFirstRowNum(), sheetAt.getLastRowNum());
                Pattern pattern = Pattern.compile(REG_);

                Row listStartRow = sheet.getRow(listRowsNum);
                if (list.isEmpty()) {
                    for (int i = 0; i < listStartRow.getLastCellNum(); i++) {
                        Cell cell = listStartRow.getCell(i);
                        if (cell != null) {
                            cell.setCellValue("");
                        }
                    }
                } else {
                    int lastCellNum = listStartRow.getLastCellNum();
                    if (listRowsNum + 1 <= sheet.getLastRowNum()) {
                        // 列表数据行后面行下移，留出数据填充区域
                        sheet.shiftRows(listRowsNum + 1, sheet.getLastRowNum(), list.size(), true, false);
                    }
                    for (int i = 0; i < list.size(); i++) {
                        Map<?, ?> share = list.get(i);
                        // 保留表达式行
                        int newRowNum = listRowsNum + i + 1;
                        // 创建新行
                        Row newRow = sheet.createRow(newRowNum);
                        for (int j = 0; j < lastCellNum; j++) {
                            Cell cell = listStartRow.getCell(j);
                            if (cell != null) {
                                Cell newCell = newRow.createCell(j);
                                // 设置单元格格式
                                newCell.setCellStyle(cell.getCellStyle());
                                // 单元格是一个表达式
                                if (cell.getCellType() == STRING && cell.getStringCellValue().matches(REG_)) {
                                    String cellExp = cell.getStringCellValue();
                                    Matcher matcher = pattern.matcher(cellExp);
                                    matcher.find();
                                    // 获取key
                                    String key = matcher.group(1);
                                    if ("index".equals(key)) {
                                        ExcelUtil.setCellValue(newCell, String.valueOf(i + 1));
                                    } else {
                                        Object newValue = share.get(key);
                                        if (newValue == null) {
                                            newValue = "";
                                        }
                                        ExcelUtil.setCellValue(newCell, newValue);
                                    }
                                }
                            }
                        }
                    }
                    // 删除list表达式行
                    sheet.removeRow(listStartRow);
                    // 数据区域上移一行，覆盖表达式行
                    sheet.shiftRows(listRowsNum + 1, sheet.getLastRowNum(), -1, true, false);
                    // 合并单元格处理
                    for (int i = 0; i < lastCellNum; i++) {
                        CellRangeAddress mergedRangeAddress = ExcelUtil.getMergedRangeAddress(sheet, listRowsNum, i);
                        // 合并的单元格
                        if (mergedRangeAddress != null) {
                            i = mergedRangeAddress.getLastColumn();
                            for (int j = 1; j < list.size(); j++) {
                                int newRowNum = listRowsNum + j;
                                sheet.addMergedRegionUnsafe(new CellRangeAddress(newRowNum, newRowNum,
                                        mergedRangeAddress.getFirstColumn(), mergedRangeAddress.getLastColumn()));
                            }
                        }
                    }
                }
                sheet.setForceFormulaRecalculation(true);
            });

            int listRowsNum = 1;
            XSSFSheet sheet = xb.getSheetAt(0);
            Pattern pattern = Pattern.compile(REG_);
            Row listStartRow = sheet.getRow(listRowsNum);
            if (!groupCosts.isEmpty()) {
                int lastCellNum = listStartRow.getLastCellNum();
                if (listRowsNum + 1 <= sheet.getLastRowNum()) {
                    // 列表数据行后面行下移，留出数据填充区域
                    sheet.shiftRows(listRowsNum + 1, sheet.getLastRowNum(), groupCosts.size(), true, false);
                }
                for (int i = 0; i < groupCosts.size(); i++) {
                    Map<?, ?> share = groupCosts.get(i);
                    // 保留表达式行
                    int newRowNum = listRowsNum + i + 1;
                    // 创建新行
                    Row newRow = sheet.createRow(newRowNum);
                    for (int j = 0; j < lastCellNum; j++) {
                        Cell cell = listStartRow.getCell(j);
                        if (cell != null) {
                            Cell newCell = newRow.createCell(j);
                            // 设置单元格格式
                            newCell.setCellStyle(cell.getCellStyle());
                            // 单元格是一个表达式
                            if (cell.getCellType() == STRING && cell.getStringCellValue().matches(REG_)) {
                                String cellExp = cell.getStringCellValue();
                                Matcher matcher = pattern.matcher(cellExp);
                                matcher.find();
                                // 获取key
                                String key = matcher.group(1);
                                if ("index".equals(key)) {
                                    ExcelUtil.setCellValue(newCell, String.valueOf(i + 1));
                                } else {
                                    Object newValue = share.get(key);
                                    if (newValue == null) {
                                        newValue = "";
                                    }
                                    ExcelUtil.setCellValue(newCell, newValue);
                                }
                            }
                        }
                    }
                }
                // 删除list表达式行
                sheet.removeRow(listStartRow);
                // 数据区域上移一行，覆盖表达式行
                sheet.shiftRows(listRowsNum + 1, sheet.getLastRowNum(), -1, true, false);
                // 合并单元格处理
                for (int i = 0; i < lastCellNum; i++) {
                    CellRangeAddress mergedRangeAddress = ExcelUtil.getMergedRangeAddress(sheet, listRowsNum, i);
                    // 合并的单元格
                    if (mergedRangeAddress != null) {
                        i = mergedRangeAddress.getLastColumn();
                        for (int j = 1; j < groupCosts.size(); j++) {
                            int newRowNum = listRowsNum + j;
                            sheet.addMergedRegionUnsafe(new CellRangeAddress(newRowNum, newRowNum,
                                    mergedRangeAddress.getFirstColumn(), mergedRangeAddress.getLastColumn()));
                        }
                    }
                }
            }
            sheet.setForceFormulaRecalculation(true);

            String tempFileName = "月度统计-" + userNike + "-" + startDate.substring(0, 10) + "-" + endDate.substring(0,10) + ".xlsx";
            HttpServletResponse response = HttpUtil.getResponse();
            response.setContentType("application/force-download");// 应用程序强制下载
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(tempFileName, "UTF-8"));
            xb.removeSheetAt(1);
            xb.write(response.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 新增sheet，并且复制sheet内容到新增的sheet里
     */
    public static void copySheet(XSSFWorkbook wb, XSSFSheet fromsheet, XSSFSheet newSheet, int firstrow, int lasttrow) {
        // 复制一个单元格样式到新建单元格
        if ((firstrow == -1) || (lasttrow == -1) || lasttrow < firstrow) {
            return;
        }
        // 复制合并的单元格
        CellRangeAddress region = null;
        for (int i = 0; i < fromsheet.getNumMergedRegions(); i++) {
            region = fromsheet.getMergedRegion(i);
            if ((region.getFirstRow() >= firstrow) && (region.getLastRow() <= lasttrow)) {
                newSheet.addMergedRegion(region);
            }
        }
        Row fromRow = null;
        Row newRow = null;
        Cell newCell = null;
        Cell fromCell = null;
        // 设置列宽
        for (int i = firstrow; i <= lasttrow; i++) {
            fromRow = fromsheet.getRow(i);
            if (fromRow != null) {
                for (int j = fromRow.getLastCellNum(); j >= fromRow.getFirstCellNum(); j--) {
                    int colnum = fromsheet.getColumnWidth((short) j);
                    if (colnum > 100) {
                        newSheet.setColumnWidth((short) j, (short) colnum);
                    }
                    if (colnum == 0) {
                        newSheet.setColumnHidden((short) j, true);
                    } else {
                        newSheet.setColumnHidden((short) j, false);
                    }
                }
            }
        }
        // 复制行并填充数据
        for (int i = 0; i <= lasttrow; i++) {
            fromRow = fromsheet.getRow(i);
            if (fromRow == null) {
                continue;
            }
            newRow = newSheet.createRow(i - firstrow);
            newRow.setHeight(fromRow.getHeight());
            for (int j = fromRow.getFirstCellNum(); j < fromRow.getPhysicalNumberOfCells(); j++) {
                fromCell = fromRow.getCell((short) j);
                if (fromCell == null) {
                    continue;
                }
                newCell = newRow.createCell((short) j);
                newCell.setCellStyle(fromCell.getCellStyle());
                setCellValue(newCell, fromCell);
            }
        }
    }

    public static void setCellValue(Cell newCell, Cell fromCell) {
        CellType cellType = fromCell.getCellType();
        switch (cellType) {
            case STRING:
                newCell.setCellValue(fromCell.getRichStringCellValue());
                break;
            case NUMERIC:
                newCell.setCellValue(fromCell.getNumericCellValue());
                break;
            case FORMULA:
                newCell.setCellValue(fromCell.getCellFormula());
                break;
            case BOOLEAN:
                newCell.setCellValue(fromCell.getBooleanCellValue());
                break;
            case ERROR:
                newCell.setCellValue(fromCell.getErrorCellValue());
                break;
            default:
                newCell.setCellValue(fromCell.getRichStringCellValue());
                break;
        }
    }

    public static void setCellValue(Cell cell, Object value) {
        if (value instanceof Number) {// 如果是数字类型的设置为数值
            cell.setCellValue(Double.parseDouble(value.toString()));
//        } else if (value instanceof Date) {// 如果为时间类型的设置为时间
//            cell.setCellValue((Date) value);
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue(value.toString());
        }
    }

    /**
     * 获取指定行/列的合并单元格区域
     *
     * @param sheet
     * @param row
     * @param column
     * @return CellRangeAddress 不是合并单元格返回null
     */
    public static CellRangeAddress getMergedRangeAddress(Sheet sheet, int row, int column) {
        List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
        for (CellRangeAddress cellAddresses : mergedRegions) {
            if (row >= cellAddresses.getFirstRow() && row <= cellAddresses.getLastRow()
                    && column >= cellAddresses.getFirstColumn() && column <= cellAddresses.getLastColumn()) {
                return cellAddresses;
            }
        }
        return null;
    }
}