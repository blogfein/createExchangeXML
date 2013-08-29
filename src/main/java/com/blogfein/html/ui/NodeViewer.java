package com.blogfein.html.ui;
import com.blogfein.html.elements.*;
import com.blogfein.html.page.Page;
import com.blogfein.html.parser.*;

import java.awt.*;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;

public class NodeViewer extends JPanel{
	
	private Node rootNode = null;
	private JScrollPane sp_main = null;
	
	public NodeViewer(Node rtNode)
	{
		this.rootNode = rtNode;
		this.setLayout();
	}
	
	private void setLayout()
	{
		this.setLayout(new BorderLayout());
		JToolBar tBar = new JToolBar();
		tBar.add(new JButton("Temp"));
		this.add(tBar, BorderLayout.NORTH);
		
		//JPanel pnTemp = new JPanel();
		NodeTreeGraph ntGraph = new NodeTreeGraph(this.rootNode);
		ntGraph.setPreferredSize(new Dimension(4000,4000));
		this.sp_main = new JScrollPane(ntGraph);
		this.sp_main.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.sp_main.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		this.add(this.sp_main, BorderLayout.CENTER);
	}
		
	
	public static void createFrame()
	{
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("Node Viewer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		System.out.println("Parser ����..");
		File file = new File("d:\\naverSample2.html");
		//File file = new File("d:\\google.html");
		//File file = new File("d:\\test.html");
		Vector<Node> v_node = null;
		try {
			
			//URL url_source = new URL("http://news.chosun.com/site/data/html_dir/2008/07/10/2008071001091.html");
			//URL url_source = new URL("http://jakarta.tistory.com/entry/��-����-����-�-���");
			URL url_source = new URL("http://www.google.com");
			Page page = new Page(url_source, 3000);
			
			Parser parser = new Parser(page);
			parser.setFilter(new ParsingFilter());
			//Parser parser = new Parser(file);
			parser.parse();
			
			v_node = parser.getParsedList();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Size : " + v_node.size());
		
		NodeViewer nv = new NodeViewer((TagNode)v_node.get(0));
		contentPane.add(nv, BorderLayout.CENTER);
		
		frame.setSize(800, 600);
		frame.setVisible(true);
	}
	
	public static void main(String...v)
	{
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createFrame();
            }
        });
	}
	
}
