package com.etwin.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import util.BaseSpringTest;

import com.etwin.service.report.excel.ExcelDataService;


/**
 * Unit test for simple App.
 */
public class ExcelTest extends BaseSpringTest {
  
	@Autowired
	ExcelDataService excelDataService;
	
	@Test
	public void testAllData() {
		System.out.println("begin test SaleData");
		List<String> messages = excelDataService.importExcelData("20140305","20140305");
		for (String message :messages){
			System.out.println(message);
		}
		Assert.assertTrue(messages.size()>0);;
	}
	
	@Test
	public void testSaleData() {
		System.out.println("begin test SaleData");
		long start = System.currentTimeMillis();
		Map<String, String> importFiles = getSaleExcelFile();
		importExcelData(importFiles);
		long end = System.currentTimeMillis();
		System.out.println("end test SaleData=" + (end-start));
	}
	
	@Test
	public void testAirlineData() {
		System.out.println("begin test AirlineData");
		long start = System.currentTimeMillis();
		Map<String, String> importFiles = getAirlineExcelFile();
		importExcelData(importFiles);
		long end = System.currentTimeMillis();
		System.out.println("end test AirlineData=" + (end-start));
	}

	@Test
	public void exportSaleData() {
		System.out.println("begin test exportSaleData");
		long start = System.currentTimeMillis();
		try {
		  excelDataService.exportCheckAccountData("b2b-ETWIN.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("end test exportSaleData=" + (end-start));
	}
	
	
	private void importExcelData(Map<String, String> importFiles) {
		try {
			for (Map.Entry<String, String> fileEntry : importFiles.entrySet()) {
				String[] excelFiles = fileEntry.getValue().split(",");
				for (String file : excelFiles) {
					excelDataService.importExcelData(fileEntry, file);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	private Map<String, String> getSaleExcelFile(){
		Map<String, String> importFileMap = new HashMap<String, String>();
		importFileMap.put("b2b-ETWIN.xml", "b2b-ETWIN305.xls");
		importFileMap.put("bsp-ETWIN.xml", "bsp-ETWIN20140305.xls");
		return importFileMap;
	}

	private Map<String, String> getAirlineExcelFile() {
		Map<String, String> importFileMap = new HashMap<String, String>();
		importFileMap.put("SZX348.xml", "b2b-SZX34820140305.xls");
		importFileMap.put("sch641-3u.xml", "b2b-sch641-3u.xls");
		importFileMap.put("pek410-mu.xml", "pek410-mu.xls");
		importFileMap.put("szx348-mu.xml", "szx348-mu.xls");
		importFileMap.put("bsp-1.xml", "bsp-1.xls");
		importFileMap.put("bsp-3.xml", "bsp-3.xls");
		importFileMap.put("bop.xml", "bsp-bop.xls");
		return importFileMap;
	}
}
