package com.blogfein.html.elements;

import com.blogfein.html.page.Page;

import java.util.*;

public class TagNode extends Node{
	
	private String str_tagName;
	//private Vector<STagAttr> v_attrs = null;
	private Hashtable<String, String> v_attrs;
	
	private Page page = null;
	private ETagType cAttr = ETagType.NOT_PARSED;
	private boolean isClosed = false;
	// TODO ����ó���� �ʿ� <img src="aa.jpg"/> <- dlf
	private boolean endClosed = false;
	
	private char ch;
	
	public TagNode(Page page)
	{
		super();
		this.page = page;
		
		this.str_tagName = "";
	}
	
	public boolean isEndClosed()
	{
		return this.endClosed;
	}
	
	public String getTagName()
	{
		return this.str_tagName;
	}
	
	public Hashtable<String, String> getAttrs()
	{
		return this.v_attrs;
	}
	
	public void parse()
	{
		do
		{
			if(this.cAttr == ETagType.NOT_PARSED)
			{
				ch = page.getNextChar();
				if(ch == '<')
				{
					this.cAttr = ETagType.TAG_START;
				}
			}
			else if(this.cAttr == ETagType.TAG_START)
			{
				ch = page.getNextChar();
				this.parseTagName();
			}
			else if(this.cAttr == ETagType.TAG_END)
			{
				return ;
			}
		}while(cAttr != ETagType.TAG_END);
		
		if(this.cAttr == ETagType.TAG_END) this.page.setCursor(-1);
	}
	
	private void parseTagName()
	{
		do
		{
			if(this.cAttr == ETagType.TAG_START)
			{
				if(this.ch == '/')
				{
					this.isClosed = true;
				}
				else if(Character.isLetterOrDigit(this.ch))
				{
					this.cAttr = ETagType.TAG_NAME;
					this.str_tagName += this.ch;
				}
			}
			else if(this.cAttr == ETagType.TAG_NAME)
			{
				if(Character.isLetterOrDigit(this.ch))
				{
					this.cAttr = ETagType.TAG_NAME;
					this.str_tagName += this.ch;
				}
				else
				{
					// tag name�� ������ ��츸 ó��
					if(this.str_tagName.length() > 0)
					{
						if(this.ch == ' ')
						{
							this.cAttr = ETagType.TAG_NAME_END;
							this.parseAttrName();
						}
						else if(this.ch == '>')
						{
							this.cAttr = ETagType.TAG_END;
						}
						else if(this.ch == '/')
						{
							// auto closed tag ó��
							this.endClosed = true;
						}
					}
				}
			}
			
		}while((this.ch = this.page.getNextChar()) != (char)Page.EOF &&
				cAttr != ETagType.TAG_NAME_END &&
				cAttr != ETagType.TAG_END);
	}
	
	private void addAttr(String name, String value)
	{
		if(this.v_attrs == null)
			this.v_attrs = new Hashtable<String, String>();
		//this.v_attrs.add(new STagAttr(name, value));
		if(name == null) name = "NULL";
		if(value == null) value = "NULL";
		
		this.v_attrs.put(name, value);
	}
	
	private void parseAttrName()
	{
		String strAttrName = "";
		while(this.cAttr != ETagType.TAG_END &&
				(this.ch = this.page.getNextChar()) != (char)Page.EOF)
		{
			if(this.cAttr == ETagType.TAG_NAME_END)
			{
				if(Character.isLetterOrDigit(this.ch))
				{
					strAttrName += this.ch;
					this.cAttr = ETagType.ATTR_NAME;
				}
				else if(this.ch == ' ')
				{
					// ����� ����
					;
				}
				else if(this.ch == '/')
				{
					this.endClosed = true;
				}
				else if(this.ch == '>')
				{
					this.cAttr = ETagType.TAG_END;
					return ;
				}
			}
			else if(this.cAttr == ETagType.ATTR_NAME)
			{
				
				if(Character.isLetterOrDigit(this.ch))
				{
					strAttrName += this.ch;
				}
				else if(this.ch == ' ')
				{
					this.cAttr = ETagType.ATTR_NAME_END;
				}
				else if(this.ch == '=')
				{
					this.cAttr = ETagType.ATTR_NAME_END_HAVE_VALUE;
					this.addAttr(strAttrName, this.parseAttrValue());
				}
				else if(this.ch == '>')
				{
					this.cAttr = ETagType.TAG_END;
					this.addAttr(strAttrName, null);
					return;
				}
			}
			else if(this.cAttr == ETagType.ATTR_VALUE_END)
			{
				if(Character.isLetterOrDigit(this.ch))
				{
					//this.cAttr = ETagType.ATTR_NAME;
					this.setAttr(ETagType.ATTR_NAME);
					this.page.setCursor(-1);
					this.parseAttrName();	// recursive parsing
					/*
					if(this.cAttr == ETagType.TAG_END)
					{
						return ;
					}
					*/
				}
				else if(this.ch == '>')
				{
					this.cAttr = ETagType.TAG_END;
					return;
				}
			}
			else if(this.cAttr == ETagType.ATTR_NAME_END)
			{
				if(this.ch == '=')
				{
					this.cAttr = ETagType.ATTR_NAME_END_HAVE_VALUE;
					this.addAttr(strAttrName, this.parseAttrValue());
				}
				else if(this.ch == '>')
				{
					this.cAttr = ETagType.TAG_END;
					this.addAttr(strAttrName, null);
					return ;
				}
				else if(Character.isLetterOrDigit(this.ch))
				{
					this.addAttr(strAttrName, null);
					this.page.setCursor(-1);
					this.setAttr(ETagType.ATTR_NAME);
					this.parseAttrName();
				}
			}
			
		}
		
		
	}
	
	public boolean isClosedTag()
	{
		return this.isClosed;
	}
	
	private void setAttr(ETagType tag)
	{
		//System.out.println(">> change attr : " + tag);
		this.cAttr = tag;
	}
	
	private String parseAttrValue()
	{
		boolean isSqOpen = false, isDqOpen = false;
		String strAttrValue = "";
		StringBuffer sb = new StringBuffer();
		while((this.ch = this.page.getNextChar()) != (char)Page.EOF)
		{
			if(this.cAttr == ETagType.ATTR_NAME_END_HAVE_VALUE)
			{
				if(this.ch == '\'')
				{
					isSqOpen = true;
					this.cAttr = ETagType.ATTR_VALUE;
				}
				else if(this.ch == '\"')
				{
					isDqOpen = true;
					this.cAttr = ETagType.ATTR_VALUE;
				}
				else if(this.ch != ' ')
				{
					sb.append(this.ch);//strAttrValue += this.ch;
					this.cAttr = ETagType.ATTR_VALUE;
				}
			}
			else if(this.cAttr == ETagType.ATTR_VALUE)
			{
				if(this.ch == ' ')
				{
					if(isSqOpen || isDqOpen)
					{
						sb.append(this.ch);//strAttrValue += this.ch;
					}
					else
					{
						this.cAttr = ETagType.ATTR_VALUE_END;
						return sb.toString();//strAttrValue;
					}
				}
				else if(this.ch == '\'')
				{
					if(isSqOpen)
					{
						this.cAttr = ETagType.ATTR_VALUE_END;
						return sb.toString();//strAttrValue;
					}
					else
					{
						sb.append(this.ch);//strAttrValue += this.ch;
					}
				}
				else if(this.ch == '\"')
				{
					if(isDqOpen)
					{
						this.cAttr = ETagType.ATTR_VALUE_END;
						return sb.toString();//strAttrValue;
					}
					else
					{
						sb.append(this.ch);//strAttrValue += this.ch;
					}
				}
				else if(this.ch == '>')
				{
					this.setAttr(ETagType.TAG_END);
					return sb.toString();//strAttrValue
				}
				else
				{
					sb.append(this.ch);//strAttrValue += this.ch;
				}
			}
		}
		return sb.toString();//strAttrValue
	}
	
	public String toString()
	{
		String strRet = "[TAG:"+this.index+"]";
		
		if(this.isClosed) strRet += "/";
		
		strRet += this.str_tagName;
		if(this.getAttrs() != null)
		{
			//for(STagAttr attr : this.getAttrs()) strRet += "\n -" + attr.attrName + "=" + attr.attrValue;
			Enumeration<String> keys = this.v_attrs.keys();
			String key = "";
			while(keys.hasMoreElements())
			{
				key = keys.nextElement();
				strRet += "\n -" + key + " : " + this.v_attrs.get(key);
			}
			
		}
		if(this.endClosed) strRet += "/";
		return strRet;
	}
}
