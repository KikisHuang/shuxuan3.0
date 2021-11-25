package com.gxdingo.sg.model;

/**
 * @author: Kikis
 * @date: 2021/4/28
 * @page:
 */
public class ChangePhoneModel {
    // 1输入旧手机号 2发送验证码到新手机号 3绑定新号码
    private int mStep = 1;

    public ChangePhoneModel() {

    }

    public void next() {
        mStep++;
    }

    public void lastStep() {
        mStep--;
    }

    public int pageLogic() {
        return mStep;
    }
}
