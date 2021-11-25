package com.gxdingo.sg.model;

import android.view.View;
import android.widget.EditText;

/**
 * @author: Kikis
 * @date: 2021/3/31
 * @page:
 */
public class ClientMineModel {


    public ClientMineModel() {

    }


    public void edittextFocusChangeListener(EditText edittext , View.OnFocusChangeListener listener) {
        edittext.setOnFocusChangeListener(listener);
    }


}
