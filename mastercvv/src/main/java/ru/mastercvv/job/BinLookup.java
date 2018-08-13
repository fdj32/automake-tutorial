package ru.mastercvv.job;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ru.mastercvv.dao.Pgsql;

@Service
public class BinLookup {

	private static final Logger LOG = LoggerFactory.getLogger(BinLookup.class);

	private static final String MASTERCVV_RU = "http://mastercvv.ru/bin/lookup/creditcard/page_%d.html?d=2";

	private static List<Integer> pageNumList = null;

	private static final String UTF8 = "UTF-8";
	
	private static StringBuffer JSON = null;

	private static File FILE = null;

	static {
		pageNumList = IntStream.range(1, 11382).boxed().collect(Collectors.toList());
		FILE = new File("bin.js");
		JSON = new StringBuffer();
	}

	@Autowired
	private Pgsql pg;

	@Scheduled(fixedRate = 300000)
	public void output() throws IOException {
		LOG.info("output() started");
		FileUtils.write(FILE, "var bin = {\"db\":[\n", UTF8, true);
		pg.selectAllBin().stream().forEach(i -> print(i));
		String s = JSON.toString();
		FileUtils.write(FILE, s.substring(0, s.length() - 2), UTF8, true);
		FileUtils.write(FILE, "\n]};\n", UTF8, true);
		LOG.info("output() ended");
	}

	public void print(Map<String, Object> map) {
		JSON.append("{\"a\":\"").append(map.get("bin"));
		JSON.append("\",\"b\":\"").append(null == map.get("bank_name") ? "" : map.get("bank_name"));
		JSON.append("\",\"c\":\"").append(null == map.get("issuing_network") ? "" : map.get("issuing_network"));
		JSON.append("\",\"d\":\"").append(null == map.get("country") ? "" : map.get("country"));
		JSON.append("\",\"e\":\"").append(null == map.get("card_type") ? "" : map.get("card_type"));
		JSON.append("\",\"f\":\"").append(null == map.get("card_level") ? "" : map.get("card_level"));
		JSON.append("\"},\n");
	}

	// @Scheduled(fixedRate = 3000)
	public void lookup() throws IOException {
		LOG.info("lookup() started");
		pageNumList.parallelStream().forEach(i -> lookupPage(i));
		LOG.info("lookup() ended");
	}

	public void lookupPage(int i) {
		String url = String.format(MASTERCVV_RU, i);
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			LOG.error(url, e);
		}
		if (null == doc)
			return;
		Elements elements = doc.select("tbody tr");
		elements.parallelStream().forEach(j -> tr(j));
		pageNumList.remove(i);
	}

	public void tr(Element j) {
		Elements elements = j.select("td");
		if (null == elements || elements.size() == 0)
			return;
		String bin = elements.get(0).text().trim();
		String bank = null;
		Element bankElmt = elements.get(1);
		if (StringUtils.isNotEmpty(bankElmt.text())) {
			bank = bankElmt.text();
		}
		String network = elements.get(2).text();
		Element countryElmt = elements.get(3);
		String country = countryElmt.selectFirst("img").attr("title");
		String type = null;
		Element typeElmt = elements.get(4);
		if (StringUtils.isNotEmpty(typeElmt.text())) {
			type = typeElmt.text();
		}
		String level = null;
		Element levelElmt = elements.get(5);
		if (StringUtils.isNotEmpty(levelElmt.text())) {
			level = levelElmt.text();
		}
		if (!pg.exist(bin)) {
			LOG.info("insert bin={}, bank={}, network={}, country={}, type={}, level={}", bin, bank, network, country,
					type, level);
			pg.insertBin(bin, bank, network, country, type, level);
		}
	}

}
