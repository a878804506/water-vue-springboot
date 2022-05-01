package cn.enilu.flash.code;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ParseExcelTest {

  public List<String[]> parseExcel(InputStream inputStream,String suffix,int startRow) throws IOException{
    //定义Excel对象变量
    Workbook workbook=null;
    //判断后缀 决定使用的解析方式，决定如何创建具体的对象
    if("xls".equals(suffix)) {
      //2003版的解析方式
      workbook=new HSSFWorkbook(inputStream);
    }else if("xlsx".equals(suffix)) {
      //2007
      workbook=new XSSFWorkbook(inputStream);
    }else {
      //未知内容
      return null;
    }

    List<String[]> result=new ArrayList<String[]>();

    for (int i = 0; i < 17; i++){
      //获取工作表，Excel分为若干个表，sheet
      Sheet sheet = workbook.getSheetAt(i);//得到第一个表格sheet
      if(sheet==null) {
        return null;
      }
      //获取表格中最后一行的行号
      int lastRowNum = sheet.getLastRowNum();
      //最后一行的行号大于startRow
      if(lastRowNum<=startRow) {
        return null;
      }
      //定义行变量和单元格变量
      Row row=null;
      Cell cell=null;
      //循环读取
      for (int rowNum = startRow; rowNum <= lastRowNum; rowNum++) {
        row=sheet.getRow(rowNum);
        //获取当前行的第一列和最后一列的标记
        short firstCellNum = row.getFirstCellNum();
        short lastCellNum = row.getLastCellNum();
        if(lastCellNum!=0) {
          String[] rowArray=new String[4];
          for(int cellNum=firstCellNum;cellNum<lastCellNum;cellNum++) {
            //拿到单元格的值
            cell=row.getCell(cellNum);
            //判断单元格是否有数据
            if(cell==null) {
              rowArray[cellNum]=null;
            }else {
              rowArray[cellNum]=parseCell(cell);
            }
          }
          rowArray[3] = sheet.getSheetName();
          result.add(rowArray);
        }
      }
    }
    return result;
  }
  /**
   * 解析单元格数据（返回字符串）
   */

  private String parseCell(Cell cell) {
    String cellStr=null;
    //判断单元格的类型
    switch (cell.getCellType()) {
      case STRING :
        //字符串类型单元格
        cellStr=cell.getRichStringCellValue().toString();
        break;
      case BLANK :
        //空数据
        cellStr="";
        break;
      case NUMERIC :
        //数字类型  包含日期、时间、数字
        //判断日期【年月日2016-12-20  | 时分10:20】类型
        if(HSSFDateUtil.isCellDateFormatted(cell)) {
          //判断具体类型，是日期还是时间
          SimpleDateFormat sdf=null;
          if(cell.getCellStyle().getDataFormat()== HSSFDataFormat.getBuiltinFormat("h:mm")) {
            //时间
            sdf=new SimpleDateFormat("HH:mm");
          }else {
            //日期
            sdf=new SimpleDateFormat("yyyy-MM-dd");
          }
          Date temp = cell.getDateCellValue();
          cellStr=sdf.format(temp);
        }else {
          //数字
          double temp=cell.getNumericCellValue();
          //数字格式化工具
          DecimalFormat format=new DecimalFormat();
          //查看单元格中的具体样式类型
          String formatStr=cell.getCellStyle().getDataFormatString();
          if(formatStr.equals("General")) {
            /**
             * 定义格式化正则
             * 保留一位小数 #.#
             * 保留两位小数#.##
             */
            format.applyPattern("#");
          }

          cellStr=format.format(temp);
        }
        break;
      default:
        cellStr="";

    }
    return cellStr;
  }

  @Test
  public void test1() throws Exception{
    InputStream inputStream=new FileInputStream("C:\\Users\\admin\\Desktop\\excel.xlsx");
    String suffix="xlsx";
    int startRow=1;
    List<String[]> result = parseExcel(inputStream, suffix, startRow);
    System.out.println("解析到的excel行数是：" + result.size());

    StringBuilder t_water_customer = new StringBuilder();
    t_water_customer.append("insert into t_water_customer(id, name, price, address, starttime, status, create_by, create_time) values ");

    StringBuilder t_water_watermeter = new StringBuilder();
    t_water_watermeter.append("insert into t_water_watermeter(id, cid, one, two, three, four, five, six, seven, eight, nine, ten, eleven, twelve, thirteen, create_by, create_time) values ");

    for(int i = 0; i < result.size() - 1; i++) {
      String[] obj = result.get(i);
//      System.out.println(Arrays.toString(obj));
      t_water_customer.append("(");
      t_water_customer.append((i + 1));
      t_water_customer.append(",");
      t_water_customer.append("'" + (Objects.nonNull(obj[0]) ? obj[0] : null) + "'" );
      t_water_customer.append(",");
      t_water_customer.append( 1.7 );
      t_water_customer.append(",");
      t_water_customer.append("'" + (Objects.nonNull(obj[3]) ? obj[3] : "")  +  "'");
      t_water_customer.append(",");
      t_water_customer.append("'" + (Objects.nonNull(obj[2]) ? obj[2] : "")   +  "'");
      t_water_customer.append(",");
      t_water_customer.append(1);
      t_water_customer.append(",");
      t_water_customer.append("1");
      t_water_customer.append(",");
      t_water_customer.append("'2022-04-11 20:19:00'");
      t_water_customer.append(")");
      if (i < result.size() -2) {
        t_water_customer.append(",");
      }

      t_water_watermeter.append("(");
      t_water_watermeter.append((i + 1));
      t_water_watermeter.append(",");
      t_water_watermeter.append((i + 1));
      t_water_watermeter.append(",");
      t_water_watermeter.append(0);
      t_water_watermeter.append(",");
      t_water_watermeter.append(0);
      t_water_watermeter.append(",");
      if (i == 944){
        System.out.println("------------------------------");
      }
      if (Objects.nonNull(obj[1]) && StringUtils.isNotEmpty(obj[1])) {
        t_water_watermeter.append(obj[1]); // 3月
      } else {
        t_water_watermeter.append(0); // 3月
      }

      t_water_watermeter.append(",");
      t_water_watermeter.append(0);
      t_water_watermeter.append(",");
      t_water_watermeter.append(0);
      t_water_watermeter.append(",");
      t_water_watermeter.append(0);
      t_water_watermeter.append(",");
      t_water_watermeter.append(0);
      t_water_watermeter.append(",");
      t_water_watermeter.append(0);
      t_water_watermeter.append(",");
      t_water_watermeter.append(0);
      t_water_watermeter.append(",");
      t_water_watermeter.append(0);
      t_water_watermeter.append(",");
      t_water_watermeter.append(0);
      t_water_watermeter.append(",");
      t_water_watermeter.append(0);
      t_water_watermeter.append(",");
      t_water_watermeter.append(0); // 13月
      t_water_watermeter.append(",");
      t_water_watermeter.append("1");
      t_water_watermeter.append(",");
      t_water_watermeter.append("'2022-04-11 20:19:00'");
      t_water_watermeter.append(")");
      if (i < result.size() -2 ) {
        t_water_watermeter.append(",");
      }

    }
    System.out.println("t_water_customer------------------------------");
    System.out.println(t_water_customer.toString());
    System.out.println("t_water_watermeter------------------------------");
    System.out.println(t_water_watermeter.toString());
  }

}
