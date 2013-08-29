package com.blogfein.html.test;
import com.blogfein.util.log.*;
import com.blogfein.html.lexer.*;
import com.blogfein.html.elements.*;

import java.io.*;

public class TestLexer {
	
	public static void sample()
	{
		try {
			Lexer lexer = new Lexer(new File("d:\\testSample.html"));
			Node node = null;
			while((node = lexer.getNextToken()) != null)
			{
				if(node instanceof TagNode)
				{
					
				}
				else if(node instanceof TextNode)
				{
					
				}
				else if(node instanceof ScriptValueNode)
				{
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void ScriptFiltering()
	{
		System.out.println("ScriptFilter--------------------------------");
		try {
			Lexer lexer = new Lexer(new File("d:\\testSample.html"));
			Node node = null;
			while((node = lexer.getNextToken()) != null)
			{
				if(node instanceof TagNode)
				{
					TagNode tNode = (TagNode)node;
					XLogger.printInfo(tNode.toString());
				}
				else if(node instanceof TextNode)
				{
					TextNode txNode = (TextNode)node;
					XLogger.printInfo(txNode.toString());
				}
				else if(node instanceof ScriptValueNode)
				{
					ScriptValueNode scpNode = (ScriptValueNode)node;
					XLogger.printInfo(scpNode.toString());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String...v)
	{
		ScriptFiltering();
	}
	
}
