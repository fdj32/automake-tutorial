package com.xp1024.job;

import java.io.IOException;
import java.util.stream.IntStream;

import org.jsoup.Jsoup;
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

	private static final String BASE = "http://w3.nat789.info/";

	private static final Logger LOG = LoggerFactory.getLogger(JsoupJob.class);

	@Autowired
	private Postgres pg;

	@Scheduled(fixedRate = 600000)
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
		Elements elements = doc.select("#category_1 tr");
		elements.parallelStream().forEach(this::tr);
	}

	private void tr(Element e) {
		if (null == e)
			return;
		Element td = e.selectFirst(".bk_a3");
		if (null == td)
			return;
		Element php = td.selectFirst(".l");
		if (null == php)
			return;
		int pFid = Integer.parseInt(php.attr("href").split("=")[1]);
		String pTitle = php.text();
		pg.saveThread(pFid, pTitle, -1);
		Elements elements = td.select("a");
		elements.parallelStream().forEach(i -> forumPhp(i, pFid));
	}

	private void forumPhp(Element e, int pFid) {
		if (null == e)
			return;
		if (e.hasClass("l"))
			return;
		String href = e.attr("href");
		String title = e.text();
		try {
			int fid = Integer.parseInt(href.split("-")[1]);
			pg.saveThread(fid, title, pFid);
			fid2db(fid);
		} catch (NumberFormatException e1) {
			LOG.error("forumPhp({}, {})", href, pFid);
		}
	}

	private void fid2db(int fid) {
		int last;
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
		Element e = doc.selectFirst("a .last");
		if (null == e)
			return 0;
		int last = Integer.parseInt(e.text().split(" ")[1]);
		LOG.info("fid={}, pages={}", fid, last);
		return last;
	}

	private void fidPage2db(int fid, int page) {
		Document doc = null;
		try {
			doc = get(fid, page);
			if (null == doc)
				return;
			Elements elements = doc.select("#threadlisttableid tbody tr td .max-td table tbody tr td .new");
			elements.parallelStream().forEach(i -> fidPageLinks(fid, page, i));
		} catch (Exception e) {
			LOG.error("Failed in fidPage({}, {})", fid, page);
			return;
		}
	}

	private void fidPageLinks(int fid, int page, Element i) {
		String line = i.attr("href") + "," + i.text();
		processLine(line, fid);
	}

	private void processLine(String s, int fid) {
		String[] ss = s.split(",");
		ss[1] = ss[1].replace("/", "");
		ss[1] = ss[1].replace("?", "");
		ss[1] = ss[1].replace(":", "");

		if (ss[0].startsWith("read.php?tid=")) {
			ss[0] = ss[0].split("&")[0]; // delete &fpage=*
		}
		String pageUrl = BASE + ss[0];

		if (pg.queryByLinkAndTitle(ss[0], ss[1]) == 0) {
			Document doc = connect(pageUrl);
			if (null == doc)
				return;
			Elements elements = doc.select("#read_tpc");
			if (null == elements || 0 == elements.size())
				return;
			String data = elements.get(0).toString();
			if (StringUtils.isEmpty(data))
				return;
			try {
				pg.save(fid, ss[0], ss[1], data);
			} catch (Exception e) {
				LOG.error("Failed processLine({}, {})", ss[0], fid);
			}
		} else {
			LOG.info("Found link={} and title={}", ss[0], ss[1]);
		}
	}

	private Document get(int fid, int page) throws IOException {
		return connect(BASE + String.format("forum.php?mod=forumdisplay&fid=%d&page=%d", fid, page));
	}

	private Document connect(String url) {
		Document doc = null;
		int times = 1;
		while (times <= RETRY_TIMES) {
			try {
				doc = Jsoup.connect(url).get();
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
