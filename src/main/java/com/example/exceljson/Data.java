package com.example.exceljson;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import org.apache.poi.ss.usermodel.FillPatternType;

@lombok.Data
@HeadStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND, fillForegroundColor = 255)
public class Data {
    @ColumnWidth(15)
    @ExcelProperty(" ")
    private String column1;

    @ColumnWidth(2)
    @ExcelProperty("  ")
    private String column2;

    @ColumnWidth(30)
    @ExcelProperty("简中")
    private String column3;

    @ColumnWidth(2)
    @ExcelProperty("  ")
    private String column4;

    @ColumnWidth(30)
    @ExcelProperty(" ")
    private String column5;

}
