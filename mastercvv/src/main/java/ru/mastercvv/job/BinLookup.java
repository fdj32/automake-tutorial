package ru.mastercvv.job;

import java.io.IOException;
import java.util.stream.IntStream;

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

	@Autowired
	private Pgsql pg;

	@Scheduled(fixedRate = 3000)
	public void lookup() throws IOException {
		LOG.info("lookup() started");
		IntStream.range(1, 11382).parallel().forEach(i -> lookupPage(i));
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
