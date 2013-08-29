package com.blogfein.html.elements;
import com.blogfein.html.lexer.*;
import com.blogfein.html.page.Page;
import java.util.*;

public abstract class Node {
	
	private Node parentNode = null;
	private Vector<Node> v_subNodes = null;
	
	protected Page page;
	protected String parsedToken;
	public ENodeType nodeType;
	
	protected int index;
	public Hashtable<String, Object> ht_property;
	public boolean marked= false;
	
	public Node()
	{
		this.parsedToken = "";
		this.ht_property = new Hashtable<String, Object>(1);
	}
	
	public int getIndex()
	{
		return this.index;
	}
	
	public void setIndex(int idx)
	{
		this.index = idx;
	}
	
	public abstract void parse();
	
	private void addSubNode(Node node)
	{
		if(this.v_subNodes == null)
			this.v_subNodes = new Vector<Node>();
		this.v_subNodes.add(node);
	}
	
	public Vector<Node> getSubNodes()
	{
		return this.v_subNodes;
	}
	
	public void setParentNode(Node node)
	{
		this.parentNode = node;
		this.parentNode.addSubNode(this);
	}
	
	public Node getParentNode()
	{
		return this.parentNode;
	}
	
	public String getToken()
	{
		return this.parsedToken;
	}
	
}
