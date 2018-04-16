package dingshi.com.hibook.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author wangqi
 * @since 2017/11/15 16:20
 */

public class WexinInfo implements Serializable {

    /**
     * openid : or65u0uZd-pjX4OX1NbQ8umKjpGk
     * nickname : SKink
     * sex : 1
     * language : zh_CN
     * city : Huangshan
     * province : Anhui
     * country : CN
     * headimgurl : http://wx.qlogo.cn/mmopen/vi_32/OQRYoR0q3qMwwYXibQfC6ticx72WEtlVX9rjjVSAYgFPywbCkgNIUwwhYL0BdNhGOssEFBRNQJE1SBB5YwF6FPog/0
     * privilege : []
     * unionid : oVPgK1Z2uWkekUobUm367im4S2Zc
     */

    private String openid;
    private String nickname;
    private int sex;
    private String language;
    private String city;
    private String province;
    private String country;
    private String headimgurl;
    private String unionid;
    private List<?> privilege;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public List<?> getPrivilege() {
        return privilege;
    }

    public void setPrivilege(List<?> privilege) {
        this.privilege = privilege;
    }

    @Override
    public String toString() {
        return "WexinInfo{" +
                "openid='" + openid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex=" + sex +
                ", language='" + language + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", country='" + country + '\'' +
                ", headimgurl='" + headimgurl + '\'' +
                ", unionid='" + unionid + '\'' +
                ", privilege=" + privilege +
                '}';
    }
}
