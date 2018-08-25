package ru.mastercvv.job;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ru.mastercvv.dao.Pgsql;

@Service
public class ID20M {

	private static final Logger LOG = LoggerFactory.getLogger(ID20M.class);

	@Autowired
	private Pgsql pg;

	@Scheduled(fixedRate = 3000)
	public void list() throws IOException {
		LOG.info("ID20M.list() started");
		Files.list(Paths.get("D:\\BaiduNetdiskDownload\\2000万身份信息查询工具\\信息查询工具")).forEach(i -> {
			try {
				path(i);
			} catch (IOException e) {
				LOG.error(e.getMessage());
			}
		});
		LOG.info("ID20M.list() ended");
	}

	public void path(Path path) throws IOException {
		int count = 0;
		if (!path.toString().endsWith("csv"))
			return;
		Iterator<CSVRecord> it = null;
		if (path.toString().endsWith("-600W.csv"))
			it = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(Files.newBufferedReader(path)).iterator();
		List<CSVRecord> list = new ArrayList<CSVRecord>();
		while (null != it && it.hasNext()) {
			CSVRecord r = it.next();
			if (r.size() != 33) {
				list.add(r);
				// LOG.info(r.toString());
			} else {
				count++;
			}
		}
		System.out.println("count=" + count);
		System.out.println("list.size()=" + list.size());
		// list.stream().forEach(i -> System.out.println(i.size()));
		List<String> ls = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			concat(ls, list.get(i));
			if (ls.size() == 33) {
				count++;
				ls = new ArrayList<String>();
			}
		}
		System.out.println("count=" + count);
	}

	public void concat(List<String> list, CSVRecord record) {
		int size = list.size();
		if (size == 0) {
			list.add(record.get(0));
		} else {
			String last = list.get(size - 1);
			list.remove(size - 1);
			list.add(last + " " + record.get(0));
		}
		for (int i = 1; i < record.size(); i++) {
			list.add(record.get(i));
		}
	}

}
