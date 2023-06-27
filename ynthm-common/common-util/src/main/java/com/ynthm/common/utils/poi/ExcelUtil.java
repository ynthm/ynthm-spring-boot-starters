package com.ynthm.common.utils.poi;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.XMLHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ExcelUtil {

  private String fileName;
  private SimpleSheetContentsHandler handler;
  // 测试使用对比使用SAX和UserModel模式选择（实际使用不需要）
  private Integer saxInterupt;

  private void setHandler(SimpleSheetContentsHandler handler) {
    this.handler = handler;
  }

  // 放置读取数据
  protected List<List<String>> table = new ArrayList<>();

  public ExcelUtil(String filename, Integer saxInterupt) {
    this.fileName = filename;
    this.saxInterupt = saxInterupt;
  }

  public List<List<String>> parse() {
    OPCPackage opcPackage = null;
    InputStream inputStream = null;

    try {
      FileInputStream fileStream = new FileInputStream(fileName);
      opcPackage = OPCPackage.open(fileStream);
      XSSFReader xssfReader = new XSSFReader(opcPackage);

      StylesTable styles = xssfReader.getStylesTable();
      ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opcPackage);
      inputStream = xssfReader.getSheetsData().next();

      processSheet(styles, strings, inputStream);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      if (opcPackage != null) {
        try {
          opcPackage.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return table;
  }

  // 确定XMLReader解析器，使用SAX模式解析xml文件
  private void processSheet(
      StylesTable styles, ReadOnlySharedStringsTable strings, InputStream sheetInputStream)
      throws SAXException, ParserConfigurationException, IOException {
    XMLReader sheetParser = XMLHelper.newXMLReader();

    if (handler == null) {
      setHandler(new SimpleSheetContentsHandler());
    }
    sheetParser.setContentHandler(new XSSFSheetXMLHandler(styles, strings, handler, false));

    try {
      sheetParser.parse(new InputSource(sheetInputStream));
    } catch (RuntimeException e) {
      System.out.println("---> 遇到空行读取文件结束！");
    }
  }

  // 实现SheetContentsHandler
  public class SimpleSheetContentsHandler implements XSSFSheetXMLHandler.SheetContentsHandler {
    protected List<String> row;

    @Override
    public void startRow(int rowNum) {
      row = new LinkedList<>();
    }

    @Override
    public void endRow(int rowNum) {
      // 判断是否使用异常作为文件读取结束（有些Excel文件格式特殊，导致很多空行，浪费内存）
      if (saxInterupt == 1) {
        if (row.isEmpty()) {
          throw new RuntimeException("Excel文件读取完毕");
        }
      }
      // 添加数据到list集合
      table.add(row);
    }

    /**
     * 所有单元格数据转换为string类型，需要自己做数据类型处理
     *
     * @param cellReference 单元格索引
     * @param formattedValue 单元格内容（全部被POI格式化为字符串）
     * @param comment
     */
    @Override
    public void cell(String cellReference, String formattedValue, XSSFComment comment) {
      row.add(formattedValue);
    }

    @Override
    public void headerFooter(String text, boolean isHeader, String tagName) {}
  }
}
