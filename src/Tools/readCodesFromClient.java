package Tools;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hwpf.HWPFDocument;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class readCodesFromClient {
    public static void main(String[] args) throws IOException {
        String readFileName = "C:\\Users\\37645\\Desktop\\上报文件存档\\工具处理文件夹\\待处理txt文件";
        String outputFileName = "C:\\Users\\37645\\Desktop\\上报文件存档\\工具处理文件夹\\输出txt文件\\output.txt";
        readFromDocAndDeal(readFileName);
//        isContainChinese("Dragon和东方龙");

    }

    public static void readFromDocAndDeal(String fileName) throws IOException {
        int m1 = 2, m2 = 0, m3 = 1, m4 = 1;
        String m1Str = "", m2Str = "", m3Str = "", m4Str = "";
        File tmpTxtFile = new File(fileName);
        File[] docFiles = tmpTxtFile.listFiles();
        for (File docFile : docFiles) {
            String encoding = "GBK";
            List<String> preItemIdList = new ArrayList<>();
            String isAuditInfo = "N";

            preItemIdList.add("0");

            FileInputStream FIS = new FileInputStream(docFile);
            InputStreamReader ISR = new InputStreamReader(FIS, encoding);
            BufferedReader br = new BufferedReader(ISR);
            String readLineStr = br.readLine();
            StringBuilder sb = new StringBuilder();
            while (StringUtils.isNotEmpty(readLineStr)) {
                String chineseWords = isContainChinese(readLineStr);
                String itemId = "";

                if(readLineStr.contains("护士长原因分析")){
                    isAuditInfo="Y";
                }
                if (readLineStr.contains("<el-form-item")) {
                    m3++;
                    m4=1;
                }
                if (readLineStr.contains("</el-form-item")) {
                    m4 = 1;
                }
                if (readLineStr.contains("<p")) {
                    m2++;
                    m3 = 1;
                    m4 = 1;
                    if (1 < preItemIdList.size()) {
                        preItemIdList.remove(preItemIdList.size() - 1);
                    }

                }
                if (StringUtils.isEmpty(chineseWords)) {
                    readLineStr = br.readLine();
                    continue;
                } else {
                    itemId = getItemId(m1, m2, m3, m4);
                    sqlGen(itemId,chineseWords,preItemIdList.get(preItemIdList.size()-1),m1,isAuditInfo,"N");
                    m4++;
                }
                if(readLineStr.contains("</p")){
                    preItemIdList.add(itemId);
                }
//                sb.append(readLineStr + "\n");
                readLineStr = br.readLine();
            }
//            String outputStr = sb.toString();
//            System.out.println(outputStr);
        }
    }

    public static String getItemId(int m1, int m2, int m3, int m4) {
        return readDoc.mToMstr(m1) + readDoc.mToMstr(m2) + readDoc.mToMstr(m3) + readDoc.mToMstr(m4);
    }

    public static String isContainChinese(String str) {
        String matchStr = "";
        String regEx = "[\\u4e00-\\u9fa5]+";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
//        System.out.println(str);
//        System.out.println(m.find());
        if (m.find()) {
            matchStr = m.group();
        }
//        System.out.println(m.group());
//        System.out.println(m.start() + "+" + m.end());
        return matchStr;
    }

    public static void sqlGen(String itemId,String itemName,String preItemId,int m1,String isAuditInfo,String isTitle){
        String temp="insert into RPT_ITEM_DICT\n" +
                "(ID, ITEM_ID, ITEM_NAME, PRE_ITEM_ID,DEFAULT_VALUE,IS_TITLE,FORM_TYPE,IS_AUDIT_INFO, IS_ENABLE, CREATE_DATE, CREATOR, CREATE_UNIT, UPDATE_DATE, UPDATER, UPDATE_UNIT , IS_DELETE )\n" +
                "select RPT_SEQ.NEXTVAL, '"+itemId+"', '"+itemName+
                "', '"+preItemId+"','','"+isTitle+"', '"+m1+"','"+isAuditInfo+"','Y',SYSDATE, null, null, SYSDATE, null, null,'N'\n" +
                "from dual where not exists(select ITEM_ID from RPT_ITEM_DICT where ITEM_ID = '"+itemId+"');\n";
        System.out.println(temp);
    }
}
