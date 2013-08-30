package com.blogfein.exchange;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.blogfein.html.parser.Parser;

public class App extends Parser
{
	private static Hashtable<Integer, String> xmlData2;

	private static String currencyText;
	private static String currencySign;
	private static String[] wordsplit;
	
    // public static void main( String[] args )
	public static void main(String...v)
    {
        // System.out.println( "Hello World!" );

        String currencyList = "";
        String currencyListArray[] = new String[7];
        Parser.main();
        xmlData2 = Parser.getXmlData();
        
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String sdf = new SimpleDateFormat("yyyyMMddHHmmss").format(date);        
        String sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date); 
        
		Document doc = new Document();
		Element root = new Element("snazzy");
		Element link = new Element("link");
		Element language = new Element("language");
		Element copyright = new Element("copyright");
		Element pubDate = new Element("pubDate");
		Element managingEditor = new Element("managingEditor");
		Element webMaster = new Element("webMaster");
		Element last_update = new Element("last_update");
		Element exchange_count = new Element("exchange_count");	

		root.setAttribute("service", "exchange");
		root.addContent(link.setText("http://www.androes.com/"));
		root.addContent(language.setText("KO"));
		root.addContent(copyright.setText("Copyright(c) Androes, All Rights Reserved."));
		root.addContent(pubDate.setText(sdf1));
		root.addContent(managingEditor.setText("snazzy79@naver.com"));
		root.addContent(webMaster.setText("snazzy79@naver.com"));
		root.addContent(last_update.setText(sdf));	

		Integer key = 0;
		Integer total = 0;
		String val = "";
		Matcher m;
		Pattern p = Pattern.compile(" ");
		Enumeration<Integer> keys = xmlData2.keys();
		while(keys.hasMoreElements())
		{
			key = keys.nextElement();
            val = (String)xmlData2.get(key);
            if (key%10 == 0 || key%10 == 7 || key%10 == 6) continue;
            // System.out.println("> Key: " + key + " Val: " + val);
            m = p.matcher(val);
            if (m.find()) {
				wordsplit = val.split(" ");
				currencyText = wordsplit[0];
				currencySign = wordsplit[1];

		    	Element currency = new Element("currency");	
				addElement(currency,"hname",currencyText);
				addElement(currency,"sign",currencySign);
				addElement(currency,"change_val",currencyListArray[1]);
				addElement(currency,"standard",currencyListArray[2]);
				addElement(currency,"receive",currencyListArray[3]);
				addElement(currency,"send",currencyListArray[4]);
				addElement(currency,"sell",currencyListArray[5]);
				addElement(currency,"buy",currencyListArray[6]);
				root.addContent(currency);
				currencyListArray = new String[7]; 
            	total++;
            }
            if (key%10 == 9) currencyListArray[1] = val;
            if (key%10 == 8) currencyListArray[2] = val;
            if (key%10 == 5) currencyListArray[3] = val;
            if (key%10 == 4) currencyListArray[4] = val;
            if (key%10 == 3) currencyListArray[5] = val;
            if (key%10 == 2) currencyListArray[6] = val;
		}

		root.addContent(exchange_count.setText(total.toString()));	
		doc.setRootElement(root);

		try {
			FileOutputStream out = new FileOutputStream("d:\\test.xml");
			// xml 파일을 떨구기 위한 경로와 파일 이름 지정해 주기
			XMLOutputter serializer = new XMLOutputter();

			Format f = serializer.getFormat();			
			f.setEncoding("euc-kr");
			// encoding 타입을 UTF-8 로 설정
			f.setIndent(" ");
			f.setLineSeparator("\r\n");
			f.setTextMode(Format.TextMode.TRIM);
			serializer.setFormat(f);

			serializer.output(doc, out);
			out.flush();
			out.close();

			// String 으로 xml 출력
			// XMLOutputter outputter = new
			// XMLOutputter(Format.getPrettyFormat().setEncoding("UTF-8")) ;
			// System.out.println(outputter.outputString(doc));
		} catch (IOException e) {
			System.err.println(e);
		}
        // System.out.println( "OK!" );
    }

	// 엘리먼트 생성
	public static Element addElement(Element parent, String name, String value) {
		Element element = new Element(name);
		element.setText(value);
		parent.addContent(element);
		return parent;
	}

	// 애트리뷰트 생성
	public void addAttribute(Element element, String name, String value) {
		Attribute attribute = new Attribute(name, value);
		element.setAttribute(attribute);
	}

	public static String getNowDateTime() {
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		return new SimpleDateFormat("yyyyMMddHHmmss").format(date);
	}
}
