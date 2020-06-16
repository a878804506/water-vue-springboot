package cn.enilu.flash.bean.exception;

public class MyExcelException extends Exception {

	public MyExcelException(String msg) {
		super(msg);
	}

	public MyExcelException(String msg, Throwable e) {
		super(msg, e);
	}
}
