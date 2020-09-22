package com.example.exceljson;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//json轉excel
@SpringBootTest
public class ExcelJsonApplicationTests3 {
    private static List<List<DataForJson>> sheetDatas = new ArrayList<>();
    private static List<DataForJson> datas = new ArrayList<>();
    private static List<String> sheets = new ArrayList<>();
    private int sheetCount = 0;
    private String  fff = "";
    private int count = 0;

    @Test
    public void main() throws IOException {
        //讀檔資料夾位置
        String path = "C:\\gamebox-i18n";
        this.readfile(path);
        this.export();
    }

    public String cover(String line, String flag, StringBuilder sss){

        if(count ==1){
            flag = fff;
        }

        if(line.contains("=> {") || line.contains("=> [")){
            flag = "start";
        }else if(line.contains("},") || line.contains("],")){
            flag = "end";
        }else if(line.contains(":")){
            String[] one = line.split(":");
            if(one.length == 1){
                flag = "start";
//                sss.append(line);
            }
        }
        if(flag.equals("start")){
            sss.append(line);
        }

        fff = flag;
        return flag;
    }

    public List<DataForJson> parse(BufferedReader br) throws IOException {

        String line;
        List<DataForJson> datas = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int i=0;
        int j=0;
        int k=0;
        boolean flag = false;
//        boolean go = false;
//        String flag = "end";
        StringBuilder sss = new StringBuilder();
//        String tempLine;
//        line=br.readLine();
//        tempLine = line;

        String reg1 = "^[a-zA-Z0-9]+:+";
        String reg2 = "',{1}$";

        Pattern pattern1 = Pattern.compile(reg1);
        Pattern pattern2 = Pattern.compile(reg2);

        while((line=br.readLine()) != null){
            line = line.trim();

            //如果有頓號 轉換成單引號
            if(line.contains("`")){
                line = line.replace("`","\'");
            }

            Matcher matcher1 = pattern1.matcher(line);
            Matcher matcher2 = pattern2.matcher(line);


            if(matcher1.find()){
                i+=1;
            }
            if(matcher2.find()){
                i+=1;
            }
            if(line.contains(": {")){
                i+=2;
            }

            if(i == 1 || flag){
                flag = true;
                sss.append(line);
                k=1;
                if(line.contains("=> {")){
                    j=1;
                    continue;
                }else if(line.contains("=> [")){
                    j=2;
                    continue;
                }

                if(j==1){
                    if(String.valueOf(sss).contains("},")){
                        i=0;
                        line = String.valueOf(sss);
                        sss = new StringBuilder();
                        flag = false;
                    }else{
                        continue;
                    }
                }else if(j==2){
                    if(String.valueOf(sss).contains("],")){
                        i=0;
                        line = String.valueOf(sss);
                        sss = new StringBuilder();
                        flag = false;
                    }else {
                        continue;
                    }
                }else if(String.valueOf(sss).contains("\',")){
                    i=0;
                    line = String.valueOf(sss);
                    sss = new StringBuilder();
                    flag = false;
                }else if(k==1){
                    i=0;
                    continue;
                }
            }

            System.out.println("line-> "+line);
            System.out.println("sb-> "+sb);

            //測試
//            String result = this.cover(line, flag, sss);
//            if(result.equals("start")){
//                count+=1;
//                continue;
//            }

            //504
//            if(line.length() < sss.length()){
//                line = String.valueOf(sss);
//            }


            /**
             //如果有頓號 轉換成單引號
             if(line.contains("`")){
             line = line.replace("`","\'");
             }

             if(line.contains("export default {")){
             go = true;
             }

             if(go){
             //針對格式有換行做處理
             if(line.contains(":") && !line.contains("\',") && !line.contains("{") && !line.contains("[")){
             sb.append(line);
             i+=1;
             continue;
             }
             else if(line.contains("=> [")){
             sb.append(line);
             i+=2;
             continue;
             }
             else if(line.contains("=> {")){
             sb.append(line);
             i+=3;
             continue;
             }
             if(i == 1){
             sb.append(line);
             String temp = String.valueOf(sb);
             if(temp.contains("\',")){
             i=0;
             sb = new StringBuilder();
             }else{
             continue;
             }
             line = temp;
             }else if(i == 2){
             sb.append(line);
             String temp = String.valueOf(sb);
             if(temp.contains("],")){
             i=0;
             sb = new StringBuilder();
             }else{
             continue;
             }
             line = temp;
             }
             else if(i == 3) {
             sb.append(line);
             String temp = String.valueOf(sb);
             if (temp.contains("},")) {
             i = 0;
             sb = new StringBuilder();
             } else {
             continue;
             }
             line = temp;
             }
             }
             **/

            //主要解析功能邏輯
            if(line.contains("],")){
                DataForJson data = new DataForJson();
                String[] cut1 = line.split(":");
                data.setColumn1(cut1[0]);
                data.setColumn2(":");
                String[] cut2 = cut1[1].split("],");
                data.setColumn3(cut2[0]);
                data.setColumn4("],");
                if(cut2.length == 2){
                    data.setColumn5(cut2[1]);
                }
                datas.add(data);
            }else if(line.contains("=>")){
                DataForJson data = new DataForJson();
                String[] cut1 = line.split(": ");
                data.setColumn1(cut1[0]);
                data.setColumn2(":");
                String[] cut2;
                if(cut1[1].contains("},")){
                    cut2 = cut1[1].split("},");
                    data.setColumn3(cut2[0]);
                    data.setColumn4("},");
                }else{
                    cut2 = cut1[1].split("',");
                    data.setColumn3(cut2[0]);
                    data.setColumn4("\',");
                }

//                data.setColumn3(cut2[0]);
//                data.setColumn4("\',");

//                String[] cut2 =
//                String[] cut2 = new String[0];
//                if(line.contains("},")){
//                    cut2 = cut1[1].split("},");
//                    data.setColumn3(cut2[0]);
//                    data.setColumn4("},");
//                }else if(line.contains("\',")){
//                    cut2 = cut1[1].split("',");
//                    data.setColumn3(cut2[0]);
//                    data.setColumn4("\',");
//                }
                if(cut2.length == 2){
                    data.setColumn5(cut2[1]);
                }
                datas.add(data);
            }else if(line.contains("\',")){
                DataForJson data = new DataForJson();
                String[] cut1 = line.split(":");
                data.setColumn1(cut1[0]);
                data.setColumn2(":\'");
                String[] cut2 = cut1[1].split("',");
                data.setColumn3(cut2[0]);
                data.setColumn4("\',");
                if(cut2.length == 2){
                    data.setColumn5(cut2[1]);
                }
                datas.add(data);
            }//客製解析格式 例如: picker: {
            else if(line.contains(": {")){
                DataForJson data = new DataForJson();
                String[] cut1 = line.split(":");
                data.setColumn1(cut1[0]);
                data.setColumn2("");
                if(cut1.length>1){
                    data.setColumn3(cut1[1].replace(",",""));
                }else{
                    data.setColumn3("{");
                }
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
            i=0;
        }
        return datas;
    }

    public void export(){

        String fileName = ExcelJsonApplicationTests.class.getResource("/").getPath()+"test.xlsx";

        ExcelWriter excelWriter = null;
        try {
            // 这里 需要指定写用哪个class去写
            excelWriter = EasyExcel.write(fileName, DataForJson.class).build();

            if(sheets.size() == 0){
                String sheetName = "sheet1";
                WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).build();
                excelWriter.write(sheetDatas.get(0), writeSheet);
            }// 資料夾有幾個就產生多少sheet
            else{
                for (int i = 0; i < sheets.size(); i++) {

                    WriteSheet writeSheet = EasyExcel.writerSheet(i, sheets.get(i)).head(DataForJson.class).build();
                    // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
                    excelWriter.write(sheetDatas.get(i), writeSheet);
                }
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

//                //sheet分頁
//                if(sheetCount > 0){
//                    sheets.add(file.getName());
//                }
//                sheetCount++;

                for (int i = 0; i < filelist.length; i++) {
                    File readfile = new File(filepath + "//" + filelist[i]);
                    if (!readfile.isDirectory()) {
                        //sheet分頁
                        if(!sheets.contains(file.getName())){
                            sheets.add(file.getName());
                        }
//                        sheetCount++;
//                        if(sheetCount > 0){
//                            sheets.add(file.getName());
//                        }
//                        sheetCount++;

                        System.out.println("讀檔:"+readfile.getName());
                        FileReader fr = new FileReader(readfile);
                        BufferedReader br = new BufferedReader(fr);
                        List<DataForJson> datas = this.parse(br);
                        this.datas.addAll(datas);

                    } else if (readfile.isDirectory()) {
                        //繼續找下一層
                        readfile(filepath + "//" + filelist[i]);
                    }
                }
                //sheet分頁
                sheetCount=sheets.size();

                //每讀完檔案下全資料換下一個
                sheetDatas.add(datas);
                this.datas = new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            System.out.println("readfile()   Exception:" + e.getMessage());
        }
    }
}
