package com.blogfein.html.parser;

import com.blogfein.html.elements.*;
import java.util.*;

public class ParsingFilter implements IParsingFilter {
	
	private ArrayList<String> al_tag = null;
	
	public ParsingFilter()
	{
		this.al_tag = new ArrayList<String>();
		this.loadTagData();
	}
	
	private void loadTagData()
	{
		this.al_tag.add("hr");
		this.al_tag.add("br");
		this.al_tag.add("img");
	}
	
	@Override
	public boolean isFiltered(Node node) {
		if(node instanceof TagNode)
		{
			TagNode tNode = (TagNode)node;
			String tagName = tNode.getTagName();
			tagName = tagName.trim();
			for(String tName : this.al_tag)
				if(tagName.equalsIgnoreCase(tName)) return true;
		}
		return false;
	}

}
