package Tools;

import com.sun.deploy.util.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.util.StringUtil;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelToSQL {

    public static void main(String[] args) throws IOException,
            InvalidFormatException {
        String test="adsa哈哈";
        System.out.println(test.getBytes("GBK").length);
//        loopPrint(1,22);
//        File xlsFile = new File("C:/Users/37645/Desktop/新项目/护士证书维护表.xlsx");
        File tmpxlsFile = new File("C:\\Users\\37645\\Desktop\\新项目\\建表excel");
        //注意修改row、改xml文件名、改timestamp、改order by、merge中update去掉checkdate、foreach、
        // transfer要注意接收字段、注意select的排序、addlist中分号可能在一个很奇怪的地方
//        File tmpxlsFile = new File("C:/Users/37645/Desktop/新项目/质量检查/建表excel");

        File[] xlsFiles = tmpxlsFile.listFiles();
        //更新代码
//        for (File xlsFile : xlsFiles) {
//            // 获得工作簿
//            Workbook workbook = WorkbookFactory.create(xlsFile);
//            // 获得工作表个数
//            int sheetCount = workbook.getNumberOfSheets();
//            // 遍历工作表
//
//            for (int i = 0; i < sheetCount; i++) {
//                Sheet sheet = workbook.getSheetAt(i);
//                String tabName = sheet.getRow(0).getCell(1).getStringCellValue();
//                String tabCNName = sheet.getRow(0).getCell(0).getStringCellValue();
//                String pkName = sheet.getRow(2).getCell(0).getStringCellValue();
//                // 获得行数
//                int rows = sheet.getLastRowNum() + 1;
//                // 获得列数，先获得一行，在得到改行列数
//                Row tmp = sheet.getRow(0);
//                if (tmp == null) {
//                    continue;
//                }
//                int cols = tmp.getPhysicalNumberOfCells();
//                // 读取数据
//                String param;
//                String comment;
//                String type;
//                String tmpTabName = underlineToCamel(tabName.toLowerCase());
//                String noPerTabName = removePer(tmpTabName);
//                String firstUpperTabName = "Per" + noPerTabName;
//                String firstLowerTabName = underlineToCamel(removePer_(tabName).toLowerCase());
//                String lowTabName = tabName.toLowerCase();
//                String temp="@Autowired\n" +
//                        "    private I"+noPerTabName+"Dao "+firstLowerTabName+"Dao;";
//                System.out.println(temp);
//            }
//        }
//        System.out.println();
//        for (File xlsFile : xlsFiles) {
//            // 获得工作簿
//            Workbook workbook = WorkbookFactory.create(xlsFile);
//            // 获得工作表个数
//            int sheetCount = workbook.getNumberOfSheets();
//            // 遍历工作表
//
//
//
//            for (int i = 0; i < sheetCount; i++) {
//                Sheet sheet = workbook.getSheetAt(i);
//                String tabName = sheet.getRow(0).getCell(1).getStringCellValue();
//                String tabCNName = sheet.getRow(0).getCell(0).getStringCellValue();
//                String pkName = sheet.getRow(2).getCell(0).getStringCellValue();
//                // 获得行数
//                int rows = sheet.getLastRowNum() + 1;
//                // 获得列数，先获得一行，在得到改行列数
//                Row tmp = sheet.getRow(0);
//                if (tmp == null) {
//                    continue;
//                }
//                int cols = tmp.getPhysicalNumberOfCells();
//                // 读取数据
//                String param;
//                String comment;
//                String type;
//                String tmpTabName = underlineToCamel(tabName.toLowerCase());
//                String noPerTabName = removePer(tmpTabName);
//                String firstUpperTabName = "Per" + noPerTabName;
//                String firstLowerTabName = underlineToCamel(removePer_(tabName).toLowerCase());
//                String lowTabName = tabName.toLowerCase();
//                String temp = "List<"+firstUpperTabName+"> per"+noPerTabName+"List=personInfoDto.getPer"+noPerTabName+"s();\n" +
//                        "        for("+firstUpperTabName+" e : per"+noPerTabName+"List ){\n" +
//                        "            e.setCreateDate(createDate);\n" +
//                        "            e.setCreator(creator);\n" +
//                        "            e.setCreateUnit(createUnit);\n"+
//                        "            "+firstLowerTabName+"Dao.add"+noPerTabName+"(e);\n" +
//                        "        }\n";
//                System.out.println(temp);
//            }
//        }


        for (File xlsFile : xlsFiles) {
            // 获得工作簿
            Workbook workbook = WorkbookFactory.create(xlsFile);
            // 获得工作表个数
            int sheetCount = workbook.getNumberOfSheets();
            // 遍历工作表


            for (int i = 0; i < sheetCount; i++) {
                try{
                    Sheet sheet = workbook.getSheetAt(i);
                    String tabName = sheet.getRow(0).getCell(1).getStringCellValue();
                    String tabCNName = sheet.getRow(0).getCell(0).getStringCellValue();
                    String pkName = sheet.getRow(2).getCell(0).getStringCellValue();
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
                    System.out.println("drop table " + tabName + " cascade constraints;");
                }catch (Exception e){
                    System.out.println(xlsFile.getName());
                }
            }
        }

        for (File xlsFile : xlsFiles) {
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
                System.out.println("-----------------------------------" + tabCNName + "：" + tabName + "-----------------------------------------------");
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
//                    System.out.print(param + ",");
//                }
//                System.out.println();
//                System.out.println();
//                for (int row = 2; row < rows; row++) {
//                    Row r = sheet.getRow(row);
//                    param = r.getCell(0).getStringCellValue();
//                    System.out.print("#{" + param + "}" + ",");
//                }
//                System.out.println();
//                System.out.println();
//                String tmpName = underlineToCamel(tabName.toLowerCase());
//                for (int row = 2; row < rows; row++) {
//                    Row r = sheet.getRow(row);
//                    param = r.getCell(0).getStringCellValue();
//                    String tmpParam = underlineToCamel(param);
//                    System.out.println("<if test=\"" + tmpParam + " != null\"" + ">");
//                    System.out.println(param + "=#{" + tmpParam + "},");
//                    System.out.println("</if>");
//                }
//                System.out.println();

                //create table

                System.out.println("create table " + tabName + " (");
                for (int row = 2; row < rows; row++) {
                    Row r = sheet.getRow(row);
//                if(r.getCell(0) == null)
//                    break;
                    param = r.getCell(0).getStringCellValue();
                    comment = r.getCell(1).getStringCellValue();
                    type = r.getCell(2).getStringCellValue();
//                    String defaultVar=r.getCell(3).getStringCellValue();
                    String isNull="";
                    if(null != r.getCell(4)){
                        isNull=r.getCell(4).getStringCellValue();
                    }

                    String tempStr;
//                    if (!org.apache.commons.lang3.StringUtils.isEmpty(defaultVar)){
//                        tempStr=param + "    " + type + " default ('"+defaultVar+"')";
//                    }

                    if("is_delete".equals(param)){
                        tempStr=param + "    " + type + " default ('N')";
                    }
                    else {
                        if("create_date".equals(param) || "update_date".equals(param))
                            tempStr=param + "    " + type + " default SYSDATE";
                        else
                            tempStr=param + "    " + type ;
                    }
                    if(!org.apache.commons.lang3.StringUtils.isEmpty(isNull)){
                        tempStr+="    "+"not null";
                    }
                    tempStr+=",";
                    System.out.println(tempStr);
                }
                System.out.println(tabSpaceSql(tabName, pkName));
                System.out.println("comment on table  " + tabName + "  is  '" + tabCNName + "';");
                for (int row = 2; row < rows; row++) {
                    Row r = sheet.getRow(row);
                    param = r.getCell(0).getStringCellValue();
                    comment = r.getCell(1).getStringCellValue();
                    type = r.getCell(2).getStringCellValue();
                    System.out.println("comment on column " + tabName + "." + param + " is '" + comment + "';");
                }
            }
        }
    }

    public static String tabSpaceSql(String tabName, String pkName) {
        String temp = " constraint PK_" + tabName + " primary key (" + pkName + ")\n" +
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
        return temp;
    }

    public static void loopPrint(int start, int end) {
        for (int i = start; i <= end; i++)
            System.out.print(i + ",");
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
        String tmp = param.substring(3);
        return tmp;
    }

    public static String removePer_(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        String tmp = param.substring(4);
        return tmp;
    }
}
