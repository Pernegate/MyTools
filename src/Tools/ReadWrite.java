package Tools;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ReadWrite
{

    //dto、时间不为空
    public static final char UNDERLINE = '_';

    public static void main(String[] args) throws IOException,
            InvalidFormatException
    {
//        File xlsFile = new File("C:/Users/37645/Desktop/新项目/护士证书维护表.xlsx");
        File tmpxlsFile = new File("C:\\Users\\37645\\Desktop\\新项目\\建表excel");
        File[] xlsFiles = tmpxlsFile.listFiles();
        for(File xlsFile : xlsFiles ) {
            // 获得工作簿
            Workbook workbook = WorkbookFactory.create(xlsFile);
            // 获得工作表个数
            int sheetCount = workbook.getNumberOfSheets();
            // 遍历工作表
            for (int i = 0; i < sheetCount; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                String tabName = sheet.getRow(0).getCell(1).getStringCellValue();
                String tabCNName = sheet.getRow(0).getCell(0).getStringCellValue();
                String pkName = sheet.getRow(2).getCell(0).getStringCellValue();
                System.out.println();
                System.out.println("-----------------------------------"+tabCNName+"："+tabName+"-----------------------------------------------");
                System.out.println();
                // 获得行数
                int rows = sheet.getLastRowNum() + 1;
                // 获得列数，先获得一行，在得到改行列数
                Row tmp = sheet.getRow(0);
                if (tmp == null) {
                    continue;
                }
                int cols = tmp.getPhysicalNumberOfCells();
                System.out.println(cols + "/" + rows);
                // 读取数据
                String param;
                String comment;
                String type;
                //生成java domain
                for (int row = 2; row < rows; row++) {
                    Row r = sheet.getRow(row);
                    param = r.getCell(0).getStringCellValue();
                    comment = r.getCell(1).getStringCellValue();
                    type = r.getCell(2).getStringCellValue();
                    param = underlineToCamel(param);
                    type = changeType(type);
                    System.out.println("/**");
                    System.out.println(" * " + comment);
                    System.out.println(" **/");
                    System.out.println("private " + type + " " + param + ";");
                }
            }
        }
    }

    public static String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        StringBuilder sb = new StringBuilder(param);
        Matcher mc = Pattern.compile("_").matcher(param);
        int i = 0;
        while (mc.find()) {
            int position = mc.end() - (i++);
            sb.replace(position - 1, position + 1, sb.substring(position, position + 1).toUpperCase());
        }
        return sb.toString();
    }

    public static String changeType(String type) {
        List<String> tmp=new ArrayList<>();
        tmp.add("NUMBER.*");
        String test=tmp.get(0);
        if(type.equals("DATE")){
            return "Date";
        }
        else if(Pattern.matches(tmp.get(0),type)){
            if(Pattern.matches(".*,.*",type))
                return  "Float";
            return  "Integer";
        }
        else{
            return "String";
        }
    }

}