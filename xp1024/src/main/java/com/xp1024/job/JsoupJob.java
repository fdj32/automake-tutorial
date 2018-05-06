package com.xp1024.job;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.apache.commons.io.FileUtils;
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

import com.xp1024.dao.Mysql;
import com.xp1024.dao.Postgres;

@Service
public class JsoupJob {

	private static final int RETRY_TIMES = 2;

	private static final int BATCH_SIZE = 40000;

	private static final String BASE = "http://w3.afulyu.pw/pw/";

	private static final String FOLDER = "E:/hub/1024/";

	private static final Logger LOG = LoggerFactory.getLogger(JsoupJob.class);

	@Autowired
	private Mysql mysql;

	@Autowired
	private Postgres pg;

	private Document get(int fid, int page) throws IOException {
		return connect(BASE + String.format("thread.php?fid=%d&page=%d", fid, page));
	}

	private int last(int fid) throws IOException {
		Document doc = get(fid, 1);
		Elements elements = doc.select("#main .pages");
		String pages = elements.get(0).text();
		int last = 0;
		String[] ss = pages.split(" ");
		for (String s : ss) {
			if (s.startsWith("1/")) {
				last = Integer.parseInt(s.substring(2));
			}
		}
		LOG.info("fid={}, pages={}", fid, last);
		return last;
	}

	private void fidPage2txt(int fid, int page, Element i) {
		String content = i.attr("href") + "," + i.text() + System.lineSeparator();
		String fileName = FOLDER + String.format("%d/%03d.txt", fid, page);
		LOG.debug(content);
		File file = new File(fileName);
		try {
			FileUtils.writeStringToFile(file, content, "UTF-8", true);
		} catch (IOException e) {
			LOG.error("Failed in writing " + content + " to " + fileName);
		}
	}

	@Scheduled(fixedRate = 600000)
	public void jsoup() throws IOException {
		LOG.info("jsoup() started");
		long start = System.currentTimeMillis();
		// IntStream.of(3, 5, 22, 7, 18, 83).parallel().forEach(i -> fid2txt(i));
		// IntStream.of(21).parallel().forEach(i -> fid2txt(i));
		// fid(21);
		// IntStream.of(14, 16, 18, 22, 3, 37, 49, 7, 78, 8, 81, 88, 15, 17, 21, 25, 30,
		// 39, 5, 75, 79, 80, 83)
		// IntStream.of(17,37,39)
		// .parallel().forEach(i -> fid(i));
		getfids();
		LOG.info("mysql.count()={}", mysql.count());
		LOG.info("pg.count()={}", pg.count());
		//mysql2pg();
		//pg2mysql();
		//pg2mysqlStream();
		LOG.info("jsoup() finished in {} ms", (System.currentTimeMillis() - start));
	}

	private void mysql2pg() {
		int size = mysql.count();
		LOG.info("Total : {}", size);
		int i = 0;
		List<Map<String, Object>> list = null;
		while (i * BATCH_SIZE < size) {
			try {
				list = mysql.query(i * BATCH_SIZE, (i + 1) * BATCH_SIZE);
				pg.batchInsert(list);
			} catch (SQLException e) {
				LOG.error("batchInsert failed in id {} -> {}", i * BATCH_SIZE, (i+1) * BATCH_SIZE);
				LOG.error("", e);
			}
			i++;
			System.gc();
		}
	}
	
	private void pg2mysql() {
		long size = pg.count();
		LOG.info("Total : {}", size);
		int i = 0;
		List<Map<String, Object>> list = null;
		while (i * BATCH_SIZE < size) {
			try {
				list = pg.query(i * BATCH_SIZE, (i + 1) * BATCH_SIZE);
				mysql.batchInsert(list);
			} catch (SQLException e) {
				LOG.error("batchInsert failed in id {} -> {}", i * BATCH_SIZE, (i+1) * BATCH_SIZE);
				LOG.error("", e);
			}
			i++;
			System.gc();
		}
	}
	
	private void pg2mysqlStream() {
		long size = pg.count();
		LOG.info("Total : {}", size);
		int i = 0;
		List<Map<String, Object>> list = null;
		while (i * BATCH_SIZE < size) {
			try {
				list = pg.query(i * BATCH_SIZE, (i + 1) * BATCH_SIZE);
				list.parallelStream().forEach(m->mysql.save((int)m.get("fid"), (String)m.get("link"), (String)m.get("title"), (String)m.get("data")));
				//mysql.batchInsert(list);
			} catch (Exception e) {
				LOG.error("batchInsert failed in id {} -> {}", i * BATCH_SIZE, (i+1) * BATCH_SIZE);
				LOG.error("", e);
			}
			i++;
			System.gc();
		}
	}

	private void fid2txt(int fid) {
		int last;
		try {
			last = last(fid);
			IntStream.range(1, last + 1).parallel().forEach(i -> fidPage(fid, i));
		} catch (IOException e) {
			LOG.error("Failed in fid2txt({})", fid);
		}
	}

	private void fidPage(int fid, int page) {
		Document doc = null;
		try {
			doc = get(fid, page);
			if (null == doc)
				return;
			Elements elements = doc.select("#ajaxtable tr td h3 a");
			elements.stream().forEach(i -> fidPage2txt(fid, page, i));
		} catch (Exception e) {
			LOG.error("Failed in fidPage({}, {})", fid, page);
			return;
		}
	}

	private void fid(int fid) {
		File folder = new File(FOLDER + fid);
		File[] files = folder.listFiles();
		if (null == files || 0 == files.length) {
			try {
				FileUtils.deleteDirectory(folder);
				LOG.info("deleteDirectory({})", fid);
			} catch (IOException e) {
				LOG.error("Failed in deleteDirectory({})", fid);
			}
		}
		Arrays.stream(files).parallel().forEach(i -> processPage(i, fid));
		// Files.list(Paths.get(FOLDER + fid)).parallel().forEach(i-> processPage(i,
		// fid));
		LOG.info("fid({}) finished", fid);
	}

	private void processPage(File file, int fid) {
		List<String> list = null;
		try {
			list = FileUtils.readLines(file, "UTF-8");
		} catch (IOException e) {
			LOG.error("Read {} Error", file.getName());
		}
		list.stream().parallel().forEach(i -> processLine(i, fid));
		FileUtils.deleteQuietly(file);
		LOG.info("processPage({}, {}) finished", file.getName(), fid);
	}

	private void processPage(Path path, int fid) {
		try {
			Files.lines(path).parallel().forEach(i -> processLine(i, fid));
		} catch (IOException e) {
			LOG.error("Failed in processPage({})", path.toString());
		}
		LOG.info("processPage({}, {}) finished", path.toString(), fid);
	}

	private void processLine(String s, int fid) {
		String[] ss = s.split(",");
		ss[1] = ss[1].replace("/", "");
		ss[1] = ss[1].replace("?", "");
		ss[1] = ss[1].replace(":", "");
		
		if(ss[0].startsWith("read.php?tid=")) {
			ss[0] = ss[0].split("&")[0]; // delete &fpage=*
		}
		String pageUrl = BASE + ss[0];
		
		if (pg.queryByLink(ss[0]) == 0) {
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
			LOG.info("Found {}", ss[0]);
		}
		// try {
		// String fileName = FOLDER + String.format("fid%d/%s.html", fid, ss[1]);
		// if (!Files.exists(Paths.get(fileName))) {
		// FileUtils.writeStringToFile(new File(fileName), data, "UTF-8");
		// }
		// } catch (IOException e) {
		// LOG.error("Failed in connect " + pageUrl);
		// }
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
			// try {
			// Thread.sleep(times * 100);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
		}
		LOG.info("connect({})", url);
		return doc;
	}

	private void getfids() {
		Document doc = connect(BASE);
		Elements elements = doc.select("#cate_1 tr th b span a");
		// elements.stream().map(Element::toString).forEach(System.out::println);
		elements.stream().parallel().forEach(this::threadPHP);
	}

	private void threadPHP(Element e) {
		// String name = e.text();
		String href = e.attr("href");
		int fid = Integer.parseInt(href.split("=")[1]);
//		fid2txt(fid);
//		fid(fid);
		fid2db(fid);
	}
	
	private void fid2db(int fid) {
		int last;
		try {
			last = last(fid);
			IntStream.range(1, last + 1).parallel().forEach(i -> fidPage2db(fid, i));
		} catch (IOException e) {
			LOG.error("Failed in fid2db({})", fid);
		}
	}
	
	private void fidPage2db(int fid, int page) {
		Document doc = null;
		try {
			doc = get(fid, page);
			if (null == doc)
				return;
			Elements elements = doc.select("#ajaxtable tr td h3 a");
			elements.stream().forEach(i -> fidPageLinks(fid, page, i));
		} catch (Exception e) {
			LOG.error("Failed in fidPage({}, {})", fid, page);
			return;
		}
	}
	
	private void fidPageLinks(int fid, int page, Element i) {
		String line = i.attr("href") + "," + i.text();
		processLine(line, fid);
	}
}
