package Tools;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneratorJavaByReplace {

    public static void main(String[] args) throws IOException,
            InvalidFormatException,InterruptedException
    {
//        loopPrint(1,22);
//        File xlsFile = new File("C:/Users/37645/Desktop/新项目/护士证书维护表.xlsx");
        File tmpxlsFile = new File("C:\\Users\\37645\\Desktop\\新项目\\建表excel");
        File tmpCopyFile = new File("C:/Users/37645/Desktop/新项目/待复制文件");
        File[] xlsFiles = tmpxlsFile.listFiles();
        File[] beCopyFiles = tmpCopyFile.listFiles();
        for(File beCopyFile : beCopyFiles) {
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
                    String noPerTabName = removePer(tmpTabName,3);
                    String firstUpperTabName = "Per" + noPerTabName;
                    String lowTabName = tabName.toLowerCase();
                    String name = beCopyFile.getName();
                    String temp=copyFile(beCopyFile);
//                    temp=temp.replaceAll("PerDegree","Qual"+noPerTabName);
                    temp=temp.replaceAll("Degree",noPerTabName);
                    temp=temp.replaceAll("degree_info_id","id");
                    temp=temp.replaceAll("degreeInfoId","id");
                    name=name.replaceAll("Degree",noPerTabName);
                    name="C:/Users/37645/Desktop/新项目/通过替换输出文件/"+name;
                    createFile(name,temp);
                    Thread.sleep(1*300);
                }
            }
        }
    }


    public  static  String copyFile(File file){
        try {
            if(!file.exists()||file.isDirectory())
                throw new FileNotFoundException();
            BufferedReader br=new BufferedReader(new FileReader(file));
            String temp=null;
            StringBuffer sb=new StringBuffer();
            temp=br.readLine();
            while(temp!=null){
                sb.append(temp+"\n");
                temp=br.readLine();
            }
            return sb.toString();

        }
        catch (Exception e){
            System.out.println("复制文件出错："+e);
            return "";
        }
    }

    public  static  void createFile(String name,String temp){
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
        }catch (Exception e){
            System.out.println("生成文件出错："+e);
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

    public static String removePer(String param,int num) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        String tmp=param.substring(num);
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
