package com.StarPlatinum.BilibiliUpInfoCrawler;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

import javax.swing.text.html.HTML;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.omg.CORBA.DATA_CONVERSION;

import com.csvreader.CsvWriter;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class BilibiliCrawler extends WebCrawler {
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg" + "|png|mp3|mp4|zip|gz))$");
	CsvWriter cw;

	/**
	 * This method receives two parameters. The first parameter is the page in which
	 * we have discovered this new url and the second parameter is the new url. You
	 * should implement this function to specify whether the given url should be
	 * crawled or not (based on your crawling logic). In this example, we are
	 * instructing the crawler to ignore urls that have css, js, git, ... extensions
	 * and to only accept urls that start with "https://www.ics.uci.edu/". In this
	 * case, we didn't need the referringPage parameter to make the decision.
	 */
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		return !FILTERS.matcher(href).matches() && href.startsWith("https://api.bilibili.com/");
	}

	/**
	 * This function is called when a page is fetched and ready to be processed by
	 * your program.
	 */
	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		logger.info(url);
		System.out.println("URL: " + url);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		String csvfile = "BiliBiliRank" + format.format(new Date()).replace(':', '-') ;
		System.out.println(csvfile);
		File csv = CreateFileUtil.createFile("./out/" + csvfile + ".csv");
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
			cw = new CsvWriter(new FileWriter(csv, true), ',');
			cw.write("Order");
			cw.write("VideoName");
			cw.write("View");
			cw.write("Uploader");
			cw.endRecord();
			// TODO video id
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// String jsonContent = new String(page.getContentData());
		// JSONObject object = new JSONObject(new String(page.getContentData()));
		// Boolean status = object.getBoolean("status");
		// File jsonFile = new File("./VedioList.json");
		//
		// try {
		// jsonFile.createNewFile();
		// BufferedWriter out = new BufferedWriter(new FileWriter(jsonFile));
		// out.write(jsonContent);
		// out.flush();
		// out.close();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		int i = 1;
		if (page.getParseData() instanceof HtmlParseData) {

			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			// String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();

			Document document = Jsoup.parse(html);
			Elements contents = document.getElementsByClass("info");
			try {
				for (Element c : contents) {

					cw.write(String.valueOf(i));
					Element titile = c.child(0);
					cw.write(titile.text());
					Elements detail = c.getElementsByClass("data-box");
					for (Element info : detail) {
						cw.write(info.text());
					}
					cw.write(c.getElementsByClass("pts").first().child(0).text());
					cw.endRecord();
					i++;
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cw.close();
			
			// Set<WebURL> links = htmlParseData.getOutgoingUrls();
			//
			// System.out.println("Text length: " + text.length());
			// System.out.println("Html length: " + html.length());
			// System.out.println("Number of outgoing links: " + links.size());
		}
		return;
	}
}
