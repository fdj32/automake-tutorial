package io.github.fdj32.runner;

import io.github.fdj32.service.DbImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DbImportRunner implements CommandLineRunner {

    @Autowired
    private DbImportService dbImportService;

    @Override
    public void run(String... args) throws Exception {
        dbImportService.importToMysql();
    }
}
