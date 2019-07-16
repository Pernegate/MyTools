package Tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dto.RPTTreeUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Enum.FormTypeEnum;

public class getRPTInsertFromJson {

    public static void main(String[] args) throws IOException {
//        String readFileName = "C:\\Users\\37645\\Desktop\\上报文件存档\\demo\\Json";
        String readFileName = "C:\\Users\\37645\\Desktop\\上报文件存档\\demo\\Json\\ReportUnplannedExtubation_sava.json";
        String outputFileName = "C:\\Users\\37645\\Desktop\\上报文件存档\\demo\\out\\ReportUnplannedExtubation_sava.txt";
        readFromDocAndDeal(readFileName, outputFileName);

    }

    public static void readFromDocAndDeal(String fileName, String outputFileName) throws IOException {
        int m1 = 5, m2 = 0, m3 = 0, m4 = 0;
        StringBuilder sqlSb = new StringBuilder();

        File file = new File(fileName);
        String jsonString = FileUtils.readFileToString(file);
        JSONArray jsonArray = JSONArray.parseArray(jsonString);
        RPTTreeUtil rptTreeUtil = new RPTTreeUtil(
                jsonArray.getJSONObject(0).getString("id"),
                jsonArray.getJSONObject(0).getString("name"),
                jsonArray.getJSONObject(0).getString("pId"),
                0
        );
        rptTreeUtil.setChildrenList(getRPTTree(jsonArray.getJSONObject(0).getString("children"), 1));

        System.out.println(rptTreeUtil);

        rptTreeUtil.setId(getItemId(m1, m2, m3, m4));
        rptTreeUtil.setpId("0");
        rptTreeUtil.setName(FormTypeEnum.getDescriptionByCode(String.valueOf(m1)));
        sqlSb.append(sqlGen(getItemId(m1, m2, m3, m4), FormTypeEnum.getDescriptionByCode(String.valueOf(m1)),
                "0", m1, "N", "Y") + "\n");
        getDictSqlByTree(rptTreeUtil, sqlSb,"N", m1, m2 + 1, m3 + 1, m4 + 1);


        GeneratorJava.createFile(outputFileName, sqlSb.toString());
    }

    public static int getDictSqlByTree(RPTTreeUtil rptTreeUtil, StringBuilder sqlSb, String isAuditInfo,
                                        int m1, int m2, int m3, int m4) {


        int level = rptTreeUtil.getLevel();
        if ("护士长原因分析".equals(rptTreeUtil.getName())) {
            isAuditInfo = "Y";
        }

        if (null != rptTreeUtil.getChildrenList() && 0 < rptTreeUtil.getChildrenList().size()) {
            for (int i = 0; i < rptTreeUtil.getChildrenList().size(); i++) {

                String isTitle = "N";
                String itemId = getItemId(m1, m2, m3, m4);
                String itemName = rptTreeUtil.getChildrenList().get(i).getName();

                rptTreeUtil.getChildrenList().get(i).setId(itemId);
                rptTreeUtil.getChildrenList().get(i).setpId(rptTreeUtil.getId());
                sqlSb.append(sqlGen(itemId, itemName, rptTreeUtil.getId(), m1, isAuditInfo, isTitle) + "\n");

                //生成userCode和deptCode的sql
                if("签名时间".equals(itemName)){
                    for(int j =0 ;j<2;j++){
                        m4++;
                        itemId = getItemId(m1, m2, m3, m4);
                        if(0 == j){
                            sqlSb.append(sqlGen(itemId, "护士长签名user_code", rptTreeUtil.getId(), m1, "YS", isTitle) + "\n");
                        }else {
                            sqlSb.append(sqlGen(itemId, "科室dept_code", rptTreeUtil.getId(), m1, "DC", isTitle) + "\n");
                        }
                    }
                }

                switch (level) {
                    case 0:
                        getDictSqlByTree(rptTreeUtil.getChildrenList().get(i), sqlSb,isAuditInfo,
                                m1, m2, m3 + 1, m4);
                        m2++;
                        break;
                    case 1:
                        getDictSqlByTree(rptTreeUtil.getChildrenList().get(i), sqlSb,isAuditInfo,
                                m1, m2, m3, m4 + 1);
                        m3++;
                        break;
                    case 2:
                        m4=getDictSqlByTree(rptTreeUtil.getChildrenList().get(i), sqlSb,isAuditInfo,
                                m1, m2, m3, m4 + 1);
                        m4++;
                        break;
                    default:
                        m4=getDictSqlByTree(rptTreeUtil.getChildrenList().get(i), sqlSb,isAuditInfo,
                                m1, m2, m3, m4 + 1);
                        m4++;
                        break;
                }
            }
        }
        m4--;
        return m4;
    }

    public static List<RPTTreeUtil> getRPTTree(String jsonString, int level) {
        List<RPTTreeUtil> rptTreeUtilList = new ArrayList<>();
        JSONArray jsonArray = JSONArray.parseArray(jsonString);

        for (int i = 0; i < jsonArray.size(); i++) {
            RPTTreeUtil rptTreeUtil = new RPTTreeUtil(
                    jsonArray.getJSONObject(i).getString("id"),
                    jsonArray.getJSONObject(i).getString("name"),
                    jsonArray.getJSONObject(i).getString("pId"),
                    level
            );
            try {
                String children = jsonArray.getJSONObject(i).getString("children");
                if (StringUtils.isNotEmpty(children)) {
                    rptTreeUtil.setChildrenList(getRPTTree(children, level + 1));
                }
            } catch (Exception e) {
                System.out.println("叶子节点!!!!!!!");
            }
            rptTreeUtilList.add(rptTreeUtil);
        }
        return rptTreeUtilList;
    }

    public static String getItemId(int m1, int m2, int m3, int m4) {
        return readDoc.mToMstr(m1) + readDoc.mToMstr(m2) + readDoc.mToMstr(m3) + readDoc.mToMstr(m4);
    }

    public static String sqlGen(String itemId, String itemName, String preItemId, int m1, String isAuditInfo, String isTitle) {
        String temp = "insert into RPT_ITEM_DICT\n" +
                "(ID, ITEM_ID, ITEM_NAME, PRE_ITEM_ID,DEFAULT_VALUE,IS_TITLE,FORM_TYPE,IS_AUDIT_INFO, IS_ENABLE, \n" +
                "CREATE_DATE, CREATOR, CREATE_UNIT, UPDATE_DATE, UPDATER, UPDATE_UNIT , IS_DELETE )\n" +
                "select RPT_SEQ.NEXTVAL, '" + itemId + "', '" + itemName +
                "', '" + preItemId + "','','" + isTitle + "', '" + m1 + "','" + isAuditInfo + "','Y',SYSDATE, null, null, SYSDATE, null, null,'N'\n" +
                "from dual where not exists(select ITEM_ID from RPT_ITEM_DICT where ITEM_ID = '" + itemId + "');\n";
        System.out.println(temp);
        return temp;
    }
}
