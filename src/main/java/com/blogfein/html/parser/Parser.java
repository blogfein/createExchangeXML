package com.blogfein.html.parser;

import com.blogfein.html.lexer.*;
import com.blogfein.html.page.Page;
import com.blogfein.html.elements.*;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author being20c@gmail.com
 */
public class Parser {
	
	private Lexer lexer = null;
	private Vector<Node> v_Nodes, v_tokens;
	private CheckStack stack = null;
	private IParsingFilter filter = null;

	private static Integer checkdata = 0;
	private static Hashtable<Integer, String> xmlData;
	
	public Parser(File file) throws IOException
	{
		this.lexer = new Lexer(file);
		this.v_Nodes = new Vector<Node>();
	}

	public Parser()
	{
		main();
	}
	
	public Parser(Page page)
	{
		this.lexer = new Lexer(page);
		this.v_Nodes = new Vector<Node>(2);
		this.v_tokens = new Vector<Node>(5000);
	}
	
	public void setFilter(IParsingFilter filter)
	{
		this.filter = filter;
	}
	
	public Vector<Node> getTokenList()
	{
		return this.v_tokens;
	}
	
	public Node parse()
	{
		this.v_Nodes.clear();
		this.v_tokens.clear();
		
		this.stack = new CheckStack();
		Node node = null;
		while((node = lexer.getNextToken()) != null)
		{
			this.v_tokens.add(node);
			
			if(node instanceof TagNode)
			{
				//System.out.println("NODE>" + node);
				if(this.filter != null)
				{
					if(this.filter.isFiltered(node)) continue;
				}
								
				TagNode tag = (TagNode)node;
				if(tag.isClosedTag())	
				{
					stack.checkNode2(tag);
				}
				else	
				{
					if(this.stack.peek() == null)
					{
						this.v_Nodes.add(tag);
					}
					else
					{
						Node pNode = this.stack.peek();
						tag.setParentNode(pNode);
					}
					
					if(this.filter != null)
					{
						if(!tag.isEndClosed() && !this.filter.isFiltered(node)) stack.push(tag);	
					}
					else
					{
						if(!tag.isEndClosed()) stack.push(tag);
					}
				}
			}
			else if(node instanceof TextNode)
			{
				TextNode txNode = (TextNode)node;
				if(this.stack.peek() == null)
				{
					this.v_Nodes.add(txNode);
				}
				else
				{
					Node pNode = this.stack.peek();
					txNode.setParentNode(pNode);
				}
				//System.out.println("text>" + txNode.getParsedText());
			}
			else if(node instanceof CommentNode)
			{
				CommentNode cmNode = (CommentNode)node;
				//System.out.println("comment>" + cmNode.getCommentString());
			}
		}
		System.out.println("Final Stack state..");
		stack.display();
		return null;
	}
	
	public Vector<Node> getParsedList()
	{
		return this.v_Nodes;
	}
	
	public static void main(String...v)
	{
		// System.out.println("Parser ..");
		// File file = new File("https://open.keb.co.kr/OPFXFR010001.web?schID=opb&mID=OPFXFR0101");
		// File file = new File("d:\\test.html");
		try {
			//URL url_source = new URL("http://news.naver.com/sports/index.nhn?category=baseball&ctg=news&mod=read&office_id=073&article_id=0001965340");
			URL url_source = new URL("https://open.keb.co.kr/OPFXFR010001.web?schID=opb&mID=OPFXFR0101");
			Page page = new Page(url_source, 3000);
			
			Parser parser = new Parser(page);
			parser.parse();
			
			Vector<Node> v_node = parser.getParsedList();
			System.out.println("Parsing Nodes :" + v_node.size());
			for(Node node : v_node)
			{
				if(node instanceof TagNode)
				{
					TagNode tagNode =(TagNode)node;
					System.out.println("TagName >" + tagNode.getTagName());
					dspNode(tagNode);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		getXmlData();
	}

	private static Integer cnt1 = 0;
	public static void dspNode(Node node)
	{
		// System.out.println("[NODE]" + node);
		Vector<Node> v_subNodes = node.getSubNodes();
		if(v_subNodes != null)
		{
			for(Node sNode : v_subNodes)
			{
				if(sNode instanceof TagNode)
				{
					TagNode tNode = (TagNode)sNode;
					Pattern p1 = Pattern.compile("txt03 pdR03");
					Matcher m1 = p1.matcher(tNode.toString());
					Pattern p2 = Pattern.compile("-onclick : detail");
					Matcher m2 = p2.matcher(tNode.toString());
					
					if (m2.find()) {
						checkdata = 1;
						// System.out.println("TagNode>" + tNode);
						dspNode(sNode);	// recursive print
					} else if (m1.find()) {
						checkdata = 2;
						// System.out.println("TagNode>" + tNode);
						dspNode(sNode);	// recursive print
					} else {		
						// System.out.println("일치 갯수:"+cnt);
						checkdata = 0;
						dspNode(sNode);	// recursive print
						continue;
					}
				}
				else if(sNode instanceof TextNode)
				{					
					TextNode tNode = (TextNode)sNode;
					if (tNode.getParsedText().trim().equals("")) continue;
					if (checkdata == 2) {
						// System.out.println("TextNode> [" + tNode.getParsedText().trim()+"]");
						cnt1++;
						addAttr(cnt1, tNode.getParsedText().trim());
					} else if (checkdata == 1) {
						// System.out.println("TextNode> [" + currencyText +"]...[" + currencySign +"]");
						cnt1++;
						addAttr(cnt1, tNode.getParsedText().trim());
					}
				}
			}
		}
	}

	private static void addAttr(Integer name, String value)
	{
		if(xmlData == null)
			xmlData = new Hashtable<Integer, String>();
		//this.v_attrs.add(new STagAttr(name, value));
		if(name == null) name = 0;
		if(value == null) value = "NULL";
		// System.out.println("name:"+name+" / value:"+value);
		xmlData.put(name, value);
	}
	
	public static Hashtable<Integer, String> getXmlData()
	{
		Hashtable<Integer, String> xmlNewData = new Hashtable<Integer, String>();

        // Sort hashtable.
        Vector v = new Vector(xmlData.keySet());
        Collections.sort(v);

        // Display (sorted) hashtable.
        for (Enumeration e = v.elements(); e.hasMoreElements();) {
             Integer key = (Integer)e.nextElement();
             String val = (String)xmlData.get(key);              
             // System.out.println("Key: " + key + " Val: " + val);
             xmlNewData.put(key, val);
        }      
        // System.out.println( ">> " + xmlData );
		return xmlNewData;
	}
}
