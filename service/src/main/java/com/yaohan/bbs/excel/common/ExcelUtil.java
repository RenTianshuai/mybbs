package com.yaohan.bbs.excel.common;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.read.metadata.ReadSheet;

import java.io.InputStream;
import java.util.List;

/**
 * 读取excel工具类
 */
public class ExcelUtil {

    public static <T> List<T> readExcel(InputStream inputStream, Class T, int rowNo, int sheetNo) {

        ExcelListener excelListener = new ExcelListener<Head>();
        ExcelReader excelReader = EasyExcel.read(inputStream, T, excelListener).build();
        if (excelReader == null) {
            return null;
        }
        ReadSheet readSheet = EasyExcel.readSheet(sheetNo).headRowNumber(rowNo).build();
        excelReader.read(readSheet);
        // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
        excelReader.finish();
        return excelListener.getDatas();

    }

}
