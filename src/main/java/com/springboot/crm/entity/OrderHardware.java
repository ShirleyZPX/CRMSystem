package com.springboot.crm.entity;

import java.io.Serializable;

public class OrderHardware extends Product implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_hardware.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_hardware.goods_code
     *
     * @mbg.generated
     */
    private String goodCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_hardware.deploy_mode
     *
     * @mbg.generated
     */
    private String deployMode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_hardware.up_port_code
     *
     * @mbg.generated
     */
    private String upPortCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_hardware.down_port_code
     *
     * @mbg.generated
     */
    private String downPortCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_hardware.public_ipv4
     *
     * @mbg.generated
     */
    private String publicIpv4;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_hardware.public_ipv6
     *
     * @mbg.generated
     */
    private String publicIpv6;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_hardware.private_ipv4
     *
     * @mbg.generated
     */
    private String privateIpv4;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_hardware.private_ipv6
     *
     * @mbg.generated
     */
    private String privateIpv6;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_hardware.manage_ipv4
     *
     * @mbg.generated
     */
    private String manageIpv4;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_hardware.manage_ipv6
     *
     * @mbg.generated
     */
    private String manageIpv6;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_hardware.platform_address
     *
     * @mbg.generated
     */
    private String platformAddress;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_hardware.platform_connect_mode
     *
     * @mbg.generated
     */
    private String platformConnectMode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_hardware.ips_update_time
     *
     * @mbg.generated
     */
    private String ipsUpdateTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_hardware.virus_date
     *
     * @mbg.generated
     */
    private String virusDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_hardware.app_recognize_date
     *
     * @mbg.generated
     */
    private String appRecognizeDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_hardware.url_date
     *
     * @mbg.generated
     */
    private String urlDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_hardware.waf_date
     *
     * @mbg.generated
     */
    private String wafDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_hardware.order_id
     *
     * @mbg.generated
     */
    private Integer orderId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_hardware.goods_id
     *
     * @mbg.generated
     */
    private Integer goodsId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table order_hardware
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_hardware.id
     *
     * @return the value of order_hardware.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_hardware.id
     *
     * @param id the value for order_hardware.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_hardware.goods_code
     *
     * @return the value of order_hardware.goods_code
     *
     * @mbg.generated
     */
    public String getGoodCode() {
        return goodCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_hardware.goods_code
     *
     * @param goodCode the value for order_hardware.goods_code
     *
     * @mbg.generated
     */
    public void setGoodCode(String goodCode) {
        this.goodCode = goodCode == null ? null : goodCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_hardware.deploy_mode
     *
     * @return the value of order_hardware.deploy_mode
     *
     * @mbg.generated
     */
    public String getDeployMode() {
        return deployMode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_hardware.deploy_mode
     *
     * @param deployMode the value for order_hardware.deploy_mode
     *
     * @mbg.generated
     */
    public void setDeployMode(String deployMode) {
        this.deployMode = deployMode == null ? null : deployMode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_hardware.up_port_code
     *
     * @return the value of order_hardware.up_port_code
     *
     * @mbg.generated
     */
    public String getUpPortCode() {
        return upPortCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_hardware.up_port_code
     *
     * @param upPortCode the value for order_hardware.up_port_code
     *
     * @mbg.generated
     */
    public void setUpPortCode(String upPortCode) {
        this.upPortCode = upPortCode == null ? null : upPortCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_hardware.down_port_code
     *
     * @return the value of order_hardware.down_port_code
     *
     * @mbg.generated
     */
    public String getDownPortCode() {
        return downPortCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_hardware.down_port_code
     *
     * @param downPortCode the value for order_hardware.down_port_code
     *
     * @mbg.generated
     */
    public void setDownPortCode(String downPortCode) {
        this.downPortCode = downPortCode == null ? null : downPortCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_hardware.public_ipv4
     *
     * @return the value of order_hardware.public_ipv4
     *
     * @mbg.generated
     */
    public String getPublicIpv4() {
        return publicIpv4;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_hardware.public_ipv4
     *
     * @param publicIpv4 the value for order_hardware.public_ipv4
     *
     * @mbg.generated
     */
    public void setPublicIpv4(String publicIpv4) {
        this.publicIpv4 = publicIpv4 == null ? null : publicIpv4.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_hardware.public_ipv6
     *
     * @return the value of order_hardware.public_ipv6
     *
     * @mbg.generated
     */
    public String getPublicIpv6() {
        return publicIpv6;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_hardware.public_ipv6
     *
     * @param publicIpv6 the value for order_hardware.public_ipv6
     *
     * @mbg.generated
     */
    public void setPublicIpv6(String publicIpv6) {
        this.publicIpv6 = publicIpv6 == null ? null : publicIpv6.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_hardware.private_ipv4
     *
     * @return the value of order_hardware.private_ipv4
     *
     * @mbg.generated
     */
    public String getPrivateIpv4() {
        return privateIpv4;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_hardware.private_ipv4
     *
     * @param privateIpv4 the value for order_hardware.private_ipv4
     *
     * @mbg.generated
     */
    public void setPrivateIpv4(String privateIpv4) {
        this.privateIpv4 = privateIpv4 == null ? null : privateIpv4.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_hardware.private_ipv6
     *
     * @return the value of order_hardware.private_ipv6
     *
     * @mbg.generated
     */
    public String getPrivateIpv6() {
        return privateIpv6;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_hardware.private_ipv6
     *
     * @param privateIpv6 the value for order_hardware.private_ipv6
     *
     * @mbg.generated
     */
    public void setPrivateIpv6(String privateIpv6) {
        this.privateIpv6 = privateIpv6 == null ? null : privateIpv6.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_hardware.manage_ipv4
     *
     * @return the value of order_hardware.manage_ipv4
     *
     * @mbg.generated
     */
    public String getManageIpv4() {
        return manageIpv4;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_hardware.manage_ipv4
     *
     * @param manageIpv4 the value for order_hardware.manage_ipv4
     *
     * @mbg.generated
     */
    public void setManageIpv4(String manageIpv4) {
        this.manageIpv4 = manageIpv4 == null ? null : manageIpv4.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_hardware.manage_ipv6
     *
     * @return the value of order_hardware.manage_ipv6
     *
     * @mbg.generated
     */
    public String getManageIpv6() {
        return manageIpv6;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_hardware.manage_ipv6
     *
     * @param manageIpv6 the value for order_hardware.manage_ipv6
     *
     * @mbg.generated
     */
    public void setManageIpv6(String manageIpv6) {
        this.manageIpv6 = manageIpv6 == null ? null : manageIpv6.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_hardware.platform_address
     *
     * @return the value of order_hardware.platform_address
     *
     * @mbg.generated
     */
    public String getPlatformAddress() {
        return platformAddress;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_hardware.platform_address
     *
     * @param platformAddress the value for order_hardware.platform_address
     *
     * @mbg.generated
     */
    public void setPlatformAddress(String platformAddress) {
        this.platformAddress = platformAddress == null ? null : platformAddress.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_hardware.platform_connect_mode
     *
     * @return the value of order_hardware.platform_connect_mode
     *
     * @mbg.generated
     */
    public String getPlatformConnectMode() {
        return platformConnectMode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_hardware.platform_connect_mode
     *
     * @param platformConnectMode the value for order_hardware.platform_connect_mode
     *
     * @mbg.generated
     */
    public void setPlatformConnectMode(String platformConnectMode) {
        this.platformConnectMode = platformConnectMode == null ? null : platformConnectMode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_hardware.ips_update_time
     *
     * @return the value of order_hardware.ips_update_time
     *
     * @mbg.generated
     */
    public String getIpsUpdateTime() {
        return ipsUpdateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_hardware.ips_update_time
     *
     * @param ipsUpdateTime the value for order_hardware.ips_update_time
     *
     * @mbg.generated
     */
    public void setIpsUpdateTime(String ipsUpdateTime) {
        this.ipsUpdateTime = ipsUpdateTime == null ? null : ipsUpdateTime.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_hardware.virus_date
     *
     * @return the value of order_hardware.virus_date
     *
     * @mbg.generated
     */
    public String getVirusDate() {
        return virusDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_hardware.virus_date
     *
     * @param virusDate the value for order_hardware.virus_date
     *
     * @mbg.generated
     */
    public void setVirusDate(String virusDate) {
        this.virusDate = virusDate == null ? null : virusDate.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_hardware.app_recognize_date
     *
     * @return the value of order_hardware.app_recognize_date
     *
     * @mbg.generated
     */
    public String getAppRecognizeDate() {
        return appRecognizeDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_hardware.app_recognize_date
     *
     * @param appRecognizeDate the value for order_hardware.app_recognize_date
     *
     * @mbg.generated
     */
    public void setAppRecognizeDate(String appRecognizeDate) {
        this.appRecognizeDate = appRecognizeDate == null ? null : appRecognizeDate.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_hardware.url_date
     *
     * @return the value of order_hardware.url_date
     *
     * @mbg.generated
     */
    public String getUrlDate() {
        return urlDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_hardware.url_date
     *
     * @param urlDate the value for order_hardware.url_date
     *
     * @mbg.generated
     */
    public void setUrlDate(String urlDate) {
        this.urlDate = urlDate == null ? null : urlDate.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_hardware.waf_date
     *
     * @return the value of order_hardware.waf_date
     *
     * @mbg.generated
     */
    public String getWafDate() {
        return wafDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_hardware.waf_date
     *
     * @param wafDate the value for order_hardware.waf_date
     *
     * @mbg.generated
     */
    public void setWafDate(String wafDate) {
        this.wafDate = wafDate == null ? null : wafDate.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_hardware.order_id
     *
     * @return the value of order_hardware.order_id
     *
     * @mbg.generated
     */
    public Integer getOrderId() {
        return orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_hardware.order_id
     *
     * @param orderId the value for order_hardware.order_id
     *
     * @mbg.generated
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_hardware.goods_id
     *
     * @return the value of order_hardware.goods_id
     *
     * @mbg.generated
     */
    public Integer getGoodsId() {
        return goodsId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_hardware.goods_id
     *
     * @param goodsId the value for order_hardware.goods_id
     *
     * @mbg.generated
     */
    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_hardware
     *
     * @mbg.generated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", goodCode=").append(goodCode);
        sb.append(", deployMode=").append(deployMode);
        sb.append(", upPortCode=").append(upPortCode);
        sb.append(", downPortCode=").append(downPortCode);
        sb.append(", publicIpv4=").append(publicIpv4);
        sb.append(", publicIpv6=").append(publicIpv6);
        sb.append(", privateIpv4=").append(privateIpv4);
        sb.append(", privateIpv6=").append(privateIpv6);
        sb.append(", manageIpv4=").append(manageIpv4);
        sb.append(", manageIpv6=").append(manageIpv6);
        sb.append(", platformAddress=").append(platformAddress);
        sb.append(", platformConnectMode=").append(platformConnectMode);
        sb.append(", ipsUpdateTime=").append(ipsUpdateTime);
        sb.append(", virusDate=").append(virusDate);
        sb.append(", appRecognizeDate=").append(appRecognizeDate);
        sb.append(", urlDate=").append(urlDate);
        sb.append(", wafDate=").append(wafDate);
        sb.append(", orderId=").append(orderId);
        sb.append(", goodsId=").append(goodsId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}