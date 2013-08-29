package com.blogfein.util.log;
import java.util.*;

public class XLogger {
	
	private Class cls_instance = null;
	
	private XLogger(Class clz)
	{
		cls_instance = clz;
	}
	
	public static void printInfo(String info)
	{
		System.out.println(info);
	}
	
	public static XLogger getLogger(Class clazz)
	{
		XLogger logger = new XLogger(clazz);
		
		return logger;
	}
	
	public void addLoggerPlugin()
	{
		return ;
	}
	
	synchronized private void print(String msg)
	{
		//TODO ��ϵ� plugin ���� ����ϴ� ��Ŀ䱸
		// ��� level ���� 
		System.out.println(msg);
	}
	
	private static String getTime()
	{
		return new Date(System.currentTimeMillis()).toLocaleString();
	}
	
	public void info(String msg)
	{
		this.print("[INFO_" +this.cls_instance.getName() + "_" + getTime() + "]" + msg);
	}
	
	public void println(String msg)
	{
		this.print(msg);
	}
	
	public void debug()
	{
		
	}
	
	public void error()
	{
		
	}
	
	public static void main(String...v)
	{
		XLogger logger = XLogger.getLogger(XLogger.class);
		logger.info("���d�d");
	}
}
