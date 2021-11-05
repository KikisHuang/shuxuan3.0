package com.gxdingo.sg.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 商圈-展开评论列表
 *
 * @author JM
 */
public class BusinessDistrictUnfoldCommentListBean implements Serializable {
    private int total;
    private String myIdentifier;
    private ArrayList<UnfoldComment> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getMyIdentifier() {
        return myIdentifier;
    }

    public void setMyIdentifier(String myIdentifier) {
        this.myIdentifier = myIdentifier;
    }

    public ArrayList<UnfoldComment> getList() {
        return list;
    }

    public void setList(ArrayList<UnfoldComment> list) {
        this.list = list;
    }

    public class UnfoldComment extends BusinessDistrictListBean.Comment implements Serializable {

    }
}
