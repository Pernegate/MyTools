package Tools;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InitDomain {

    public static void main(String[] args) throws IOException,
            InvalidFormatException
    {
//        loopPrint(1,22);
//        File xlsFile = new File("C:/Users/37645/Desktop/新项目/建表excel_1119/人员调配表.xlsx");
        File tmpxlsFile = new File("C:/Users/37645/Desktop/新项目/建表excel");
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
                String tmpTabName=underlineToCamel(tabName.toLowerCase());
                String noPerTabName=removePer(tmpTabName);
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
                // 读取数据
                String param;
                String comment;
                String type;

                //sql
//                for (int row = 2; row < rows; row++) {
//                    Row r = sheet.getRow(row);
//                    param = r.getCell(0).getStringCellValue();
//                    String tmpParam = underlineToCamel(param);
//                    tmpParam=upperFirst(tmpParam);
//                    type = r.getCell(2).getStringCellValue();
//                    if (!type.equals("DATE"))
//                        System.out.println(tmpTabName+".set"+tmpParam+"(\""+row+"\");");
//                    else
//                        System.out.println(tmpTabName+".set"+tmpParam+"(now);");
//                }
//                System.out.println();
//                for (int row = 2; row < rows; row++) {
//                    Row r = sheet.getRow(row);
//                    param = r.getCell(0).getStringCellValue();
//                    String tmpParam = underlineToCamel(param);
//                    tmpParam=upperFirst(tmpParam);
//                    type = r.getCell(2).getStringCellValue();
//                    if (!type.equals("DATE"))
//                        System.out.print(row+",");
//                    else
//                        System.out.print("to_date('2018/11/14','yyyy/mm/dd'),");
//                }

                //postman test
                for (int row = 2; row < rows; row++) {
                    Row r = sheet.getRow(row);
                    param = r.getCell(0).getStringCellValue();
                    String tmpParam = underlineToCamel(param);
                    tmpParam=upperFirst(tmpParam);
                    type = r.getCell(2).getStringCellValue();
                    if (!type.equals("DATE"))
                        System.out.println(underlineToCamel("\""+param.toLowerCase())+"\""+":"+"\""+row+"\",");
                    else
                        System.out.println(underlineToCamel("\""+param.toLowerCase())+"\""+":\"2018/11/20 13:27\",");
                }
                System.out.println();
//                System.out.println(underlineToCamel(removePer(tabName)).toUpperCase()+"(\""+underlineToCamel(removePer(tabName).toLowerCase())+"\",\"\"),");
            }
        }
    }

    public  static  String tabSpaceSql(String tabName,String pkName){
        String temp=" constraint PK_" + tabName + " primary key (" + pkName + ")\n" +
                "     using index \n" +
                "     tablespace TSP_KYEENQM\n" +
                "     pctfree 10\n" +
                "     initrans 2\n" +
                "     maxtrans 255\n" +
                "     storage\n" +
                "     (\n" +
                "       initial 64K\n" +
                "       next 1M\n" +
                "       minextents 1\n" +
                "       maxextents unlimited\n" +
                "     )\n" +
                ")\n" +
                "tablespace TSP_KYEENQM\n" +
                "  pctfree 10\n" +
                "  initrans 1\n" +
                "  maxtrans 255\n" +
                "  storage\n" +
                "  (\n" +
                "    initial 64K\n" +
                "    next 1M\n" +
                "    minextents 1\n" +
                "    maxextents unlimited\n" +
                "  );\n";
        return  temp;
    }

    public static void loopPrint(int start,int end){
        for(int i=start;i<= end;i++)
            System.out.print(i+",");
        System.out.println();
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

    public static String removePer(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        String tmp=param.substring(4);
        return tmp;
    }

    public  static  String upperFirst(String temp){
        temp=temp.toUpperCase().substring(0,1)+temp.substring(1);
        return  temp;
    }
}
