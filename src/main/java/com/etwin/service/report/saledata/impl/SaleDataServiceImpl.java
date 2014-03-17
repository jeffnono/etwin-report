package com.etwin.service.report.saledata.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.etwin.service.report.saledata.SaleDataService;
import com.nami.excelimport.bean.ImportCellDesc;
import com.nami.excelimport.util.StringUtil;

@Service
public class SaleDataServiceImpl implements SaleDataService{
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public void importExcelData(List<Map<String, ImportCellDesc>> datas) throws Exception {
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
	
	public List<Map<String, String>> exportExcelData(List<ImportCellDesc> columns) throws Exception {
		String table = columns.get(0).getTargetTable();
		StringBuffer buffer = new StringBuffer("SELECT ");
		List<String> cloumns = new ArrayList<String>();
		for (ImportCellDesc column: columns) {
			cloumns.add(column.getFieldName());
		}
		buffer.append(StringUtils.join(cloumns, ",") + " FROM "  + table);
		List<Map<String, String>> result= new ArrayList<Map<String, String>>();
	/*	jdbcTemplate.queryForMap(buffer.toString(), new RowMapper(){  
      public Object mapRow(ResultSet rs, int rowNum) throws SQLException {  
      	Map<String, String> rowMap =  new HashMap<String, String>();  
      	  String column = rs.
      	  rowMap(rs.getLong("id"),rs.getString(columnIndex));  
          return rowMap;  
       }} );*/
		result.addAll((Collection<? extends Map<String, String>>) jdbcTemplate.queryForList(buffer.toString()));
		return result;
	}
	
	private String getDynamicSQL(Map<String, ImportCellDesc> columnsMap) {
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
