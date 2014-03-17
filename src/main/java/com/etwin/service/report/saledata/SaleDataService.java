package com.etwin.service.report.saledata;

import java.util.List;
import java.util.Map;

import com.nami.excelimport.bean.ImportCellDesc;

public interface SaleDataService {
	
	public void importExcelData(List<Map<String, ImportCellDesc>> datas) throws Exception;
	public List<Map<String, String>> exportExcelData(List<ImportCellDesc> columns) throws Exception;
	
}
