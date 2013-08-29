package com.blogfein.html.elements;

import com.blogfein.html.page.Page;

public class ScriptValueNode extends Node{
	private Page page = null;
	private TagNode parentTagNode = null;
	protected String parsedText;
	
	public ScriptValueNode(Page page, TagNode node)
	{
		super();
		this.page = page;
		this.parentTagNode = node;
	}
	
	public void parse()
	{
		//System.out.println("parsing text node..");
		StringBuffer sbResult = new StringBuffer();
		this.parsedText = "";
		char ch;
		while((ch = this.page.getNextChar()) != (char)Page.EOF)
		{
			//System.out.println("TX>>" + this.page.getCursor());
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
						//System.out.println("script check>" + strPreCon);
						if(strPreCon.indexOf("script") > -1 ||
								strPreCon.indexOf("SCRIPT") > -1)
						{
							this.page.setCursor(-1);
							this.parsedText = sbResult.toString();
							return ;
						}
						else
						{
							//this.parsedText += ch;
							sbResult.append(ch);
						}
					}
					
				}
				else	// �Ϲ� Tag�� ����ó��
				{
					this.page.setCursor(-1);
					return ;
				}
			}
			else
			{
				//this.parsedText += ch;
				sbResult.append(ch);
			}
		}
	}
	
	public String getParsedText()
	{
		return this.parsedText;
	}
	
	public String toString()
	{
		return "[ScriptTEXT:"+this.index+"]" + this.parsedText;
	}
}
