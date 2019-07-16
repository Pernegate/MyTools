package Tools;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class readDoc {

    //preItemId很多地方有问题
    public static void main(String[] args) throws IOException {
        File tmpDocFile = new File("C:\\Users\\37645\\Desktop\\上报文件存档\\备份\\不良事件\\test");
        File[] docFiles = tmpDocFile.listFiles();
        int m1=0,m2=1,m3=0,m4=1;
        boolean psLock=false;
        String m1str,m2str,m3str,m4str;
        String itemId="",itemName="",preItemId="0",lastStr="0";
        List<String> preItemIdList=new ArrayList<>();
        int depth=-1;
        String isAuditInfo="N";

        for (File docFile : docFiles) {
            isAuditInfo="N";
            m1++;
            m1str=mToMstr(m1);
            m2str=mToMstr(m2);
            m3str=mToMstr(m3);
            m4str=mToMstr(m4);
            FileInputStream FIS = new FileInputStream(docFile);
            HWPFDocument doc = new HWPFDocument(FIS);
            String str = doc.getDocumentText();
            String[] strs=str.split("\r");
            for(int i=0;i<strs.length;i++){
                if("人员因素".equals(strs[i])){
                    isAuditInfo="Y";
                }
                if("Pss".equals(strs[i])){
                    preItemIdList.add(itemId);
                    depth++;
                    continue;
                }
                if("Pssend".equals(strs[i])){
                    preItemIdList.remove(depth);
                    depth--;
                    continue;
                }
                if("Ps".equals(strs[i])){
                    psLock=true;
                    lastStr=itemId;
                    continue;
                }
                if("Psend".equals(strs[i])){
                    psLock=false;
                    m4=1;
                    m4str=mToMstr(m4);
                    preItemId="0";
                    continue;
                }
                if("M2".equals(strs[i])){
                    m2++;
                    m2str=mToMstr(m2);
                    m3=0;
                    m3str=mToMstr(m3);
                    System.out.println();
                    System.out.println();
                    continue;
                }
                if (psLock){
                    m4++;
                    m4str=mToMstr(m4);
                    if(-1 >= depth){
                        preItemId=lastStr;
                    }else {
                        preItemId=preItemIdList.get(depth);
                    }

                }else {
                    m3++;
                    m3str=mToMstr(m3);
                }
                itemId=m1str+m2str+m3str+m4str;
                itemName=strs[i];
                sqlGen(itemId,itemName,preItemId,m1,isAuditInfo);
            }
        }
    }

    public static String mToMstr(int m){
        if(m>9){
            return String.valueOf(m);
        }else{
            return "0"+m;
        }
    }

    public static void sqlGen(String itemId,String itemName,String preItemId,int m1,String isAuditInfo){
        String temp="insert into RPT_ITEM_DICT\n" +
                "(ID, ITEM_ID, ITEM_NAME, PRE_ITEM_ID,DEFAULT_VALUE,IS_TITLE,FORM_TYPE,IS_AUDIT_INFO, IS_ENABLE, CREATE_DATE, CREATOR, CREATE_UNIT, UPDATE_DATE, UPDATER, UPDATE_UNIT , IS_DELETE )\n" +
                "select RPT_SEQ.NEXTVAL, '"+itemId+"', '"+itemName+
                "', '"+preItemId+"','','N', '"+m1+"','"+isAuditInfo+"','Y',SYSDATE, null, null, SYSDATE, null, null,'N'\n" +
                "from dual where not exists(select ITEM_ID from RPT_ITEM_DICT where ITEM_ID = '"+itemId+"');\n";
        System.out.println(temp);
    }

}
