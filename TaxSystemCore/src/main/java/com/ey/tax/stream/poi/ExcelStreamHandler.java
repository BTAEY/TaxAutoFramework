package com.ey.tax.stream.poi;

import com.ey.tax.exceptions.ExcelOperationException;
import com.ey.tax.performance.TimeLoggerUtil;
import com.ey.tax.stream.poi.model.ColumnHeaderModel;
import com.ey.tax.stream.poi.model.RowModel;
import com.google.common.base.CharMatcher;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.springframework.stereotype.Component;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhuji on 2/6/2018.
 */
@Component
public class ExcelStreamHandler {
    private static final Logger logger = LogManager.getLogger();

    private final TimeLoggerUtil timeLoggerUtil = TimeLoggerUtil.getInstance();

    public List<RowModel> read(InputStream input, List<String> configHeaders) throws Exception {
        timeLoggerUtil.start("ExcelStreamHandler<read>");
        InputStream sheet2 = null;
        try {
            OPCPackage pkg = OPCPackage.open(input);
            XSSFReader r = new XSSFReader(pkg);
            SharedStringsTable sst = r.getSharedStringsTable();

            SheetHandler sheetHandler = new SheetHandler(sst, configHeaders);
            XMLReader parser = fetchSheetParser(sheetHandler);

            // 获得第一个sheet
            sheet2 = r.getSheet("rId1");
            InputSource sheetSource = new InputSource(sheet2);
            parser.parse(sheetSource);
            Map<Integer, RowModel> rows = sheetHandler.getRows();
            List<RowModel> result = Lists.newLinkedList(Iterables.transform(rows.entrySet(), new Function<Map.Entry<Integer,RowModel>, RowModel>() {
                @Override
                public RowModel apply(final Map.Entry<Integer, RowModel> input){
                    return  input.getValue();
                }
            }));
            result = FluentIterable.from(result).filter(new Predicate<RowModel>() {
                @Override
                public boolean apply(RowModel input) {
                    return !input.isEmpty();
                }
            }).toList();
            logger.debug("total amount to import : "+result.size()+ " rows.");
            return result;
        }catch (Exception e){
            logger.error("handle excel occurs error.",e);
            throw new ExcelOperationException("handle excel occurs error.",e);
        } finally {
            if(sheet2 != null){
                sheet2.close();
            }
            timeLoggerUtil.stop();
        }
    }

    public XMLReader fetchSheetParser(ContentHandler handler) throws SAXException {
        XMLReader parser =
                XMLReaderFactory.createXMLReader(
                        "org.apache.xerces.parsers.SAXParser"
                );
        parser.setContentHandler(handler);
        return parser;
    }

    /**
     * 处理sax的handler
     */
    private static class SheetHandler extends DefaultHandler {
        private SharedStringsTable sst;
        private String lastContents;
        private boolean nextIsString;
        private Map<Integer,RowModel> rows = new HashMap<>();
        private Map<String,ColumnHeaderModel> headerIndexMapping = new HashMap<>();
        private RowModel currentRow;

        private int currentRowIndex;
        private String currentColumnLetter;

        private List<String> configHeaders;

        private SheetHandler(SharedStringsTable sst,List<String> configHeaders) {
            this.sst = sst;
            this.configHeaders = configHeaders;
        }

        //元素开始时的handler
        public void startElement(String uri, String localName, String name,
                                 Attributes attributes) throws SAXException {
            // c => 单元格
            if(name.equals("c")) {
                String rowMark = attributes.getValue("r");
                Integer rowMarkIndex = Integer.valueOf(CharMatcher.DIGIT.retainFrom(rowMark));
                currentRowIndex = rowMarkIndex;
                String columnMarkLetter = CharMatcher.JAVA_LETTER.retainFrom(rowMark);
                currentColumnLetter = columnMarkLetter;
                if(rowMarkIndex == 1){
                    headerIndexMapping.put(columnMarkLetter,new ColumnHeaderModel(columnMarkLetter));
                }else{
                    if(rows.containsKey(rowMarkIndex)){
                        currentRow = rows.get(rowMarkIndex);
                    }else{
                        currentRow = new RowModel(rowMarkIndex);
                        rows.put(rowMarkIndex,currentRow);
                    }
                }
//                System.out.print(attributes.getValue("r") + " - ");
                // 获取单元格类型
                String cellType = attributes.getValue("t");
//                System.out.print(cellType+" - ");
                if(cellType != null && cellType.equals("s")) {
                    nextIsString = true;
                } else {
                    nextIsString = false;
                }
            }
            lastContents = "";
        }

        //元素结束时的handler
        public void endElement(String uri, String localName, String name)
                throws SAXException {
            if(nextIsString) {
                int idx = Integer.parseInt(lastContents);
                lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
                nextIsString = false;
            }


            // v => 单元格内容
            if(name.equals("v")) {
                ColumnHeaderModel columnHeaderModel = headerIndexMapping.get(currentColumnLetter);
                if(currentRowIndex == 1){
                    if(StringUtils.isEmpty(lastContents)){
                        return;
                    }
                    if(configHeaders.contains(lastContents)){
                        columnHeaderModel.setHeader(lastContents);
                    }else{
                        headerIndexMapping.remove(currentColumnLetter);
                    }

                }else{
                    if(columnHeaderModel == null){
                        return;
                    }
                    currentRow = rows.get(currentRowIndex);
                    if(currentRow.isEmpty() && StringUtils.isNotEmpty(lastContents)){
                        currentRow.setEmpty(false);
                    }
                    currentRow.getHeaderValueMapping().put(columnHeaderModel.getHeader(),lastContents);
                }
//                System.out.println(lastContents);
            }
            if(name.equals("c") && currentRowIndex != 1){
                ColumnHeaderModel columnHeaderModel = headerIndexMapping.get(currentColumnLetter);
                if(columnHeaderModel == null){
                    return;
                }
                currentRow = rows.get(currentRowIndex);
                currentRow.getHeaderValueMapping().put(columnHeaderModel.getHeader(),lastContents);
            }
        }

        //读取元素间内容时的handler
        public void characters(char[] ch, int start, int length)
                throws SAXException {
            lastContents += new String(ch, start, length);
        }

        public Map<Integer, RowModel> getRows() {
            return rows;
        }

        public Map<String, ColumnHeaderModel> getHeaderIndexMapping() {
            return headerIndexMapping;
        }
    }
}
