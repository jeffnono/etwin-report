package com.etwin.service.report.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.nami.excelimport.bean.ExcelData;
import com.nami.excelimport.bean.ImportCellDesc;
import com.nami.excelimport.userinterface.ExcelImportUtil;
import com.nami.excelimport.util.StringUtil;

@Service
public class ExcelDataService {
	String filePath = "file:E:/project/etwin-report/src/test/resources/data/";
	String templatePath = "file:E:/project/etwin-report/src/main/resources/template/";

	@Autowired
	JdbcTemplate jdbcTemplate;

	public void importExcelData(Map.Entry<String, String> fileEntry, String file) throws Exception {
		List<Map<String, ImportCellDesc>> datas = getExcelData(fileEntry, file);
		List<Object[]> paramaters = new ArrayList<Object[]>();
		for (Map<String, ImportCellDesc> data : datas) {
			List<Object> cellData = new ArrayList<Object>();
			for (Map.Entry<String, ImportCellDesc> entry : data.entrySet()) {
				cellData.add(entry.getValue().getFieldValue());
			}
			paramaters.add(cellData.toArray(new Object[cellData.size()]));
		}
		String sql = getDynamicSQL(datas.get(0));
		jdbcTemplate.batchUpdate(sql, paramaters);
	}

	public List<Map<String, ImportCellDesc>> getExcelData(Map.Entry<String, String> fileEntry, String file) throws FileNotFoundException {
		InputStream importExcelStream = new FileInputStream(ResourceUtils.getFile(filePath + file));
		FileInputStream xmlInputStream = new FileInputStream(ResourceUtils.getFile(templatePath + fileEntry.getKey()));
		ExcelData excelData = ExcelImportUtil.readExcel(xmlInputStream, importExcelStream);
		List<Map<String, ImportCellDesc>> datas = excelData.getRepeatData();
		return datas;
	}

	public String getDynamicSQL(Map<String, ImportCellDesc> columnsMap) {
		String tableName = "";
		List<String> cloumns = new ArrayList<String>();
		List<String> dynamicSplit = new ArrayList<String>();
		for (Map.Entry<String, ImportCellDesc> entry : columnsMap.entrySet()) {
			cloumns.add(entry.getValue().getFieldName());
			dynamicSplit.add("?");
			if (StringUtil.isEmpty(tableName)) {
				tableName = entry.getValue().getTargetTable();
			}
		}
		StringBuffer sqlBuff = new StringBuffer();
		sqlBuff.append("INSERT  INTO " + tableName + "(");
		sqlBuff.append(StringUtils.join(cloumns, ",") + ")");
		sqlBuff.append(" VALUES (" + StringUtils.join(dynamicSplit, ",") + " )");
		return sqlBuff.toString();
	}

}
