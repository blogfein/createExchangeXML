package com.blogfein.html.elements;

import com.blogfein.html.lexer.*;
import com.blogfein.html.page.Page;

public class TextNode extends Node{
	
	private Page page = null;
	private TagNode parentTagNode = null;
	protected String parsedText;
	
	public TextNode(Page page, TagNode node)
	{
		super();
		this.page = page;
		this.parentTagNode = node;
	}
	
	public void parse()
	{
		//System.out.println("parsing text node..");
		StringBuffer sb = new StringBuffer();
		this.parsedText = "";
		char ch;
		while((ch = this.page.getNextChar()) != (char)Page.EOF)
		{
			//System.out.println("TX>>" + ch);
			if(ch == '<')
			{
				if(this.parentTagNode != null)	// script value�� ���� �ּ�ó��
				{
					String tagName = this.parentTagNode.getTagName();
					if(tagName.equalsIgnoreCase("script"))
					{
						int startIndex = this.page.getCursor();
						String strPreCon = this.page
								.getSubString(startIndex, startIndex + "/script".length());
						//System.out.println("Script Check>" + strPreCon);
						if(strPreCon.indexOf("script") > -1 ||
								strPreCon.indexOf("SCRIPT") > -1)
						{
							this.page.setCursor(-1);
							this.parsedText = sb.toString();
							return ;
						}
						else
						{
							//this.parsedText += ch;
							sb.append(ch);
						}
					}
					
				}
				else	// �Ϲ� Tag�� ����ó��
				{
					this.page.setCursor(-1);
					this.parsedText = sb.toString();
					return ;
				}
			}
			else
			{
				//this.parsedText += ch;
				sb.append(ch);
			}
		}
	}
	
	public String getParsedText()
	{
		return this.parsedText;
	}
	
	public String toString()
	{
		return "[TEXT:"+this.index+"]" + this.parsedText;
	}
}
