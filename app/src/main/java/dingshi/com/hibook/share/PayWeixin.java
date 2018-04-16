package dingshi.com.hibook.share;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author wangqi
 * @since 2017/11/18 15:29
 */

public class PayWeixin implements Serializable {

    /**
     * partnerId : 1490812732
     * prepayId : false
     * package : Sign=WXPay
     * nonceStr : 13ql06cmx4uprcdxls0qgl2vng69w7gr
     * timeStamp : 1513569335
     * sign : 8CEF1C0A9BFCA9E2C3515393964B66BA
     * out_trade_no 订单
     */

    private String partnerId;
    private String prepayId;
    @SerializedName("package")
    private String packageX;
    private String nonceStr;
    private String timeStamp;
    private String sign;
    private String out_trade_no;
    /*
     * 柜子的格子编号
     */
    private String cell_id;


    public String getCell_id() {
        return cell_id;
    }

    public void setCell_id(String cell_id) {
        this.cell_id = cell_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
