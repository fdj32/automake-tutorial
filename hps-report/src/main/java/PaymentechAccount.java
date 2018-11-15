import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class PaymentechAccount {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		List<String> list = Files.lines(Paths.get("/Users/nickfeng/Desktop/have_approved_txn.txt"))
				.collect(Collectors.toList());
		try (InputStream inp = new FileInputStream("/Users/nickfeng/Desktop/Active Network TDs - 203554.xlsx")) {
			Workbook wb = WorkbookFactory.create(inp);
			Sheet sheet = wb.getSheetAt(0);
			Iterator<Row> ri = sheet.rowIterator();
			while (ri.hasNext()) {
				Row r = ri.next();
				Cell c = r.getCell(3);
				Cell c5 = r.createCell(5);
				if (CellType.STRING == c.getCellType()) {
					c5.setCellType(CellType.STRING);
					c5.setCellValue("Have approved txn in last 3 months");
				} else if (CellType.NUMERIC == c.getCellType()) {
					String td = "" + (int) c.getNumericCellValue();
					c5.setCellType(CellType.BOOLEAN);
					if (list.contains(td)) {
						c5.setCellValue(true);
					} else {
						c5.setCellValue(false);
					}
				}
			}
			try (OutputStream fileOut = new FileOutputStream(
					"/Users/nickfeng/Desktop/Active Network TDs - 203554-AMS.xlsx")) {
				wb.write(fileOut);
			}
		}
	}

}
