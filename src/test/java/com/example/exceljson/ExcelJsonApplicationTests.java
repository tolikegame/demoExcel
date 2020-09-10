package com.example.exceljson;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ExcelJsonApplicationTests {

    private static String json = "export default{\n" +
            "    verifyCode:{\n" +
            "        needCheck:'請先校驗驗證碼', //請先校驗驗證碼\n" +
            "        wordTip:'請先選擇文字', //請先選擇文字\n" +
            "    },\n" +
            "}";

    @Test
    void main() throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(json.getBytes(Charset.forName("utf8")))));
        String line;
        List<Data> datas = new ArrayList<>();
        while((line=br.readLine()) != null){
            if(line.contains(":\'")){
                Data data = new Data();
                String[] cut1 = line.split(":'");
                data.setColumn1(cut1[0].trim());
                data.setColumn2(":\'");
                String[] cut2 = line.split("',");
                data.setColumn3(cut2[0].trim());
                data.setColumn4("\',");
                data.setColumn5(cut2[1]);
                datas.add(data);
            }else if(line.contains(":")){
                Data data = new Data();
                String[] cut1 = line.split(":");
                data.setColumn1(cut1[0].trim());
                data.setColumn2("");
                data.setColumn3(cut1[1]);
                data.setColumn4("");
                data.setColumn5("");
                datas.add(data);
            }else {
                Data data = new Data();
                data.setColumn1(line);
                data.setColumn2("");
                data.setColumn3("");
                data.setColumn4("");
                data.setColumn5("");
                datas.add(data);
            }
        }
        String fileName = ExcelJsonApplicationTests.class.getResource("/").getPath()+"text.xlsx";
        String sheetName = "sheet1";
        EasyExcel.write(fileName, Data.class).sheet(sheetName).doWrite(datas);
    }


}
