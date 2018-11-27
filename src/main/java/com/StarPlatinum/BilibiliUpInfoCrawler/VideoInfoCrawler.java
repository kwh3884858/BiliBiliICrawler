package com.StarPlatinum.BilibiliUpInfoCrawler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.csvreader.CsvWriter;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class VideoInfoCrawler extends WebCrawler {
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg" + "|png|mp3|mp4|zip|gz))$");
	CsvWriter cw;

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		return !FILTERS.matcher(href).matches() && href.startsWith("https://www.bilibili.com/");
	}

	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		logger.info(url);
		System.out.println("URL: " + url);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

		String csvfile = "VidoeInfo" +format.format(new Date()).replace(':', '-') ;
//		File csv = new File("../out/" + csvfile + ".csv");
//		if (csv.isFile()) {
//			csv.delete();
//		}
		File csv = CreateFileUtil.createFile("./out/" + csvfile + ".csv");

		try {
			cw = new CsvWriter(new FileWriter(csv, true), ',');
			cw.write("av号");
			cw.write("观看量");
			cw.write("弹幕数");
			cw.write("评论数");
			cw.write("收藏数");
			cw.write("硬币数");
			cw.write("分享数");
			cw.write("喜欢数");
			cw.write("当前排名");
			cw.write("历史排名");

			cw.endRecord();
			// TODO video id
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// String jsonContent = new String(page.getContentData());
		JSONObject object = new JSONObject(new String(page.getContentData()));
		JSONObject status = object.getJSONObject("data");
		try {
			cw.write(String.valueOf(status.getLong("aid")) );
			cw.write(String.valueOf(status.getLong("view")));
			cw.write(String.valueOf(status.getLong("danmaku")));
			cw.write(String.valueOf(status.getLong("reply")));
			cw.write(String.valueOf(status.getLong("favorite")));
			cw.write(String.valueOf(status.getLong("coin")));
			cw.write(String.valueOf(status.getLong("share")));
			cw.write(String.valueOf(status.getLong("like")));
			cw.write(String.valueOf(status.getLong("now_rank")));
			cw.write(String.valueOf(status.getLong("his_rank")));
			cw.endRecord();

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		cw.close();
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
		// int i = 1;
		// if (page.getParseData() instanceof HtmlParseData) {
		//
		// HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
		// // String text = htmlParseData.getText();
		// String html = htmlParseData.getHtml();
		//
		// Document document = Jsoup.parse(html);
		// Elements contents = document.getElementsByClass("info");
		// try {
		// for (Element c : contents) {
		//
		// cw.write(String.valueOf(i));
		// Element titile = c.child(0);
		// cw.write(titile.text());
		// Elements detail = c.getElementsByClass("data-box");
		// for (Element info : detail) {
		// cw.write(info.text());
		// }
		// cw.write(c.getElementsByClass("pts").first().child(0).text());
		// cw.endRecord();
		// i++;
		// }
		//
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// Set<WebURL> links = htmlParseData.getOutgoingUrls();
		//
		// System.out.println("Text length: " + text.length());
		// System.out.println("Html length: " + html.length());
		// System.out.println("Number of outgoing links: " + links.size());
		// }
	}
}
