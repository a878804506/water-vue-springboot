package cn.enilu.flash.bean.entity.water;

import lombok.Data;

/**
 * 客户水费账单详情
 * 开票 、 前端界面、 生成excel 会用到
 */
@Data
public class WaterBill implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    /**
     * 客户编号
     */
    private Integer cid;
    /**
     * 客户姓名
     */
    private String cname;
    /**
     * 当前年份
     */
    private Integer year;
    /**
     * 月份
     */
    private Integer month;
    /**
     * 上个月止码（本月的起码）
     */
    private Double firstNumber;
    /**
     * 这个月的止码
     */
    private Double lastNumber;
    /**
     * 本月用水量
     */
    private Double waterCount;
    /**
     * 单价
     */
    private Double price;
    /**
     * 用户住址
     */
    private String address;
    /**
     * 计量水费
     */
    private Double meterageCost;
    /**
     * 计量水费 拆分后的展示
     */
    private char[] charMeterageCost = new char[8];

    /**
     * 容量水费
     */
    private Double capacityCost;
    /**
     * 容量水费 拆分后的展示
     */
    private char[] charCapacityCost = new char[8];
    /**
     * 合计水费
     */
    private Double waterCost;
    /**
     * 合计水费 拆分后的展示
     */
    private char[] charWaterCost = new char[8];
    /**
     * 大写金额
     */
    private String capitalization;

    /**
     * 生成excel的名字
     */
    private String excelFileName;
}
