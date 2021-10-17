package com.gxdingo.sg.bean;

import java.util.List;

/**
 * @author: Weaving
 * @date: 2021/4/23
 * @page:
 */
public class AddressListBean {

    private List<AddressBean> addresses;

    public List<AddressBean> getList() {
        return addresses;
    }

    public void setList(List<AddressBean> list) {
        this.addresses = list;
    }
}
