package com.xp1024.job;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.xp1024.dao.Postgres;

@Service
public class JsoupJob {

	private static final int RETRY_TIMES = 2;

	private static final String BASE = "http://93.cao1024lui99.xyz/pw/";

	private static final Logger LOG = LoggerFactory.getLogger(JsoupJob.class);

	@Autowired
	private Postgres pg;

	@Scheduled(fixedRate = 300000, initialDelay=300000)
	public void img() throws IOException {
		LOG.info("img() started");
		long start = System.currentTimeMillis();
		Map<String, Object> idMap = pg.htmdataIdRange();
		int minId = (int) idMap.get("minId");
		int maxId = (int) idMap.get("maxId");
		List<Map<String, Object>> dataList = null;
		while (minId < maxId) {
			dataList = pg.loadHtmdata(minId, minId + 1000);
			minId += 1000;
			dataList.stream().forEach(i -> saveImg(i));
		}
		LOG.info("img() ended");
		LOG.info("img() finished in {} seconds", (System.currentTimeMillis() - start) / 1000);
	}

	public void saveImg(Map<String, Object> map) {
		Element e = new Element("body");
		e.html((String) map.get("data"));
		Elements elements = e.select("img");
		elements.stream().forEach(i -> pg.saveImg(i.attr("src")));
		LOG.info("save {} img", elements.size());
	}

	@Scheduled(fixedRate = 1000*60*60*24)
	public void jsoup() throws IOException {
		LOG.info("jsoup() started");
		long start = System.currentTimeMillis();
		long startCount = pg.count();
		getfids();
		long endCount = pg.count();
		LOG.info("startCount={}, endCount={}, {} added", startCount, endCount, (endCount - startCount));
		LOG.info("jsoup() finished in {} seconds", (System.currentTimeMillis() - start) / 1000);
	}

	private void getfids() {
		Document doc = connect(BASE);
		Elements elements = doc.select("tbody tr th span a");
		elements.parallelStream().forEach(this::forumPhp);
	}

	private void forumPhp(Element e) {
		if (null == e)
			return;
		String href = e.attr("href");
		String title = e.text();
		try {
			int fid = Integer.parseInt((href.split("-")[3]).split("\\.")[0]);
			pg.saveThread(fid, title, -1);
			fid2db(fid);
		} catch (NumberFormatException e1) {
			LOG.error("forumPhp({}, {})", href, -1);
		}
	}

	private void fid2db(int fid) {
		int last = 0;
		try {
			last = last(fid);
			if (0 == last)
				return;
			IntStream.range(1, last + 1).parallel().forEach(i -> fidPage2db(fid, i));
		} catch (IOException e) {
			LOG.error("Failed in fid2db({})", fid);
		}
	}

	private int last(int fid) throws IOException {
		Document doc = get(fid, 1);
		Element e = doc.selectFirst(".pagesone");
		if (null == e)
			return 0;
		int last = Integer.parseInt(e.text().split("/")[1].split(" ")[0]);
		LOG.info("fid={}, pages={}", fid, last);
		return last;
	}

	private void fidPage2db(int fid, int page) {
		Document doc = null;
		try {
			doc = get(fid, page);
			if (null == doc)
				return;
			Elements elements = doc.select("tr td h3 a");
			elements.parallelStream().forEach(i -> threadHtml(fid, page, i));
		} catch (Exception e) {
			LOG.error("Failed in fidPage({}, {})", fid, page);
			return;
		}
	}

	private void threadHtml(int fid, int page, Element i) {
		String href = i.attr("href");
		String title = i.text();
		if (pg.queryByLinkAndTitle(href, title) == 0) {
			Document doc = connect(BASE + href);
			if (null == doc)
				return;
			Elements elements = doc.select(".tpc_content");
			if (null == elements || 0 == elements.size())
				return;
			String data = elements.get(0).toString();
			if (StringUtils.isEmpty(data))
				return;
			try {
				pg.save(fid, href, title, data);
			} catch (Exception e) {
				LOG.error("Failed save({}, {}, {}, {})", fid, href, title, data);
			}
		} else {
			LOG.info("Found link={} or title={}", href, title);
		}

	}

	private Document get(int fid, int page) throws IOException {
		return connect(BASE + String.format("thread-htm-fid-%d-page-%d.html", fid, page));
	}

	private Document connect(String url) {
		Document doc = null;
		int times = 1;
		while (times <= RETRY_TIMES) {
			try {
				Connection conn = HttpConnection.connect(url);
				conn.header("Accept",
						"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
				conn.header("Accept-Encoding", "gzip, deflate");
				conn.header("Accept-Language", "zh-CN,zh;q=0.9,ja;q=0.8");
				conn.header("Cache-Control", "max-age=0");
				conn.header("Connection", "keep-alive");
				conn.header("Cookie",
						"visid_incap_1729161=vLXyOD5mQo+fcYH/3t34vtq8SlsAAAAAQUIPAAAAAACqxN9WUhqVORrSMvdvYKwh; incap_ses_798_1729161=NoZkCHVVeDqSQ5G+MBETC9q8SlsAAAAAWUoHO2dTqIOsE5shO9nNeA==; nlbi_1729161=9IKJQ7Nq8HoG35t9kRXV5AAAAACDo2AC54Qsb3PW1Q6Ent4Q; UM_distinctid=1649bf1b82c2d2-04591256761493-5b193413-1fa400-1649bf1b82e307; CNZZDATA1261158850=1579915528-1531623579-%7C1531623579; 19fg_2132_saltkey=JJPq77z7; 19fg_2132_lastvisit=1531621213; 19fg_2132_sid=giJQq9; 19fg_2132_lastact=1531625513%09forum.php%09ajax");
				conn.header("Host", "s3.99hiya.biz");
				conn.header("If-None-Match", "\"9f180aff\"");
				conn.header("Upgrade-Insecure-Requests", "1");
				conn.userAgent(
						"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
				doc = conn.get();
			} catch (IOException e) {
				LOG.error("Failed in Jsoup.connect({}), times={}", url, times);
			}
			if (null != doc)
				break;
			else
				times++;
		}
		LOG.info("connect({})", url);
		return doc;
	}

}
