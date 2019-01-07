/**
 * 
 */
package com.StarPlatinum.BilibiliUpInfoCrawler;

import java.io.IOException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;


/**
 * @author cookie
 *
 */
public class BiliBiliCrawlerUI {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		long interval  = 0;
		Scanner scanner = new Scanner(System.in);
		
		while (true) {
			System.out.println("输入需要执行的功能\n"
					+ "（0：获取一个视频的详细数据）\n"
					+ "（1:获取排行榜的数据）\n"
					+ "（9：退出软件）");
			
			int flag = -1;
			if (scanner.hasNextInt()) {
				flag = scanner.nextInt();	
				System.out.println("flag is " + flag );
			}
			
			
			switch (flag) {
			case 0:
				System.out.println("进入获取视频阶段\n");
				System.out.println("请输入对应的aid，即为av号码，输入纯数字，不要带上av");
				String aid = "";
				if (scanner.hasNextInt()) {
					aid = scanner.next();
				}
				if (aid == "") {
					System.out.println("Error av");
					break;
				}
				String videoPage = "https://api.bilibili.com/x/web-interface/archive/stat?aid=" + aid;
				System.out.println("输入定时执行的时间，分钟为单位，为0则不进行定时执行");
				
				if (scanner.hasNextInt()) {
					interval = scanner.nextLong();
				}
				if (interval == 0) {
					BilibiliCrawlerForVideoInfo info = new BilibiliCrawlerForVideoInfo(videoPage);
					info.GetVideoInfo();
				}else {
					interval = interval * 60000;
					Timer timer = new Timer();   
			        timer.schedule(new GetVideoInfo(videoPage), 0, interval);
				}
			
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
				System.out.println("输入定时执行的时间，分钟为单位，为0则不进行定时执行");

				if (scanner.hasNextInt()) {
					interval = scanner.nextLong();
				}
				if (interval == 0) {
					BilibiliCrawlerForRankList rankList = new BilibiliCrawlerForRankList(rankPage);
					rankList.GetRankList();
				}else {
					interval = interval * 60000;
					Timer timer = new Timer();   
			        timer.schedule(new GetRankList(rankPage), 0, interval);
				}
				

				System.out.println("执行结束");
		
				break;
			case 9:
				scanner.close();
				return;
				
			default:
				System.out.println("输入错误，确保输入的为单个数字，请重试");
				break;
			}
			
		}
		
		

	}

}
class GetVideoInfo extends TimerTask {
    String m_url; 

    public GetVideoInfo(String url) { 
     this.m_url = url;
    } 

    @Override 
    public void run() { 
    		BilibiliCrawlerForVideoInfo info = new BilibiliCrawlerForVideoInfo(m_url);
		try {
			info.GetVideoInfo();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     // You can do anything you want with param 
    } 
}
class GetRankList extends TimerTask {
    String m_url; 

    public GetRankList(String url) { 
     this.m_url = url;
    } 

    @Override 
    public void run() { 
    	BilibiliCrawlerForRankList rankList = new BilibiliCrawlerForRankList(m_url);
		try {
			rankList.GetRankList();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     // You can do anything you want with param 
    } 
}