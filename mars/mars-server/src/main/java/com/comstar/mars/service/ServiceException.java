/*
 * ServiceException.java 2014-7-10
 */
package com.comstar.mars.service;

/**
 * Service层的异常基类
 * 
 * @author Li,Rong
 * @version 1.0
 */
public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = -1013025865487470867L;

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
