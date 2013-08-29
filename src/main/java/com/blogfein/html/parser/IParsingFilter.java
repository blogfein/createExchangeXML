package com.blogfein.html.parser;
import com.blogfein.html.elements.*;

public interface IParsingFilter {
	/*
	 * if node is to be filtered, return true
	 */
	public boolean isFiltered(Node node);
}
