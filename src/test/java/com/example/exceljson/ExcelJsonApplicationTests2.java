package com.example.exceljson;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

//讀取資料夾底下的類json轉excel
//註解的大部分為讀取指定單一的檔案
@SpringBootTest
public class ExcelJsonApplicationTests2 {
    // 雙引號 "" 內填入要轉換的資料
//    private static String json = "";

    private static List<List<DataForJson>> sheetDatas = new ArrayList<>();
    private static List<DataForJson> datas = new ArrayList<>();
    private static List<String> sheets = new ArrayList<>();
    private int sheetCount = 0;

    @Test
    public void main() throws IOException {
        //讀檔資料夾位置
        String path = "C:\\gamebox-i18n";
        this.readfile(path);
        this.export(datas);
    }

    public List<DataForJson> parse(BufferedReader br) throws IOException {
        //逐行讀取資料
//        BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(json.getBytes(Charset.forName("utf8")))));
        String line;
        List<DataForJson> datas = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int i=0;
        while((line=br.readLine()) != null){
            //針對格式有空行做處理
            if(line.contains(":") && !line.contains("\',") && !line.contains("{")){
                sb.append(line);
                i++;
                continue;
            }
            if(i == 1){
                sb.append(line);
                line = String.valueOf(sb);
                i=0;
                sb = new StringBuilder();
            }

            //如果有頓號 轉換成單引號
            if(line.contains("`")){
                line = line.replace("`","\'");
            }
            //主要解析功能邏輯
            if(line.contains("\',")){
                line = line.trim();
                DataForJson data = new DataForJson();
                String[] cut1 = line.split(":");
                data.setColumn1(cut1[0]);
                data.setColumn2(":\'");
                String[] cut2 = cut1[1].split("',");
                //去掉多餘的單引號
                data.setColumn3(cut2[0].replace("\'",""));
                data.setColumn4("\',");
                if(cut2.length == 2){
                    data.setColumn5(cut2[1]);
                }
                datas.add(data);
            }//客製解析格式 例如: picker: {
            else if(line.contains(": {")){
                line = line.trim();
                DataForJson data = new DataForJson();
                String[] cut1 = line.split(":");
                data.setColumn1(cut1[0]);
                data.setColumn2("");
                data.setColumn3("{");
                data.setColumn4("");
                data.setColumn5("");
                datas.add(data);
            }else {
                DataForJson data = new DataForJson();
                data.setColumn1(line);
                data.setColumn2("");
                data.setColumn3("");
                data.setColumn4("");
                data.setColumn5("");
                datas.add(data);
            }
        }
        return datas;
    }

    public void export(List<DataForJson> datas){
        //簡單寫出一份資料
//        //excel存檔位址
//        //存檔位置格式-> /C:/temp/text.xlsx
//        //不能直接丟檔案到C槽如:/C:/text.xlsx 會爆掉!!
//        String fileName = ExcelJsonApplicationTests.class.getResource("/").getPath()+"test.xlsx";
//        String sheetName = "sheet1";
//        //寫出excel的地方
//        //Data.class = 控制Head的功能
//        //sheetName = 寫頁籤的地方
//        //datas = 逐行寫出資料
//        EasyExcel.write(fileName, DataForJson.class).sheet(sheetName).doWrite(datas);

        String fileName = ExcelJsonApplicationTests.class.getResource("/").getPath()+"test.xlsx";

        ExcelWriter excelWriter = null;
        try {
            // 这里 需要指定写用哪个class去写
            excelWriter = EasyExcel.write(fileName, DataForJson.class).build();

//            // 这里注意 如果同一个sheet只要创建一次
//            String sheetName = "sheet1";
//            WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).build();

            // 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来
            for (int i = 0; i < sheets.size(); i++) {
                WriteSheet writeSheet = EasyExcel.writerSheet(i, sheets.get(i)).head(DataForJson.class).build();
                // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
                excelWriter.write(sheetDatas.get(i), writeSheet);
            }
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }


    /**
     * 讀取某個資料夾下的所有檔案
     */
    public void readfile(String filepath) throws FileNotFoundException, IOException {
        try {
            File file = new File(filepath);
            if (!file.isDirectory()) {
                System.out.println("檔案:"+file.getName());
            } else if (file.isDirectory()) {
                String[] filelist = file.list();
                //sheet分頁
                if(sheetCount > 0){
                    sheets.add(file.getName());
                }
                sheetCount++;

                for (int i = 0; i < filelist.length; i++) {
                    File readfile = new File(filepath + "//" + filelist[i]);
                    if (!readfile.isDirectory()) {
                        FileReader fr = new FileReader(readfile);
                        BufferedReader br = new BufferedReader(fr);
                        List<DataForJson> datas = this.parse(br);
                        this.datas.addAll(datas);

                    } else if (readfile.isDirectory()) {
                        //繼續找下一層
                        readfile(filepath + "//" + filelist[i]);
                    }
                }
                //每讀完檔案下全資料換下一個
                sheetDatas.add(datas);
                this.datas = new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            System.out.println("readfile()   Exception:" + e.getMessage());
        }
    }
}
