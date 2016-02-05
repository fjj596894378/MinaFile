package com.minafile.exception;

/**
 * 自定义异常
 * @author king_fu
 *
 */
public class MyRuntimeException extends RuntimeException{

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 4919661694992129768L;

	public MyRuntimeException() {
		super();
	}

	public MyRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public MyRuntimeException(String message) {
		super(message);
	}

	public MyRuntimeException(Throwable cause) {
		super(cause);
	}

}
