package com.etwin.service.report.excel.impl;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.etwin.service.report.excel.ExcelDataService;
import com.etwin.service.report.saledata.SaleDataService;
import com.nami.excelexport.module.ExcelModule;
import com.nami.excelexport.module.SheetModule;
import com.nami.excelexport.userinterface.ExcelExpUtil;
import com.nami.excelimport.bean.ExcelData;
import com.nami.excelimport.bean.ExcelStruct;
import com.nami.excelimport.bean.ImportCellDesc;
import com.nami.excelimport.userinterface.ExcelImportUtil;
import com.nami.excelimport.util.ParseXMLUtil;

@Service
public class ExcelDataServiceImpl implements ExcelDataService {
	private static final String XLS = ".xls";
	@Value("${sale_data_path}")
	String filePath;
	@Value("${template_path}")
	String templatePath;
	@Value("${output_file}")
	String outPutfile;
	@Value("${output.excel.template}")
	String outPutTemplate;

	@Autowired
	SaleDataService saleDataService;

	public List<String> importExcelData(String fromDate, String toDate) {
		List<String> messages = new ArrayList<String>();
		try {
			Map<String, String> salesFiles = getSaleExcelFile();
			Map<String, String> airlineFiles = getAirlineExcelFile();
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			Calendar start = Calendar.getInstance();
			Calendar end = Calendar.getInstance();
			start.setTime(format.parse(fromDate));
			end.setTime(format.parse(toDate));
			while (end.compareTo(start) >= 0) {
				String date = format.format(start.getTime());
				start.add(Calendar.DAY_OF_MONTH, 1);
				importExcelData(date, messages, salesFiles);
				importExcelData(date, messages, airlineFiles);
			}
		} catch (ParseException e) {
			messages.add("导入文件失败");
		}
		return messages;
	}

	private void importExcelData(String date, List<String> messages, Map<String, String> importFiles) {

		for (Map.Entry<String, String> fileEntry : importFiles.entrySet()) {
			String[] excelFiles = fileEntry.getValue().split(",");
			for (String file : excelFiles) {
				try {
					importExcelData(fileEntry, filePath + date + "/" + file);
					messages.add(file + " 导入完成");
				} catch (Exception e) {
					messages.add(file + " 导入失败");
				}
			}
		}
	}

	public List<Map<String, ImportCellDesc>> getExcelData(Map.Entry<String, String> fileEntry, String file) throws Exception {
		List<Map<String, ImportCellDesc>> result = new ArrayList<Map<String, ImportCellDesc>>();
		Workbook wb = new HSSFWorkbook(new FileInputStream(file));
		int sheetNum = wb.getNumberOfSheets();
		for (int i = 0; i < sheetNum; i++) {
			InputStream importExcelStream = new FileInputStream(file);
			InputStream xmlInputStream = new ClassPathResource(templatePath + fileEntry.getKey()).getInputStream();
			ExcelData excelData = ExcelImportUtil.readExcel(xmlInputStream, importExcelStream, i);
			result.addAll(excelData.getRepeatData());
		}
		return result;
	}

	public void importExcelData(Map.Entry<String, String> fileEntry, String file) throws Exception {
		List<Map<String, ImportCellDesc>> datas = getExcelData(fileEntry, file);
		saleDataService.importExcelData(datas);
	}

	public void exportCheckAccountData(String xmlTemplate) throws Exception {
		List<SheetModule> sheetData = new ArrayList<SheetModule>();
		Map<String, String> onceData = new HashMap<String, String>();
		InputStream xmlInputStream = new ClassPathResource(templatePath + xmlTemplate).getInputStream();
		ExcelStruct excelStruct = ParseXMLUtil.parseImportStruct(xmlInputStream);
		List<ImportCellDesc> columns = excelStruct.getRepeatImportCells();
		List<Map<String, String>> exportExcelData = saleDataService.exportExcelData(columns);
		SheetModule matchSheet = new SheetModule(onceData, exportExcelData);
		sheetData.add(matchSheet);
		ExcelModule excelModule = new ExcelModule(sheetData, null);
		ExcelExpUtil.expExcel(excelModule, outPutTemplate, outPutfile);
	}

	private Map<String, String> getSaleExcelFile() {
		Map<String, String> importFileMap = new HashMap<String, String>();
		importFileMap.put("b2b-ETWIN.xml", "b2b-ETWIN" + XLS);
		importFileMap.put("bsp-ETWIN.xml", "bsp-ETWIN" + XLS);
		return importFileMap;
	}

	private Map<String, String> getAirlineExcelFile() {
		Map<String, String> importFileMap = new HashMap<String, String>();
		importFileMap.put("SZX348.xml", "b2b-SZX348" + XLS);
		importFileMap.put("sch641-3u.xml", "b2b-sch641-3u" + XLS);
		importFileMap.put("pek410-mu.xml", "pek410-mu" + XLS);
		importFileMap.put("szx348-mu.xml", "szx348-mu" + XLS);
		importFileMap.put("bsp-1.xml", "bsp-1" + XLS);
		importFileMap.put("bsp-3.xml", "bsp-3" + XLS);
		importFileMap.put("bop.xml", "bsp-bop" + XLS);
		return importFileMap;
	}
}
