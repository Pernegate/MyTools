package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @param
 * @author zl
 * 将list转为tree的工具类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RPTTreeUtil {
    private String id;

    private String name;

    private String pId;

    private int level;

    private List<RPTTreeUtil> childrenList;

    public RPTTreeUtil(String id, String name, String pId,int level) {
        this.id = id;
        this.name = name;
        this.pId = pId;
        this.level = level;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setChildrenList(List<RPTTreeUtil> childrenList) {
        this.childrenList = childrenList;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getpId() {
        return pId;
    }

    public int getLevel() {
        return level;
    }

    public List<RPTTreeUtil> getChildrenList() {
        return childrenList;
    }

}
