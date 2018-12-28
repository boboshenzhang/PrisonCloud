/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package top.ibase4j.core.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 字典工具类
 * 
 * @author jeeplus
 * @version 2013-5-29
 */
public class ReadExclUtils {
	public static void main(String[] args) {
		String path = "D:\\2016年贵州省遵义市绥阳县枧坝镇高台村水口组农户信息表.xlsx";
		try {
			// List<List<String>> result = new ReadExclUtils().readXls(path);
			List<List<String>> result = new ReadExclUtils().readXlsx(path);
			System.out.println(result.size());
			for (int i = 0; i < result.size(); i++) {
				List<String> model = result.get(i);
				System.out.println("orderNum:" + model.get(0) + "--> orderAmount:" + model.get(1));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Title: readXls
	 * @Description: 处理xls文件
	 * @param @param
	 *            path
	 * @param @return
	 * @param @throws
	 *            Exception 设定文件
	 * @return List<List<String>> 返回类型
	 * @throws
	 * 
	 * 			从代码不难发现其处理逻辑：
	 *             1.先用InputStream获取excel文件的io流
	 *             2.然后穿件一个内存中的excel文件HSSFWorkbook类型对象，这个对象表示了整个excel文件。
	 *             3.对这个excel文件的每页做循环处理 4.对每页中每行做循环处理 5.对每行中的每个单元格做处理，获取这个单元格的值
	 *             6.把这行的结果添加到一个List数组中 7.把每行的结果添加到最后的总结果中
	 *             8.解析完以后就获取了一个List<List<String>>类型的对象了
	 * 
	 */
	public List<List<String>> readXls(String path) throws Exception {
		InputStream is = new FileInputStream(path);
		// HSSFWorkbook 标识整个excel
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		List<List<String>> result = new ArrayList<List<String>>();
		int size = hssfWorkbook.getNumberOfSheets();
		// 循环每一页，并处理当前循环页
		for (int numSheet = 0; numSheet < size; numSheet++) {
			// HSSFSheet 标识某一页
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			// 处理当前页，循环读取每一行
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				// HSSFRow表示行
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				int minColIx = hssfRow.getFirstCellNum();
				int maxColIx = hssfRow.getLastCellNum();
				List<String> rowList = new ArrayList<String>();
				// 遍历改行，获取处理每个cell元素
				for (int colIx = minColIx; colIx < maxColIx; colIx++) {
					// HSSFCell 表示单元格
					HSSFCell cell = hssfRow.getCell(colIx);
					if (cell == null) {
						continue;
					}
					rowList.add(getStringVal(cell));
				}
				result.add(rowList);
			}
		}
		return result;
	}

	public List<List<String>> readCellXls(String path, int rowId) throws Exception {
		InputStream is = new FileInputStream(path);
		// HSSFWorkbook 标识整个excel
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		List<List<String>> result = new ArrayList<List<String>>();
		int size = hssfWorkbook.getNumberOfSheets();
		// 循环每一页，并处理当前循环页
		for (int numSheet = 0; numSheet < size; numSheet++) {
			// HSSFSheet 标识某一页
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			// 处理当前页，循环读取每一行
			for (int rowNum = rowId; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				// HSSFRow表示行
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				int minColIx = hssfRow.getFirstCellNum();
				int maxColIx = hssfRow.getLastCellNum();
				List<String> rowList = new ArrayList<String>();
				// 遍历改行，获取处理每个cell元素
				for (int colIx = minColIx; colIx < maxColIx; colIx++) {
					// HSSFCell 表示单元格
					HSSFCell cell = hssfRow.getCell(colIx);
					if (cell == null) {
						continue;
					}
					rowList.add(getStringVal(cell));
				}
				result.add(rowList);
			}
		}
		return result;
	}

	/**
	 * 
	 * @Title: readXlsx @Description: 处理Xlsx文件 @param @param
	 * path @param @return @param @throws Exception 设定文件 @return
	 * List<List<String>> 返回类型 @throws
	 */
	public List<List<String>> readCellXlsx(String path, int rowId) throws Exception {
		InputStream is = new FileInputStream(path);
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
		List<List<String>> result = new ArrayList<List<String>>();
		// 循环每一页，并处理当前循环页
		for (XSSFSheet xssfSheet : xssfWorkbook) {
			if (xssfSheet == null) {
				continue;
			}
			// 处理当前页，循环读取每一行
			for (int rowNum = rowId; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);
				int minColIx = xssfRow.getFirstCellNum();
				int maxColIx = xssfRow.getLastCellNum();
				List<String> rowList = new ArrayList<String>();
				for (int colIx = minColIx; colIx < maxColIx; colIx++) {
					XSSFCell cell = xssfRow.getCell(colIx);
					if (cell == null) {
						rowList.add("");
						continue;

					}
					rowList.add(cell.toString());
				}
				result.add(rowList);
			}
		}
		return result;
	}

	/**
	 * 
	 * @Title: readXlsx @Description: 处理Xlsx文件 @param @param
	 * path @param @return @param @throws Exception 设定文件 @return
	 * List<List<String>> 返回类型 @throws
	 */
	public List<List<String>> readXlsx(String path) throws Exception {
		InputStream is = new FileInputStream(path);
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
		List<List<String>> result = new ArrayList<List<String>>();
		// 循环每一页，并处理当前循环页
		for (XSSFSheet xssfSheet : xssfWorkbook) {
			if (xssfSheet == null) {
				continue;
			}
			// 处理当前页，循环读取每一行
			for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);
				int minColIx = xssfRow.getFirstCellNum();
				int maxColIx = xssfRow.getLastCellNum();
				List<String> rowList = new ArrayList<String>();
				for (int colIx = minColIx; colIx < maxColIx; colIx++) {
					XSSFCell cell = xssfRow.getCell(colIx);
					if (cell == null) {
						rowList.add("");
						continue;

					}
					rowList.add(cell.toString());
				}
				result.add(rowList);
			}
		}
		return result;
	}

	public static String getStringVal(HSSFCell cell) {
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue() ? "TRUE" : "FALSE";
		case Cell.CELL_TYPE_FORMULA:
			return cell.getCellFormula();
		case Cell.CELL_TYPE_NUMERIC:
			cell.setCellType(Cell.CELL_TYPE_STRING);
			return cell.getStringCellValue();
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		default:
			return "";
		}
	}
}