package com.blogfein.html.parser;
import com.blogfein.html.elements.*;
import java.util.*;

public class CheckStack {
	
	private Vector<Node> v_data;
	private int v_pointer = 0;
	
	public CheckStack()
	{
		this.v_data = new Vector<Node>();
	}
	
	public boolean empty()
	{
		if(this.v_data.size() == 0) return true;
		return false;
	}
	
	public void push(Node node)
	{
		this.v_data.add( node);
	}
	
	public Node pop()
	{
		Node node = this.v_data.lastElement();
		this.v_data.removeElementAt(this.v_data.size()-1);
		return node;
	}
	/**
	 * get the top of data in the stack
	 * @return
	 */
	public Node peek()
	{
		if(this.v_data.size() > 0) 
			return this.v_data.get(this.v_data.size() - 1);
		return null;
	}
	
	public Node checkNode2(TagNode tagNode)
	{
		Node retNode = null;
		int index = 0;
		Node popNode =null; 
		int count = 0;
		while((popNode = this.getNode(index++)) != null)
		{
			if(popNode instanceof TagNode)
			{
				TagNode tNode = (TagNode)popNode;
				//System.out.println("S:Target="+tNode.getTagName() + ":" + tagNode.getTagName());
				if(tNode.getTagName().equalsIgnoreCase(tagNode.getTagName()))
				{
					for(int i=0;i<=count;i++)
						retNode =  this.pop();
					// ���� stack ���� ���
					//System.out.println("current:" + tagNode);
					//this.display();
					break;
				}
				else
				{	// ���簪�� �ٸ� ���� �ִ� ���..
					count++;				
					continue;
				}
				
			}
			else
			{
				System.out.println("�̻����..");
			}
			
		}
		//this.display();
		return retNode;
	}
	
	public Node checkNode(TagNode tagNode)
	{
		Node retNode = null;
		int index = 0;
		Node popNode =null; 
		int count = 0;
		while((popNode = this.getNode(index++)) != null)
		{
			if(popNode instanceof TagNode)
			{
				TagNode tNode = (TagNode)popNode;
				//System.out.println("S:Target="+tNode.getTagName() + ":" + tagNode.getTagName());
				if(tNode.getTagName().equalsIgnoreCase(tagNode.getTagName()))
				{
					for(int i=0;i<=count;i++)
						retNode =  this.pop();
					break;
				}
				else
				{
					count++;
					continue;
				}
			}
			else
			{
				System.out.println("�̻����..");
			}
		}
		//this.display();
		return retNode;
	}
	
	/**
	 * if input value is out of range, return null 
	 * @param offset '0' is current value
	 * @return
	 */
	public Node getNode(int offset)
	{
		int index =this.v_data.size() -1 - offset;
		if(index < 0) return null;
		return this.v_data.get(index);
	}
	
	public void display()
	{
		System.out.println("---------");
		for(Node node : this.v_data)
		{
			if(node instanceof TagNode)
			{
				TagNode tNode = (TagNode)node;
				System.out.println("|" + tNode.getTagName() + "\t|");
			}
			else
			{
				System.out.println("<"+node.getClass().getCanonicalName()+">");
			}
		}
		System.out.println("---------");
	}
}
