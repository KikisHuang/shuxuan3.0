package com.gxdingo.sg.bean;

import java.util.List;

/**
 * @author: Weaving
 * @date: 2021/6/3
 * @page:
 */
public class BusinessScopeEvent {

    public List<StoreCategoryBean> data;

    public String selectedScope;

    public BusinessScopeEvent(List<StoreCategoryBean> data, String selectedScope) {
        this.data = data;
        this.selectedScope = selectedScope;
    }

    public BusinessScopeEvent(List<StoreCategoryBean> data) {
        this.data = data;
    }
}
