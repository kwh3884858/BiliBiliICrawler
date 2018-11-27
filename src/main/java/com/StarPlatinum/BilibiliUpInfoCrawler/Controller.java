package com.StarPlatinum.BilibiliUpInfoCrawler;

import java.awt.Button;
import java.io.BufferedWriter;
import java.io.File;
import java.util.Scanner;

import com.google.common.base.CaseFormat;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String crawlStorageFolder = "./CrawlerStorageFolder";
		CreateFileUtil.createDir(crawlStorageFolder);

		int numberOfCrawlers = 1;

		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setMaxDepthOfCrawling(0);
		config.setIncludeBinaryContentInCrawling(true);

		/*
		 * Instantiate the controller for this crawl.
		 */
		PageFetcher pageFetcher = new PageFetcher(config);
		PageFetcher pageFetcher2 = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller;
		/*
		 * For each crawl, you need to add some seed urls. These are the first URLs that
		 * are fetched and then the crawler starts following links which are found in
		 * these pages
		 */
		// String mid = "265224956";
		// String spaceURL =
		// "https://space.bilibili.com/ajax/member/getSubmitVideos?mid="+
		// mid + "&pagesize=90&tid=0&page=1&keyword=&order=pubdate";
		
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("输入需要执行的功能\n（0：获取一个视频的详细数据）\n（1:获取排行榜的数据）");
			
			int flag = -1;
			if (scanner.hasNextInt()) {
				flag = scanner.nextInt();
			}
			
			
			switch (flag) {
			case 0:
				System.out.println("进入获取视频阶段");
				System.out.println("请输入对应的aid，即为av号码，输入纯数字，不要带上av");
				String aid = scanner.next();
				String videoPage = "https://api.bilibili.com/x/web-interface/archive/stat?aid=" + aid;
				controller = new CrawlController(config, pageFetcher, robotstxtServer);
				controller.addSeed(videoPage);
				controller.startNonBlocking(VideoInfoCrawler.class, numberOfCrawlers);
				Thread.sleep(5 * 1000);
//				controller.shutdown();
				//controller.waitUntilFinish();
				break;

			case 1:
				System.out.println("进入获取排行榜阶段");

				System.out.println("输入需要获取的榜单\n（0：获取全站榜）\n（1:获取原创榜）");
				int rankkey = scanner.nextInt();
				String rank = "none";
				while (rank == "none") {
					switch (rankkey) {
					case 0:
						rank = "all";
						break;

					case 1:
						rank = "origin";
						break;

					default:
						System.out.println("输入错误，确保输入的为单个数字，请重试");
						break;
					}
				}

				// System.out.println("输入需要获取的板块\n(0:全站)\n（1:动画）\n（2:音乐）\n");

				// String subTitle = "1";

				String rankPage = "https://www.bilibili.com/ranking/" + rank + "/1/0/1";
				controller = new CrawlController(config, pageFetcher2, robotstxtServer);
				controller.addSeed(rankPage);
				controller.startNonBlocking(BilibiliCrawler.class, numberOfCrawlers);
				// Wait for 5 seconds
				Thread.sleep(5 * 1000);
				System.out.println("执行结束");
				// Send the shutdown request and then wait for finishing
//				controller.shutdown();
				//controller.waitUntilFinish();
				break;

			default:
				System.out.println("输入错误，确保输入的为单个数字，请重试");
				break;
			}
			
		}
		
		/*
		 * Start the crawl. This is a blocking operation, meaning that your code will
		 * reach the line after this only when crawling is finished.
		 */

	}

}
