package com.StarPlatinum.BilibiliUpInfoCrawler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.csvreader.CsvWriter;


public class BilibiliCrawlerForRankList {
	
	String m_url;
	CsvWriter m_cw;
	
	public BilibiliCrawlerForRankList(String url) {
		// TODO Auto-generated constructor stub
		
		m_url = url;
		
		System.out.println("URL: " + m_url);
	}
	
	public void GetRankList() throws IOException {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		String csvfile = "BiliBiliRank" + format.format(new Date()).replace(':', '-') ;
		System.out.println(csvfile);
		File csv = CreateFileUtil.createFile("./out/" + csvfile , "csv");
		try {
			OutputStream outputStream= new FileOutputStream(csv);
			byte[] uft8bom={(byte)0xef,(byte)0xbb,(byte)0xbf};
			outputStream.write(uft8bom);
			outputStream.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			m_cw = new CsvWriter(new FileWriter(csv, true), ',');
			m_cw.write("Order");
			m_cw.write("VideoName");
			m_cw.write("View");
			m_cw.write("Uploader");
			m_cw.endRecord();
			// TODO video id
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int i = 1;
		
			Document docPageJson = Jsoup.connect(m_url).ignoreContentType(true).get();
//			JSONObject docPage = new JSONObject(docPageJson.getElementsByTag("body").text());

//			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
//			String html = htmlParseData.getHtml();

//			Document document = Jsoup.parse(html);
			Elements contents = docPageJson.getElementsByClass("info");
			try {
				for (Element c : contents) {

					m_cw.write(String.valueOf(i));
					Element titile = c.child(0);
					m_cw.write(titile.text());
					Elements detail = c.getElementsByClass("data-box");
					for (Element info : detail) {
						m_cw.write(info.text());
					}
					m_cw.write(c.getElementsByClass("pts").first().child(0).text());
					m_cw.endRecord();
					i++;
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			m_cw.close();
			
			// Set<WebURL> links = htmlParseData.getOutgoingUrls();
			//
			// System.out.println("Text length: " + text.length());
			// System.out.println("Html length: " + html.length());
			// System.out.println("Number of outgoing links: " + links.size());
	
		return;
	}
}
