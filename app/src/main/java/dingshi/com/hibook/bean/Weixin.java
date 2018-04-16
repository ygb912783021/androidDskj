package dingshi.com.hibook.bean;

/**
 * @author wangqi
 * @since 2017/11/15 16:09
 */

public class Weixin {


    /**
     * access_token : 3_BoctN8geZOWdOkbWL-Wkoi-pQa3LmG2BBCieVgaJIXIw10MUJJpELGotpQA1s4r99sT7Mp7kSfw5lPsEBI7GQ2hMzY1s6eojFmj8KA-5_2k
     * expires_in : 7200
     * refresh_token : 3_QitsFGOggUXFOSyQ3vn9DA8paZhOZCH8QWZXUfuTlUNBsVr_6-yP9Erd-QopxfpR2acKOX4qKO_eFhMpIAvb0IXFFmjG9AB5Pa9ibdgrDfQ
     * openid : or65u0uZd-pjX4OX1NbQ8umKjpGk
     * scope : snsapi_userinfo
     * unionid : oVPgK1Z2uWkekUobUm367im4S2Zc
     */

    private String access_token;
    private int expires_in;
    private String refresh_token;
    private String openid;
    private String scope;
    private String unionid;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }





}
