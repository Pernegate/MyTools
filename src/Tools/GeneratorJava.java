package Tools;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneratorJava {

    public static void main(String[] args) throws IOException,
            InvalidFormatException {
//        loopPrint(1,22);
//        File xlsFile = new File("C:/Users/37645/Desktop/新项目/护士证书维护表.xlsx");
        File tmpxlsFile = new File("C:\\Users\\37645\\Desktop\\新项目\\建表excel");
        File[] xlsFiles = tmpxlsFile.listFiles();
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
                String pkName = sheet.getRow(2).getCell(0).getStringCellValue().toLowerCase();
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

                //Dao接口
                String tmpTabName = underlineToCamel(tabName.toLowerCase());
                String noPerTabName = removePer(tmpTabName);
                String firstUpperTabName = "Per" + noPerTabName;
                String lowTabName = tabName.toLowerCase();
                String firstLowerTabName = underlineToCamel(removePer_(tabName).toLowerCase());
                String name = "";
                String temp = createIDao(firstUpperTabName, noPerTabName, pkName, tmpTabName);
//                name = "C:/Users/37645/Desktop/新项目/输出代码/" + "I" + noPerTabName + "Dao.java";
//                createFile(name, temp);
//                System.out.println(temp);
//                System.out.println();
//
//                //Dao
//                temp = createDao(noPerTabName, firstUpperTabName, pkName, tmpTabName);
//                name = "C:/Users/37645/Desktop/新项目/输出代码/" + noPerTabName + "Dao.java";
//                createFile(name, temp);
//                System.out.println(temp);
                System.out.println();

                //mapper.xml------------------------------------------------------------------------------------------------------------------
                String item="item.";
                temp = "";
                temp = createSelectSQl(noPerTabName, firstUpperTabName, lowTabName, rows, sheet);

                //foreach insert
                temp +=
                        "    <insert id=\"add" + noPerTabName + "List\" " + " parameterType=\"java.util.List\">\n" +
                                "        begin\n" +
                                "        <foreach collection=\"list\" item=\"item\" >\n";
                temp += mergeSql(lowTabName, pkName,item);
                temp += "           update \n";
                temp += updateSql(rows, sheet, lowTabName,item);
                temp += "        WHEN NOT MATCHED THEN\n";
                temp += "        insert \n";
                temp += insertSql(rows, sheet, lowTabName,item);
                temp +="        </foreach>\n" +
                        "        end ;\n"+
                        "    </insert>\n" +
                                "\n";


                //insert
                temp +=
                        "    <insert id=\"add" + noPerTabName + "\" " + " parameterType=\"com.kyee.nqm.personalFile.domain.Per" + noPerTabName + "\">\n";
                temp += mergeSql(lowTabName, pkName,"");
                temp += "           update \n";
                temp += updateSql(rows, sheet, lowTabName,"");
                temp += "        WHEN NOT MATCHED THEN\n";
                temp += "        insert \n";
                temp += insertSql(rows, sheet, lowTabName,"");
                temp +=
                        "    </insert>\n" +
                                "\n";

                //delete
                temp +=
                        "    <update id=\"del" + noPerTabName + "\">\n" +
                                "        update " + lowTabName + " \n" +
                                "        set is_delete='Y'\n" +
                                "        where " + pkName + "=#{" + pkName + "}\n" +
                                "    </update>\n"
                                + "\n";

                //update
                temp += "    <update id=\"update" + noPerTabName + "\" " + " parameterType=\"com.kyee.nqm.personalFile.domain.Per" + noPerTabName + "\">\n" +
                        "           update " + lowTabName + "\n";
                temp += updateSql(rows, sheet, lowTabName,"");
                temp +=
                        "       where " + pkName + "=" + "#{" + underlineToCamel(pkName) + "} and is_delete != 'Y'\n" +
                                "    </update>\n" +
                                "</mapper>";
                System.out.println(temp);
                name = "C:\\Users\\37645\\Desktop\\新项目\\输出代码\\" + firstLowerTabName + ".xml";
                createFile(name, temp);
            }
        }
    }

    public static String createIDao(String firstUpperTabName, String noPerTabName, String pkName, String tmpTabName) {
        String temp = temp = "package com.kyee.nqm.personalFile.dao;\n" +
                "\n" +
                "import com.kyee.nqm.personalFile.domain." + firstUpperTabName + ";\n" +
                "\n" +
                "import java.util.List;\n" +
                "\n" +
                "public interface I" + noPerTabName + "Dao {\n";
        temp += "   List<" + firstUpperTabName + "> get" + noPerTabName + "(String " + pkName + ");\n" +
                "   int add" + noPerTabName + "();\n" +
                "   int del" + noPerTabName + "(String " + pkName + ");\n" +
                "   int update" + noPerTabName + "(" + firstUpperTabName + " " + tmpTabName + ");";
        temp += "\n}";
        return temp;
    }

    public static String createDao(String noPerTabName, String firstUpperTabName, String pkName, String tmpTabName) {
        String temp = "package com.kyee.nqm.personalFile.dao.impl;\n" +
                "\n" +
                "import com.kyee.nqm.personalFile.dao.I" + noPerTabName + "Dao;\n" +
                "import com.kyee.nqm.personalFile.domain." + firstUpperTabName + ";\n" +
                "import org.mybatis.spring.SqlSessionTemplate;\n" +
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.stereotype.Repository;\n" +
                "\n" +
                "import java.util.HashMap;\n" +
                "import java.util.List;\n" +
                "import java.util.Map;";
        temp += "@Repository\n" +
                "public class " + noPerTabName + "Dao implements I" + noPerTabName + "Dao {\n" +
                "\n" +
                "    @Autowired\n" +
                "    private SqlSessionTemplate sqlSessionTemplate;\n" +
                "    @Override\n" +
                "    public List<" + firstUpperTabName + "> get" + noPerTabName + "(String " + pkName + "){\n" +
                "        Map param= new HashMap<>();\n" +
                "        param.put(\"" + pkName + "\"," + pkName + ");\n" +
                "        return sqlSessionTemplate.selectList(\"get" + noPerTabName + "\",param);\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public int add" + noPerTabName + "(){\n" +
                "        return sqlSessionTemplate.insert(\"add" + noPerTabName + "\");\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public int del" + noPerTabName + "( String " + pkName + "){\n" +
                "        Map param= new HashMap<>();\n" +
                "         param.put(\"" + pkName + "\"," + pkName + ");\n" +
                "        return sqlSessionTemplate.update(\"del" + noPerTabName + "\",param);\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    @Override\n" +
                "    public int update" + noPerTabName + "( " + firstUpperTabName + " " + tmpTabName + "){\n" +
                "        return sqlSessionTemplate.update(\"update" + noPerTabName + "\"," + tmpTabName + ");\n" +
                "    }\n" +
                "\n" +
                "}";
        return temp;
    }

    public static String updateSql(int rows, Sheet sheet, String lowTabName,String item) {
        String temp = "";
        temp =
                "           <set>\n";
        for (int row = 4; row < rows - 1; row++) {
            Row r = sheet.getRow(row);
            String param = r.getCell(0).getStringCellValue();
            String type = r.getCell(2).getStringCellValue();
            type = changeType(type);
            String tmpParam = underlineToCamel(param);
            String dateParm = "";
            if(param.toLowerCase().contains("creat")){
                continue;
            }
            if("create_date".equals(param.toLowerCase()) || "update_date".equals(param.toLowerCase()))
                type="TIMESTAMP";
            dateParm = "#{" + item + tmpParam + ",jdbcType=" + type + "}";
            temp += "                <if test=\"" + item + tmpParam + " != null" + " \">\n";
                temp += "                     " + param + "=" + dateParm + ",\n";
            temp += "                </if>\n";
        }
        temp +=
                "           </set>\n";
        return temp;
    }

    public static String mergeSql(String lowTabName, String pkName,String item) {
        String temp = "";
        temp = "        MERGE INTO " + lowTabName + " a\n" +
                "        USING (SELECT #{" + item + underlineToCamel(pkName) + "} as " + pkName + " FROM dual) b\n" +
                "        ON (a." + pkName + " = b." + pkName + ")\n" +
                "        WHEN MATCHED THEN\n";
        return temp;
    }

    public static String insertSql(int rows, Sheet sheet, String lowTabName,String item) {
        String temp = "";
        temp =
                "        (";
        for (int row = 2; row < rows; row++) {
            Row r = sheet.getRow(row);
            String param = r.getCell(0).getStringCellValue();
            if (row == rows - 1) {
                temp += param + ")";
            }
            else {
                temp += param + ",";
            }
            if ((row - 1) % 6 == 0 && row != rows - 1)
                temp += "\n" + "        ";
        }
        temp += "\n";
        temp +=
                "        values \n" +
                        "       (";
        for (int row = 2; row < rows; row++) {
            Row r = sheet.getRow(row);
            String param = r.getCell(0).getStringCellValue();
            String tmpParam = underlineToCamel(param);
            String type = r.getCell(2).getStringCellValue();
            type = changeType(type);
            if("create_date".equals(param.toLowerCase()) || "update_date".equals(param.toLowerCase()))
                type="TIMESTAMP";
            tmpParam = "#{" + item + tmpParam + ",jdbcType=" + type + "}";
            if("is_delete".equals(param))
                tmpParam="'N'";
            if (row == rows - 1)
                temp += tmpParam + ")";
            else
                temp += tmpParam + ",";
            if ((row - 1) % 6 == 0 && row != rows - 1)
                temp += "\n" + "        ";
        }
        temp += ";"+"\n";
        return temp;
    }

    public static String createSelectSQl(String noPerTabName, String firstUpperTabName, String lowTabName, int rows, Sheet sheet) {
        String temp = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n" +
                "<mapper namespace=\"com.kyee.nqm.personalFile.dao.impl." + noPerTabName + "Dao\">\n" +
                "\n" +
                "    <resultMap id=\"SelectResultMap\" type=\"com.kyee.nqm.personalFile.domain."+firstUpperTabName+"\" >\n" +
                "        <result column=\"create_date\" property=\"createDate\" jdbcType=\"TIMESTAMP\" />\n" +
                "        <result column=\"update_date\" property=\"updateDate\" jdbcType=\"TIMESTAMP\" />\n" +
                "    </resultMap>\n"+
                "\n"+
                "    <select id=\"get" + noPerTabName + "\" resultMap=\"SelectResultMap\">\n" +
                "        SELECT\n" +
                "        ";
        for (int row = 2; row < rows - 1; row++) {
            Row r = sheet.getRow(row);
            String param = r.getCell(0).getStringCellValue();
            if (row == rows - 2)
                temp += param;
            else
                temp += param + ",";
            if ((row - 1) % 6 == 0 && row != rows - 3)
                temp += "\n" + "        ";
        }
        temp += "\n" +
                "        FROM\n" +
                "            " + lowTabName + "\n" +
                "        WHERE user_code =#{user_code,jdbcType = VARCHAR} and is_delete != 'Y'\n" +
                "    </select>\n" +
                "\n";
        return temp;
    }

    public static void createFile(String name, String temp) {
        try {
            String fileName = name;
            File file = new File(fileName);
            if (!file.exists())
                file.createNewFile();
            else
                file.delete();
            FileOutputStream out = new FileOutputStream(file, true);
            out.write(temp.getBytes("utf-8"));
            out.close();
        } catch (Exception e) {
            System.out.println("生成文件出错：" + e);
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

    public static String removePer(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        String tmp = param.substring(3);
        return tmp;
    }

    public static String changeType(String type) {
        List<String> tmp = new ArrayList<>();
        tmp.add("NUMBER.*");
        String test = tmp.get(0);
        if (type.equals("DATE") || type.equals("TIMESTAMP")) {
            return type;
        } else if (Pattern.matches(tmp.get(0), type)) {
            if (Pattern.matches(".*,.*", type))
                return "NUMERIC";
            return "NUMERIC";
        } else {
            return "VARCHAR";
        }
    }

    public static String removePer_(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        String tmp = param.substring(4);
        return tmp;
    }
}
