package com.etwin.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

public class TestExcel {

	public static void main(String[] args) throws Exception {
		InputStream importExcelStream = new FileInputStream(new File("E:/project/etwin-report/src/test/resources/data/20140313/b2b-ETWIN20140307.xls"));
		Workbook wb = new HSSFWorkbook(importExcelStream);
		TestExcel testExcel = new TestExcel();
		Sheet sheet = wb.getSheet("sheet0");
		String value= testExcel.getMergedRegionValue(sheet,6,1);
		boolean merge = testExcel.isMergedRegion(sheet,6,1);
		System.out.println(value +"==" + merge);
		Row row6 = sheet.getRow(6);
		Row row7 = sheet.getRow(7);
		
		value= testExcel.getMergedRegionValue(sheet,5,1);
		merge = testExcel.isMergedRegion(sheet,5,1);
		System.out.println(value +"==" + merge);		
		
	}
	/**
	 * 获取合并单元格的值
	 * @param sheet
	 * @param row
	 * @param column
	 * @return
	 */
	public String getMergedRegionValue(Sheet sheet ,int row , int column){
		int sheetMergeCount = sheet.getNumMergedRegions();
		for(int i = 0 ; i < sheetMergeCount ; i++){
			CellRangeAddress ca = sheet.getMergedRegion(i);
			int firstColumn = ca.getFirstColumn();
			int lastColumn = ca.getLastColumn();
			int firstRow = ca.getFirstRow();
			int lastRow = ca.getLastRow();
			
			if(row >= firstRow && row <= lastRow){
				
				if(column >= firstColumn && column <= lastColumn){
					Row fRow = sheet.getRow(firstRow);
					Cell fCell = fRow.getCell(firstColumn);
					
					return getCellValue(fCell) ;
				}
			}
		}
		
		return null ;
	}
	
	/**
	 * 判断指定的单元格是否是合并单元格
	 * @param sheet
	 * @param row
	 * @param column
	 * @return
	 */
	public boolean isMergedRegion(Sheet sheet , int row , int column){
		int sheetMergeCount = sheet.getNumMergedRegions();
		
		for(int i = 0 ; i < sheetMergeCount ; i++ ){
			CellRangeAddress ca = sheet.getMergedRegion(i);
			int firstColumn = ca.getFirstColumn();
			int lastColumn = ca.getLastColumn();
			int firstRow = ca.getFirstRow();
			int lastRow = ca.getLastRow();
			
			if(row >= firstRow && row <= lastRow){
				if(column >= firstColumn && column <= lastColumn){
					return true ;
				}
			}
		}
		
		return false ;
	}
	
	/**
	 * 获取单元格的值
	 * @param cell
	 * @return
	 */
	public String getCellValue(Cell cell){
		if(cell == null) return "";
		if(cell.getCellType() == Cell.CELL_TYPE_STRING){
			return cell.getStringCellValue();
		}else if(cell.getCellType() == Cell.CELL_TYPE_BOOLEAN){
			return String.valueOf(cell.getBooleanCellValue());
			
		}else if(cell.getCellType() == Cell.CELL_TYPE_FORMULA){
			
			return cell.getCellFormula() ;
			
		}else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
			
			return String.valueOf(cell.getNumericCellValue());
			
		}
		
		return "";
	}
}
