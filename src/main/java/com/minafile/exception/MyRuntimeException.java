package com.minafile.exception;

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
