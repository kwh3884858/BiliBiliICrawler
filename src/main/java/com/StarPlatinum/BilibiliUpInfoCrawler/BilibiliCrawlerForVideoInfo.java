package com.StarPlatinum.BilibiliUpInfoCrawler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.csvreader.CsvWriter;

public class BilibiliCrawlerForVideoInfo {
	
	String m_url;
	CsvWriter m_cw;

	public BilibiliCrawlerForVideoInfo(String url) {
		// TODO Auto-generated constructor stub
		m_url = url;
		
		System.out.println("URL: " + m_url);
		
	}
	
	public void GetVideoInfo() throws IOException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

		String csvfile = "VidoeInfo" +format.format(new Date()).replace(':', '-') ;
		File csv = CreateFileUtil.createFile("./out/" + csvfile , "csv");

		try {
			m_cw = new CsvWriter(new FileWriter(csv, true), ',');
			m_cw.write("av号");
			m_cw.write("观看量");
			m_cw.write("弹幕数");
			m_cw.write("评论数");
			m_cw.write("收藏数");
			m_cw.write("硬币数");
			m_cw.write("分享数");
			m_cw.write("喜欢数");
			m_cw.write("当前排名");
			m_cw.write("历史排名");

			m_cw.endRecord();
			// TODO video id
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// String jsonContent = new String(page.getContentData());
		Document docPageJson = Jsoup.connect(m_url).ignoreContentType(true).get();
		JSONObject docPage = new JSONObject(docPageJson.getElementsByTag("body").text());
//		JSONObject object = new JSONObject(new String(page.getContentData()));
		JSONObject status = docPage.getJSONObject("data");
		try {
			m_cw.write(String.valueOf(status.getLong("aid")) );
			m_cw.write(String.valueOf(status.getLong("view")));
			m_cw.write(String.valueOf(status.getLong("danmaku")));
			m_cw.write(String.valueOf(status.getLong("reply")));
			m_cw.write(String.valueOf(status.getLong("favorite")));
			m_cw.write(String.valueOf(status.getLong("coin")));
			m_cw.write(String.valueOf(status.getLong("share")));
			m_cw.write(String.valueOf(status.getLong("like")));
			m_cw.write(String.valueOf(status.getLong("now_rank")));
			m_cw.write(String.valueOf(status.getLong("his_rank")));
			m_cw.endRecord();

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("Jsoup.connect failed! Check Internet connect.");
			e1.printStackTrace();
		}
		m_cw.close();
	}
}
