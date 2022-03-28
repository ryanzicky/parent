package com.zr.encrypt.enums;

/**
 * @Author zhourui
 * @Date 2021/10/12 14:24
 */
public enum DataType {

    NAME("NAME", "用户名"),
    TELEPHONE("TELEPHONE", "电话"),
    MOBILE("MOBILE", "手机"),
    ID_NO("ID_NO", "身份证号码"),
    BANK_NO("BANK_NO", "银行卡号类"),
    MAILBOX("MAILBOX", "邮箱类"),
    ADDRESS("ADDRESS", "地址类"),
    PASS_WORD("PASS_WORD", "密码类");

    private String code;
    private String desc;

    private DataType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
