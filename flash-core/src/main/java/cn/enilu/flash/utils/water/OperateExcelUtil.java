package cn.enilu.flash.utils.water;

import cn.enilu.flash.bean.entity.water.WaterBill;
import cn.enilu.flash.utils.HttpUtil;
import jxl.SheetSettings;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.*;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

public class OperateExcelUtil {

    /**
     * 生成excel
     *
     * @param waterBill          月度账单
     * @param waterExcelRootPath 文件根目录位置
     * @return
     */
    public static String createExcelFile(WaterBill waterBill, String waterExcelRootPath) {
        String fileName = "";
        try {
            // 第一种字体样式
            WritableFont font1 = new WritableFont(WritableFont.createFont("黑体"), 12, WritableFont.BOLD);
            WritableCellFormat format1 = new WritableCellFormat(font1);
            format1.setBorder(Border.BOTTOM, BorderLineStyle.DOUBLE);
            format1.setAlignment(jxl.format.Alignment.CENTRE); // 把水平对齐方式指定为居中
            format1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); // 把垂直对齐方式指定为居中
            // 第二种字体样式
            WritableFont font2 = new WritableFont(WritableFont.TIMES, 12);
            WritableCellFormat format2 = new WritableCellFormat(font2);
            format2.setAlignment(jxl.format.Alignment.CENTRE); // 把水平对齐方式指定为居中
            // 第三种字体样式
            WritableFont font3 = new WritableFont(WritableFont.createFont("黑体"), 10);
            WritableCellFormat format3 = new WritableCellFormat(font3);
            format3.setWrap(true);// 自动换行
            format3.setBorder(Border.ALL, BorderLineStyle.THIN);
            format3.setAlignment(jxl.format.Alignment.CENTRE); // 把水平对齐方式指定为居中
            format3.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); // 把垂直对齐方式指定为居中
            // 第四种字体样式
            WritableFont font4 = new WritableFont(WritableFont.TIMES, 10);
            WritableCellFormat format4 = new WritableCellFormat(font4);
            format4.setWrap(true);// 自动换行
            format4.setBorder(Border.ALL, BorderLineStyle.THIN);
            format4.setAlignment(jxl.format.Alignment.CENTRE); // 把水平对齐方式指定为居中
            format4.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); // 把垂直对齐方式指定为居中
            // 第五种字体样式
            WritableFont font5 = new WritableFont(WritableFont.TIMES, 9);
            WritableCellFormat format5 = new WritableCellFormat(font5);
            format5.setWrap(true);// 自动换行
            format5.setBorder(Border.ALL, BorderLineStyle.THIN);
            format5.setAlignment(jxl.format.Alignment.CENTRE); // 把水平对齐方式指定为居中
            format5.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); // 把垂直对齐方式指定为居中
            // 第六种字体样式
            WritableFont font6 = new WritableFont(WritableFont.createFont("黑体"), 8);
            WritableCellFormat format6 = new WritableCellFormat(font6);
            format6.setBorder(Border.ALL, BorderLineStyle.THIN);
            format6.setAlignment(jxl.format.Alignment.CENTRE); // 把水平对齐方式指定为居中
            format6.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); // 把垂直对齐方式指定为居中
            // 第七种字体样式
            WritableFont font7 = new WritableFont(WritableFont.createFont("黑体"), 10);
            WritableCellFormat format7 = new WritableCellFormat(font7);
            format7.setBorder(Border.NONE, BorderLineStyle.THIN);
            format7.setAlignment(jxl.format.Alignment.CENTRE); // 把水平对齐方式指定为居中
            format7.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); // 把垂直对齐方式指定为居中
            // 第八种字体样式
            WritableFont font8 = new WritableFont(WritableFont.createFont("黑体"), 9);
            WritableCellFormat format8 = new WritableCellFormat(font8);
            format8.setBorder(Border.NONE, BorderLineStyle.THIN);
            format8.setAlignment(jxl.format.Alignment.CENTRE); // 把水平对齐方式指定为居中
            format8.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); // 把垂直对齐方式指定为居中
            // 第九种字体样式
            WritableFont font9 = new WritableFont(WritableFont.createFont("黑体"), 10, WritableFont.BOLD);
            WritableCellFormat format9 = new WritableCellFormat(font9);
            format9.setBorder(Border.ALL, BorderLineStyle.THIN);
            format9.setAlignment(jxl.format.Alignment.CENTRE); // 把水平对齐方式指定为居中
            format9.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); // 把垂直对齐方式指定为居中
            // 第十种字体样式
            WritableFont font10 = new WritableFont(WritableFont.createFont("黑体"), 8);
            WritableCellFormat format10 = new WritableCellFormat(font10);
            format10.setWrap(true);// 自动换行
            format10.setBorder(Border.ALL, BorderLineStyle.THIN);
            format10.setAlignment(jxl.format.Alignment.CENTRE); // 把水平对齐方式指定为居中
            format10.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); // 把垂直对齐方式指定为居中
            // 用户编号前面+“0”
            String cid = String.format("%04d", waterBill.getCid());
            WritableWorkbook workbook = null;

            // 判断文件夹是否存在
            String fullFilePath = waterExcelRootPath + System.getProperty("file.separator") + waterBill.getYear() + String.format("%02d", waterBill.getMonth());
            File filePath = new File(fullFilePath);
            if (!filePath.exists() && !filePath.isDirectory()) {
                System.out.println("文件夹不存在");
                filePath.mkdir();
            }
            File file = null;
            // 判断用户是要单月开票还是连月（1-2月）开票
            fileName = cid + "-" + waterBill.getCname() + "的" + waterBill.getYear() + "年" + (waterBill.getMonth() == 13 ? "1-2" : waterBill.getMonth()) + "月收据.xls";

            file = new File(fullFilePath + System.getProperty("file.separator") + fileName);
            // 若文件不存在
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                try {
                    // 创建文件
                    System.out.println("文件不存在");
                    file.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            workbook = Workbook.createWorkbook(file);
            // 把用户的名字作为工作表的第一页，参数0表示第一页
            WritableSheet sheet = workbook.createSheet("这里是第一页", 0);
            // 合并第一行
            sheet.mergeCells(0, 0, 12, 0);
            Label a = new Label(0, 0, "谷城县水利局紫金农水站水费收据", format1);
            sheet.addCell(a);
            // 合并第二行
            sheet.mergeCells(0, 1, 12, 1);
            Label b = null;
            b = new Label(0, 1, waterBill.getYear() + "年" + (waterBill.getMonth() == 13 ? "1-2" : waterBill.getMonth()) + "月", format2);
            sheet.addCell(b);
            Label c = new Label(0, 2, "用户", format3);
            sheet.addCell(c);
            // 合并BC列
            sheet.mergeCells(1, 2, 2, 2);
            Label d = new Label(1, 2, waterBill.getCname(), format9);
            sheet.addCell(d);
            Label e = new Label(3, 2, "住址", format3);
            sheet.addCell(e);
            // 合并E-L列
            sheet.mergeCells(4, 2, 11, 2);
            Label f = new Label(4, 2, StringUtils.isEmpty(waterBill.getAddress()) ? "紫金" : waterBill.getAddress(), format3);
            sheet.addCell(f);
            Label g = new Label(12, 2, "备注", format4);
            sheet.addCell(g);
            // 合并A4 A5
            sheet.mergeCells(0, 3, 0, 4);
            Label h = new Label(0, 3, "项目及类别", format3);
            sheet.addCell(h);
            // 合并B4 C4
            sheet.mergeCells(1, 3, 2, 3);
            Label i = new Label(1, 3, "水表动态", format3);
            sheet.addCell(i);
            Label j = new Label(1, 4, "起码", format3);
            sheet.addCell(j);
            Label k = new Label(2, 4, "止码", format3);
            sheet.addCell(k);
            // 合并D4 D5
            sheet.mergeCells(3, 3, 3, 4);
            Label l = new Label(3, 3, "用水量(立方米)", format5);
            sheet.addCell(l);
            // 合并E4 E5
            sheet.mergeCells(4, 3, 4, 4);
            Label m = new Label(4, 3, "单价", format6);
            sheet.addCell(m);
            // 合并F4-L4
            sheet.mergeCells(5, 3, 11, 3);
            Label n = new Label(5, 3, "金额", format3);
            sheet.addCell(n);
            Label n1 = new Label(5, 4, "万", format3);
            sheet.addCell(n1);
            Label n2 = new Label(6, 4, "千", format3);
            sheet.addCell(n2);
            Label n3 = new Label(7, 4, "百", format3);
            sheet.addCell(n3);
            Label n4 = new Label(8, 4, "十", format3);
            sheet.addCell(n4);
            Label n5 = new Label(9, 4, "元", format3);
            sheet.addCell(n5);
            Label n6 = new Label(10, 4, "角", format3);
            sheet.addCell(n6);
            Label n7 = new Label(11, 4, "分", format3);
            sheet.addCell(n7);
            Label o = new Label(0, 5, "计量水费", format6);
            sheet.addCell(o);
            Label p = new Label(1, 5, waterBill.getFirstNumber().toString(), format9); // 起码
            sheet.addCell(p);
            Label q = new Label(2, 5, waterBill.getLastNumber().toString(), format9); // 止码
            sheet.addCell(q);
            Label r = new Label(3, 5, waterBill.getWaterCount().toString(), format9); // 用水量
            sheet.addCell(r);
            Label s = new Label(4, 5, waterBill.getPrice().toString(), format6); // 单价
            sheet.addCell(s);
            Label t = new Label(5, 5, waterBill.getCharMeterageCost()[0] + "", format9); // 万
            sheet.addCell(t);
            Label t1 = new Label(6, 5, waterBill.getCharMeterageCost()[1] + "", format9); // 千
            sheet.addCell(t1);
            Label t2 = new Label(7, 5, waterBill.getCharMeterageCost()[2] + "", format9); // 百
            sheet.addCell(t2);
            Label t3 = new Label(8, 5, waterBill.getCharMeterageCost()[3] + "", format9); // 十
            sheet.addCell(t3);
            Label t4 = new Label(9, 5, waterBill.getCharMeterageCost()[4] + "", format9); // 元
            sheet.addCell(t4);
            Label t5 = new Label(10, 5, waterBill.getCharMeterageCost()[6] + "", format9); // 角
            sheet.addCell(t5);
            Label t6 = new Label(11, 5, waterBill.getCharMeterageCost()[7] + "", format9); // 分
            sheet.addCell(t6);
            Label t7 = new Label(0, 6, "容量水费", format6);
            sheet.addCell(t7);
            Label t8 = new Label(1, 6, "", format9); // 空格
            sheet.addCell(t8);
            Label t9 = new Label(2, 6, "", format9);
            sheet.addCell(t9);
            Label t0 = new Label(3, 6, "", format9);
            sheet.addCell(t0);
            Label t11 = new Label(4, 6, "", format9);
            sheet.addCell(t11);
            Label t12 = new Label(5, 6, waterBill.getCharCapacityCost()[0] + "", format9);
            sheet.addCell(t12);
            Label t13 = new Label(6, 6, waterBill.getCharCapacityCost()[1] + "", format9);
            sheet.addCell(t13);
            Label t14 = new Label(7, 6, waterBill.getCharCapacityCost()[2] + "", format9);
            sheet.addCell(t14);
            Label t15 = new Label(8, 6, waterBill.getCharCapacityCost()[3] + "", format9); // 拾
            sheet.addCell(t15);
            Label t16 = new Label(9, 6, waterBill.getCharCapacityCost()[4] + "", format9); // 元
            sheet.addCell(t16);
            Label t17 = new Label(10, 6, waterBill.getCharCapacityCost()[6] + "", format9); // 角
            sheet.addCell(t17);
            Label t18 = new Label(11, 6, waterBill.getCharCapacityCost()[7] + "", format9); // 分
            sheet.addCell(t18);
            Label v = new Label(0, 7, "合计大写", format6);
            sheet.addCell(v);
            // 合并B8-E8
            sheet.mergeCells(1, 7, 4, 7);
            Label v1 = new Label(1, 7, waterBill.getCapitalization(), format9);
            sheet.addCell(v1);
            Label v2 = new Label(5, 7, waterBill.getCharWaterCost()[0] + "", format9); // 万
            sheet.addCell(v2);
            Label v3 = new Label(6, 7, waterBill.getCharWaterCost()[1] + "", format9); // 千
            sheet.addCell(v3);
            Label v4 = new Label(7, 7, waterBill.getCharWaterCost()[2] + "", format9); // 百
            sheet.addCell(v4);
            Label v5 = new Label(8, 7, waterBill.getCharWaterCost()[3] + "", format9); // 十
            sheet.addCell(v5);
            Label v6 = new Label(9, 7, waterBill.getCharWaterCost()[4] + "", format9); // 元
            sheet.addCell(v6);
            Label v7 = new Label(10, 7, waterBill.getCharWaterCost()[6] + "", format9); // 角
            sheet.addCell(v7);
            Label v8 = new Label(11, 7, waterBill.getCharWaterCost()[7] + "", format9); // 分
            sheet.addCell(v8);
            // 合并备注单元格
            sheet.mergeCells(12, 3, 12, 7);
            Label w = new Label(12, 3, "", format6);
            sheet.addCell(w);

            Label x = new Label(0, 8, "合计：", format7);
            sheet.addCell(x);
            Label y = new Label(2, 8, "复核：", format7);
            sheet.addCell(y);
            Label z = new Label(4, 8, "收款：", format7);
            sheet.addCell(z);
            // 合并K8L8
            sheet.mergeCells(10, 8, 11, 8);
            Label z1 = new Label(10, 8, "开票：", format7);
            sheet.addCell(z1);

            Label num = new Label(12, 8, cid, format8);
            sheet.addCell(num);

            // 将第一行的高度设为200
            sheet.setRowView(0, 435);
            sheet.setRowView(1, 435); // 第二行高度
            sheet.setRowView(2, 435); // 第三行高度
            sheet.setRowView(3, 435);
            sheet.setRowView(4, 435);
            sheet.setRowView(5, 435);
            sheet.setRowView(6, 435);
            sheet.setRowView(7, 435);
            sheet.setRowView(8, 435);
            // 将第一列的宽度设为30
            sheet.setColumnView(0, 7); // A列
            sheet.setColumnView(1, 5); // B列
            sheet.setColumnView(2, 5); // C列
            sheet.setColumnView(3, 5); // D列
            sheet.setColumnView(4, 4); // E列
            sheet.setColumnView(5, 3);
            sheet.setColumnView(6, 3);
            sheet.setColumnView(7, 3);
            sheet.setColumnView(8, 3);
            sheet.setColumnView(9, 3);
            sheet.setColumnView(10, 3);
            sheet.setColumnView(11, 3); // L列
            sheet.setColumnView(12, 4); // M列

            SheetSettings sheetSettings = sheet.getSettings(); // 获取原sheet的属性
            sheetSettings.setLeftMargin(0.4); // 设置左边距
            sheetSettings.setTopMargin(0.4); // 设置上边距

            workbook.write();
            workbook.close();
        } catch (Exception e) {

        }
        return fileName;
    }

    public static void fileDownload( String waterExcelRootPath, String yearMonth, String excelFileName) throws Exception {
        HttpServletResponse response = HttpUtil.getResponse();
        response.setCharacterEncoding("UTF-8");
        // 第一步：设置响应类型
        response.setContentType("application/force-download");// 应用程序强制下载
        // 第二读取文件
        InputStream in = new FileInputStream(waterExcelRootPath + yearMonth + System.getProperty("file.separator") + excelFileName);
        // 设置响应头，对文件进行url编码
        excelFileName = URLEncoder.encode(excelFileName, "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + excelFileName);
        response.setContentLength(in.available());
        // 第三步：老套路，开始copy
        OutputStream out = response.getOutputStream();
        byte[] b = new byte[1024];
        int len = 0;
        while ((len = in.read(b)) != -1) {
            out.write(b, 0, len);
        }
        out.flush();
        out.close();
        in.close();
    }
}