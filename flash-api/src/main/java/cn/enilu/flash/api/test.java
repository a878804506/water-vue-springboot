package cn.enilu.flash.api;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.servlet.ServletOutputStream;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {

    // 匹配"{exp}"
    private static final String REG = "\\{([a-zA-Z_]+)\\}";
    // 匹配"{xxx.exp}"
    private static final String REG_LIST = "\\{([a-zA-Z_]+)\\.([a-zA-Z_]+)\\}";

    private static final Pattern PATTERN = Pattern.compile(REG);
    private static final Pattern PATTERN_LIST = Pattern.compile(REG_LIST);


    public static void main(String[] args) throws Exception {
        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        Resource resource = resourceResolver.getResource("classpath:templates" + File.separator + "water_template.xlsx");
        System.out.println(resource.exists());
        if (resource.exists() && resource.isFile()){
            Map<String, Object> context = new HashMap<>();
            context.put("id","9527");
            context.put("title","这是标题");
            context.put("year","2022");
            context.put("month","7");
            context.put("cname","陈韵辉");
            context.put("address","这是地址");
            context.put("firstNumber","001");
            context.put("lastNumber","999");
            context.put("waterCount","10");
            context.put("price","1.7");
            context.put("meterage.wan","1");
            context.put("meterage.qian","2");
            context.put("meterage.bai","3");
            context.put("meterage.shi","4");
            context.put("meterage.yuan","5");
            context.put("meterage.jiao","6");
            context.put("meterage.fen","7");
            context.put("capacity.wan","11");
            context.put("capacity.qian","22");
            context.put("capacity.bai","33");
            context.put("capacity.shi","44");
            context.put("capacity.yuan","55");
            context.put("capacity.jiao","66");
            context.put("capacity.fen","77");
            context.put("capitalization","十万");
            context.put("cost.wan","111");
            context.put("cost.qian","222");
            context.put("cost.bai","333");
            context.put("cost.shi","444");
            context.put("cost.yuan","555");
            context.put("cost.jiao","666");
            context.put("cost.fen","777");


            byte[] bytes = test.writeExcel(resource.getInputStream(), context);

            File file0 = new File("C:\\Users\\admin\\Desktop\\testttttt.xlsx");

            try (
                    FileOutputStream bos = new FileOutputStream(file0);
            )
            {
                bos.write(bytes);
            }
        }
    }

    /**
     * 根据模板生成Excel文件
     *
     * @param inputStream  模版文件
     * @param context      表头或表尾数据集合
     * @return
     */
    public static byte[] writeExcel(InputStream inputStream, Map<String, Object> context) {
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
}
