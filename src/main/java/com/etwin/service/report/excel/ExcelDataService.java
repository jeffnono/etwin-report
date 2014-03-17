package com.etwin.service.report.excel;

import java.util.List;
import java.util.Map;

import com.nami.excelexport.module.ExcelModule;
import com.nami.excelimport.bean.ImportCellDesc;

public interface ExcelDataService {
	
	public List<String> importExcelData(String fromDate,String toDate);
	public List<Map<String, ImportCellDesc>> getExcelData(Map.Entry<String, String> fileEntry, String file) throws Exception;

	public void importExcelData(Map.Entry<String, String> fileEntry, String file) throws Exception ;
	
	public void exportCheckAccountData(String xmlTemplate) throws Exception;
}
