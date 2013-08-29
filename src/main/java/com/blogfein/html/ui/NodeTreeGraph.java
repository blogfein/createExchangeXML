package com.blogfein.html.ui;
import com.blogfein.html.elements.*;

import java.awt.*;
import javax.swing.*;

import java.util.*;

public class NodeTreeGraph extends JPanel{
	
	private Node rootNode = null;
	
	public NodeTreeGraph(Node rootNode)
	{
		this.rootNode = rootNode;
		this.setSize(1200, 1200);
		
	}
	
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.RED);
		//new TagNodeShape(g, (TagNode)this.rootNode, 100, 100);
		
		drawNode(g, this.rootNode, 2000, 2000, 0);
		//this.drawGD(g);
	}
	
	
	public void drawGD(Graphics g)
	{
		int x = 200, y = 200;
		int size = 10;
		int length = 50;
		g.setColor(Color.red);
		g.fillOval(x, y, size, size);
		
		int x1 = (int)(length * Math.cos(Math.PI)) + x;	//180��
		int y1 = (int)(length * Math.sin(Math.PI)) + y;		
		
		g.drawOval(x1, y1, size, size);
		
		x1 = (int)(length * Math.cos(Math.PI/2)) + x;	//90��
		y1 = (int)(length * Math.sin(Math.PI/2)) + y;		
		
		g.setColor(Color.blue);
		g.drawOval(x1, y1, size, size);
		
		x1 = (int)(length * Math.cos(Math.PI/2*3)) + x;	//90��
		y1 = (int)(length * Math.sin(Math.PI/2*3)) + y;		
		
		g.setColor(Color.blue);
		g.drawOval(x1, y1, size, size);
		
		x1 = (int)(length * Math.cos(0)) + x;	//0��
		y1 = (int)(length * Math.sin(0)) + y;		
		
		g.setColor(Color.green);
		g.drawOval(x1, y1, size, size);
		
	}
	
	public void drawNode(Graphics g, Node node, int x, int y, double pAngle)
	{
		int length = 0;
		double baseOffset = 0;
		if(node instanceof TagNode)
		{
			TagNode tNode = (TagNode)node;
			double temp = new TagNodeShape(g, tNode, x, y, pAngle).parentAngle;
			baseOffset = temp;
		}
		else if(node instanceof TextNode)
		{
			TextNode txNode = (TextNode)node;
			double temp = new TextNodeShape(g, txNode, x, y, pAngle).parentAngle;
			baseOffset = temp;
		}
		
		Vector<Node> subNodes = node.getSubNodes();
		
		//y += 20;
		if(subNodes != null)
		{
			int size = subNodes.size();
			int count = 0;
			for(Node sNode : subNodes)
			{
				if(sNode.getSubNodes() != null && sNode.getSubNodes().size() > 0)
					length=100;
				else length = 30;
								
				count ++;
				
				int x1 = (int)(length * Math.cos(2*Math.PI*count/size + baseOffset)) + x;	//180��
				int y1 = (int)(length * Math.sin(2*Math.PI*count/size + baseOffset)) + y;
				g.drawLine(x+5, y+5, x1+5, y1+5);
				drawNode(g, sNode, x1, y1, 2*Math.PI*count/size+baseOffset);
			}
		}
	}
	
	class TagNodeShape
	{
		public int x, y;
		private Graphics g;
		private TagNode tNode;
		public double parentAngle;
		
		public TagNodeShape(Graphics g, TagNode node, int x, int y, double pAngle)
		{
			this.x = x;
			this.y = y;
			this.g = g;
			this.tNode = node;
			this.parentAngle = pAngle;
			
			this.paint();
		}
		
		private void paint()
		{
			this.g.setColor(Color.BLUE);
			this.g.fillOval(x, y, 10, 10);
			this.g.drawString(tNode.getTagName(), x+11, y+11);
		}
	}
	
	class TextNodeShape
	{
		public int x, y;
		private Graphics g;
		private TextNode tNode;
		public double parentAngle;
		
		public TextNodeShape(Graphics g, TextNode node, int x, int y, double pAngle)
		{
			this.x = x;
			this.y = y;
			this.g = g;
			this.tNode = node;
			this.parentAngle = pAngle;
			
			this.paint();
		}
		
		private void paint()
		{
			this.g.setColor(Color.RED);
			this.g.fillOval(x, y, 10, 10);
			if(tNode.getParsedText().startsWith("����"))
			{
				System.out.println(">>"+this.tNode.getParentNode()+">>" + this.parentAngle);
			}
			this.g.drawString(tNode.getParsedText(), x+11, y+11);
		}
	}
	
	
	public static void main(String...v)
	{
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                NodeViewer.createFrame();
            }
        });
	}
}


