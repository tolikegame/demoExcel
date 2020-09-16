package com.example.exceljson;

import com.alibaba.excel.EasyExcel;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

//類json轉excel
@SpringBootTest
class ExcelJsonApplicationTests {

    // 雙引號 "" 內填入要轉換的資料
    private static String json = "";


    @Test
    void main() throws IOException {

        //逐行讀取資料
        BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(json.getBytes(Charset.forName("utf8")))));
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
            }//客製解析格式為 -> picker: {
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

        //excel存檔位址
        //存檔位置格式-> /C:/temp/text.xlsx
        //不能直接丟檔案到C槽如:/C:/text.xlsx 會爆掉!!
        String fileName = ExcelJsonApplicationTests.class.getResource("/").getPath()+"test.xlsx";
        String sheetName = "sheet1";
        //寫出excel的地方
        //Data.class = 控制Head的功能
        //sheetName = 寫頁籤的地方
        //datas = 逐行寫出資料
        EasyExcel.write(fileName, DataForJson.class).sheet(sheetName).doWrite(datas);
    }

    /**
     * 讀取某個資料夾下的所有檔案
     */
    public static boolean readfile(String filepath) throws FileNotFoundException, IOException {
        try {

            File file = new File(filepath);
            if (!file.isDirectory()) {
                System.out.println("檔案");
                System.out.println("path=" + file.getPath());
                System.out.println("absolutepath=" + file.getAbsolutePath());
                System.out.println("name=" + file.getName());

            } else if (file.isDirectory()) {
                System.out.println("資料夾");
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File readfile = new File(filepath + "//" + filelist[i]);
                    if (!readfile.isDirectory()) {
                        System.out.println("path=" + readfile.getPath());
                        System.out.println("absolutepath="
                                + readfile.getAbsolutePath());
                        System.out.println("name=" + readfile.getName());

                    } else if (readfile.isDirectory()) {
                        readfile(filepath + "//" + filelist[i]);
                    }
                }

            }

        } catch (FileNotFoundException e) {
            System.out.println("readfile()   Exception:" + e.getMessage());
        }
        return true;
    }


}
