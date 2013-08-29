package com.blogfein.html.lexer;
import java.io.*;
import java.net.URL;
import java.util.*;

import com.blogfein.html.elements.*;
import com.blogfein.html.page.Page;

public class Lexer {
	
	private Page page = null;
	private TagNode latestTag;
	private int currentIndex = 0;
	
	public Lexer(File sourceFile) throws IOException
	{
		this.page = new Page(sourceFile);
		this.currentIndex = 0;
	}
	
	public Lexer(Page page)
	{
		this.page = page;
		this.currentIndex = 0;
	}
	
	public Node getNextToken()
	{
		
		this.currentIndex ++;
		char ch;
		while((ch=this.page.getNextChar()) != (char)Page.EOF)
		{
			// processing of the 'Script' value
			
			if(this.latestTag != null)
			{
				//System.out.println("latest Tag :" + this.latestTag.getTagName());
				if(this.latestTag.getTagName().equalsIgnoreCase("script"))
				{
					//System.out.println("========= Script Value Parsing..");
					this.page.setCursor(-1);
					//08.8.2 ���� ---------------------------------------------
					//TextNode node = new TextNode(this.page, this.latestTag);
					ScriptValueNode node = new ScriptValueNode(this.page, this.latestTag);
					node.parse();
					this.latestTag = null;
					node.setIndex(this.currentIndex);
					return node;
				}
			}
						
			if(ch == '<')
			{
				
				char chTemp;
				int indexTemp = -1;
				while((chTemp = this.page.getChar((indexTemp = this.page.getCursor()))) == ' ');
				if(this.page.getChar(indexTemp) == '!')	// �ּ�ó��
				{
					this.page.setCursor(-1);
					CommentNode node = new CommentNode(this.page);
					node.parse();
					node.setIndex(this.currentIndex);
					return node;
				}
				else
				{
					this.page.setCursor(-1);
					TagNode node = this.parseTag();
					if(!node.isClosedTag())	this.latestTag = node;
					else this.latestTag = null;
					node.setIndex(this.currentIndex);
					return node;
				}
			}
			else if(ch == ' ' || ch == '\n' || ch == '\r')
			{
				// ���� lineCh�� ����
				;
			}
			else //if(Character.isLetterOrDigit(ch))
			{
				Node node = null;
				// script�� �ּ�ó�� �ʿ�
				this.page.setCursor(-1);
				if(this.latestTag != null)
				{
					//System.out.println("latest Tag :" + this.latestTag.getTagName());
					if(this.latestTag.getTagName().equalsIgnoreCase("script"))
					{
						node = new TextNode(this.page, this.latestTag);
					}
					else
						node = new TextNode(this.page, null);
				}
				else
				{
					node = new TextNode(this.page, null);
				}
				node.parse();
				node.setIndex(this.currentIndex);
				return node;
			}
			
			
		}
		return null;
	}
	
	private TagNode parseTag()
	{
		TagNode tagNode = new TagNode(this.page);
		tagNode.parse();
		return tagNode;
	}
	
	public static void main(String...v)
	{
		try
		{			
			//URL url_source = new URL("http://news.naver.com/sports/index.nhn?category=baseball&ctg=news&mod=read&office_id=073&article_id=0001965340");
			URL url_source = new URL("http://www.naver.com");
			Page page = new Page(url_source, 3000);
			Lexer lexer = new Lexer(page);
			
			Node node = null;
			while((node = lexer.getNextToken()) != null)
			{
				if(node instanceof TagNode)
				{
					String isClosed = "";
					TagNode tag = (TagNode)node;
					if(tag.isClosedTag()) isClosed = "/";
					System.out.println("tag>" + tag);
				}
				else if(node instanceof TextNode)
				{
					TextNode txNode = (TextNode)node;
					System.out.println("text>" + txNode.getParsedText());
				}
				else if(node instanceof ScriptValueNode)
				{
					ScriptValueNode txNode = (ScriptValueNode)node;
					System.out.println("scriptValue>" + txNode.getParsedText());
				}
				else if(node instanceof CommentNode)
				{
					CommentNode cmNode = (CommentNode)node;
					System.out.println("comment>" + cmNode.getCommentString());
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
