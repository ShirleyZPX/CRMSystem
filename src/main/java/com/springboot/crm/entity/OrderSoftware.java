package com.springboot.crm.entity;

import java.io.Serializable;

public class OrderSoftware extends Product implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_software.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_software.good_code
     *
     * @mbg.generated
     */
    private String goodCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_software.software_expire_date
     *
     * @mbg.generated
     */
    private String softwareExpireDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_software.hardware_code
     *
     * @mbg.generated
     */
    private String hardwareCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_software.order_id
     *
     * @mbg.generated
     */
    private Integer orderId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_software.goods_id
     *
     * @mbg.generated
     */
    private Integer goodsId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table order_software
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_software.id
     *
     * @return the value of order_software.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_software.id
     *
     * @param id the value for order_software.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_software.good_code
     *
     * @return the value of order_software.good_code
     *
     * @mbg.generated
     */
    public String getGoodCode() {
        return goodCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_software.good_code
     *
     * @param goodCode the value for order_software.good_code
     *
     * @mbg.generated
     */
    public void setGoodCode(String goodCode) {
        this.goodCode = goodCode == null ? null : goodCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_software.software_expire_date
     *
     * @return the value of order_software.software_expire_date
     *
     * @mbg.generated
     */
    public String getSoftwareExpireDate() {
        return softwareExpireDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_software.software_expire_date
     *
     * @param softwareExpireDate the value for order_software.software_expire_date
     *
     * @mbg.generated
     */
    public void setSoftwareExpireDate(String softwareExpireDate) {
        this.softwareExpireDate = softwareExpireDate == null ? null : softwareExpireDate.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_software.hardware_code
     *
     * @return the value of order_software.hardware_code
     *
     * @mbg.generated
     */
    public String getHardwareCode() {
        return hardwareCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_software.hardware_code
     *
     * @param hardwareCode the value for order_software.hardware_code
     *
     * @mbg.generated
     */
    public void setHardwareCode(String hardwareCode) {
        this.hardwareCode = hardwareCode == null ? null : hardwareCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_software.order_id
     *
     * @return the value of order_software.order_id
     *
     * @mbg.generated
     */
    public Integer getOrderId() {
        return orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_software.order_id
     *
     * @param orderId the value for order_software.order_id
     *
     * @mbg.generated
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_software.goods_id
     *
     * @return the value of order_software.goods_id
     *
     * @mbg.generated
     */
    public Integer getGoodsId() {
        return goodsId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_software.goods_id
     *
     * @param goodsId the value for order_software.goods_id
     *
     * @mbg.generated
     */
    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_software
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
        sb.append(", softwareExpireDate=").append(softwareExpireDate);
        sb.append(", hardwareCode=").append(hardwareCode);
        sb.append(", orderId=").append(orderId);
        sb.append(", goodsId=").append(goodsId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}