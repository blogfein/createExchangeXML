package com.blogfein.html.elements;

public class STagAttr {
	public String attrName, attrValue;
	
	public STagAttr(String name, String value)
	{
		this.attrName = name;
		this.attrValue = value;
	}
	
	public String toString()
	{
		return this.attrName + "=" + this.attrValue;
	}
}
