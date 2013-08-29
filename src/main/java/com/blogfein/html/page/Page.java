package com.blogfein.html.page;
import java.util.*;
import java.io.*;
import java.net.*;

import org.apache.log4j.*;

public class Page {
	
	static Logger logger = Logger.getLogger(Page.class);
	
	public static int EOF = -1;
	private StringBuffer sBuf = null;
	private int cursor;
	private boolean isValid = false;
	
	public String strSrcURI;
	
	public Page(File sourceFile) throws IOException
	{
		this.strSrcURI = sourceFile.getAbsolutePath();
		
		this.isValid = false;
		this.initCursor();
		this.loadDataFile(sourceFile);
	}	
	
	public Page(URL url, int timeout) throws Exception
	{
		this.strSrcURI = url.toString();
		
		this.isValid = false;
		this.initCursor();
		this.loadDataFromURL(url, timeout);
	} 
	
	public void initCursor()
	{
		this.cursor = 0;
	}
	
	public void printBufferData()
	{
		for(int i=0;i<this.sBuf.length();i++)
			System.out.print(this.sBuf.charAt(i));
	}
	
	private void loadDataFromURL(URL url, int timeout) throws IOException
	{
		logger.info("URL:" + url);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		//System.out.println("Contents Type : " + conn.getContentType());
		//System.out.println("URL Content Encoding : " + conn.getContentEncoding());
		String ct = conn.getContentType(), charset = "euc-kr";
		if(ct.indexOf("charset=") > 0)
			charset = ct.substring("charset=".length()+ct.indexOf("charset="));
		
		
		
		conn.setConnectTimeout(timeout);
		conn.setInstanceFollowRedirects(true);
		String contentType = conn.getContentType();
		
		if(contentType != null && contentType.startsWith("text/html"))
		{
			InputStreamReader isr = new InputStreamReader(conn.getInputStream(), charset);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			this.sBuf = new StringBuffer();
			while((line = br.readLine()) != null)
			{
				//System.out.println("download>" + line);
				this.sBuf.append(line + "\r\n");
			}
			
			this.sBuf.trimToSize();
			br.close();
			
			this.isValid = true;
		}
		else
		{
			throw new IOException("Invalid URL :" + url);
		}
	}
	
	public static String getAbsURL(String baseURL, String indexURL)
	{
		String strRet = baseURL;
		try
		{
			if(indexURL == null) return baseURL;
			
			if(!baseURL.trim().endsWith("/"))
			{
				strRet += "/";
			}
			
			if(indexURL.startsWith("/"))
			{
				strRet += indexURL.substring(1);
			}
			else if(indexURL.startsWith("http://"))
			{
				strRet = indexURL;
			}
			else
			{
				strRet += indexURL;
			}
		}catch(Exception e)
		{
			System.out.println("BaseURL :" + baseURL + ", IndexURL :" + indexURL);
			e.printStackTrace();
		}
		return strRet;
	}
	
	private void loadDataFile(File file) throws IOException
	{
		this.cursor = 0;
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = null;
		this.sBuf = new StringBuffer();
		while((line=br.readLine()) != null)
		{
			//System.out.println("source>" + line);
			this.sBuf.append(line + "\r\n");
		}
		this.sBuf.trimToSize();
		br.close();
		
		this.isValid = true;
	}
	
	public char getCurrentChar()
	{
		return this.sBuf.charAt(this.cursor);
	}
		
	public char getNextChar()
	{
		//System.out.println("sBuf capacity :" + sBuf.capacity());
		if(this.sBuf != null && this.sBuf.length() > 0)
		{
			if(this.cursor < sBuf.capacity())
				return this.sBuf.charAt(this.cursor++);
			else
			{
				//System.out.println("cursor : capa = " + this.cursor + " : " + sBuf.capacity());
				return (char)EOF;
			}
		}
		else
		{
			return (char)EOF;
		}
	}
	
	public int getCursor()
	{
		return this.cursor;
	}
	
	public char getChar(int index)
	{
		return this.sBuf.charAt(index);
	}
	
	public String getSubString(int start, int end)
	{
		if(end > start)
		{
			String strRet = "";
			for(int i=start; i <= end; i++)
				strRet += getChar(i);
			
			return strRet;
		}
		else
		{
			return null;
		}
	}
	
	public boolean setCursor(int offset)
	{
		if(this.cursor+offset >= 0 
				&& this.cursor+offset < this.sBuf.capacity())
		{
			this.cursor += offset;
			return true;
		}
		return false;
	}
	
	public static void main(String...v)
	{
		/*
		String ct = "text/html; charset=utf-8";
		if(ct.indexOf("charset=") > 0)
		{
			System.out.println(">>>" + ct.substring("charset=".length()+ct.indexOf("charset=")));
		}
		*/
		
		try {
			//File sourceFile = new File("d:\\naverSample.html");
			//Page page = new Page(sourceFile);
			
			URL url = new URL("https://open.keb.co.kr/OPFXFR010001.web?schID=opb&mID=OPFXFR0101");
			Page page = new Page(url, 3000);
						
			char ch = 0;
			logger.debug("Get chars..");
			while((ch = page.getNextChar()) != (char)EOF)
			{
				System.out.print(ch);
			}
		} catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
