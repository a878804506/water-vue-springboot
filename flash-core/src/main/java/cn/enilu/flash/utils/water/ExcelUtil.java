package cn.enilu.flash.utils.water;

import cn.enilu.flash.bean.entity.water.WaterBill;
import cn.enilu.flash.utils.HttpUtil;
import org.apache.poi.ss.usermodel.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelUtil {

    // 匹配"{exp}"
    private static final String REG = "\\{([a-zA-Z_]+)\\}";
    // 匹配"{xxx.exp}"
    private static final String REG_LIST = "\\{([a-zA-Z_]+)\\.([a-zA-Z_]+)\\}";

    private static final java.util.regex.Pattern PATTERN = java.util.regex.Pattern.compile(REG);
    private static final java.util.regex.Pattern PATTERN_LIST = Pattern.compile(REG_LIST);

    /**
     * 根据模板生成Excel文件
     *
     * @param context      表头或表尾数据集合
     * @return
     */
    public static byte[] writeExcel(Map<String, Object> context) throws Exception {

//        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
//        Resource resource = resourceResolver.getResource("classpath:templates" + File.separator + "water_template.xlsx");

        ClassPathResource classPathResource = new ClassPathResource("templates" + File.separator + "water_template.xlsx");
        InputStream inputStream =classPathResource.getInputStream();

        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);// 获取配置文件sheet 页
            int listStartRowNum = -1;
            for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    for (int j = 0; j < row.getLastCellNum(); j++) {
                        Cell cell = row.getCell(j);
                        if (cell != null && Objects.equals(cell.getCellType(), CellType.STRING)) {
                            String cellValue = cell.getStringCellValue();
                            // 获取到列表数据所在行
                            if (listStartRowNum == -1 && cellValue.matches(REG_LIST)) {
                                listStartRowNum = i;
                            }

                            Object newValue = cellValue;
                            Matcher matcher = PATTERN.matcher(cellValue);
                            Matcher matcher1 = PATTERN_LIST.matcher(cellValue);
                            while(matcher.find()){
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
                            while(matcher1.find()){
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
                            setCellValue(cell, newValue);

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

    private static void setCellValue(Cell cell, Object value) {
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

    public static void fileDownload( WaterBill waterBill, byte[] bytes) throws Exception {

        // 判断用户是要单月开票还是连月（1-2月）开票
        String excelFileName = waterBill.getCid() + "-" + waterBill.getCname() + "的" + waterBill.getYear() + "年" + (waterBill.getMonth() == 13 ? "1-2" : waterBill.getMonth()) + "月收据.xlsx";

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
}