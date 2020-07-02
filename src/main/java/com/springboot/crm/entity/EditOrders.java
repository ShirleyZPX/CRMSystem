package com.springboot.crm.entity;

import java.io.Serializable;
import java.util.Date;

public class EditOrders implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column edit_orders.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column edit_orders.order_id
     *
     * @mbg.generated
     */
    private Integer orderId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column edit_orders.accept_order_people_id
     *
     * @mbg.generated
     */
    private Integer acceptOrderPeopleId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column edit_orders.install_time
     *
     * @mbg.generated
     */
    private Date installTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column edit_orders.edit_time
     *
     * @mbg.generated
     */
    private Date editTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column edit_orders.operator_id
     *
     * @mbg.generated
     */
    private Integer operatorId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column edit_orders.returned_reason
     *
     * @mbg.generated
     */
    private String returnedReason;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column edit_orders.business_customer_id
     *
     * @mbg.generated
     */
    private Integer businessCustomerId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column edit_orders.equipment_status
     *
     * @mbg.generated
     */
    private String equipmentStatus;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column edit_orders.business_level
     *
     * @mbg.generated
     */
    private String businessLevel;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table edit_orders
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column edit_orders.id
     *
     * @return the value of edit_orders.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column edit_orders.id
     *
     * @param id the value for edit_orders.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column edit_orders.order_id
     *
     * @return the value of edit_orders.order_id
     *
     * @mbg.generated
     */
    public Integer getOrderId() {
        return orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column edit_orders.order_id
     *
     * @param orderId the value for edit_orders.order_id
     *
     * @mbg.generated
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column edit_orders.accept_order_people_id
     *
     * @return the value of edit_orders.accept_order_people_id
     *
     * @mbg.generated
     */
    public Integer getAcceptOrderPeopleId() {
        return acceptOrderPeopleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column edit_orders.accept_order_people_id
     *
     * @param acceptOrderPeopleId the value for edit_orders.accept_order_people_id
     *
     * @mbg.generated
     */
    public void setAcceptOrderPeopleId(Integer acceptOrderPeopleId) {
        this.acceptOrderPeopleId = acceptOrderPeopleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column edit_orders.install_time
     *
     * @return the value of edit_orders.install_time
     *
     * @mbg.generated
     */
    public Date getInstallTime() {
        return installTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column edit_orders.install_time
     *
     * @param installTime the value for edit_orders.install_time
     *
     * @mbg.generated
     */
    public void setInstallTime(Date installTime) {
        this.installTime = installTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column edit_orders.edit_time
     *
     * @return the value of edit_orders.edit_time
     *
     * @mbg.generated
     */
    public Date getEditTime() {
        return editTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column edit_orders.edit_time
     *
     * @param editTime the value for edit_orders.edit_time
     *
     * @mbg.generated
     */
    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column edit_orders.operator_id
     *
     * @return the value of edit_orders.operator_id
     *
     * @mbg.generated
     */
    public Integer getOperatorId() {
        return operatorId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column edit_orders.operator_id
     *
     * @param operatorId the value for edit_orders.operator_id
     *
     * @mbg.generated
     */
    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column edit_orders.returned_reason
     *
     * @return the value of edit_orders.returned_reason
     *
     * @mbg.generated
     */
    public String getReturnedReason() {
        return returnedReason;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column edit_orders.returned_reason
     *
     * @param returnedReason the value for edit_orders.returned_reason
     *
     * @mbg.generated
     */
    public void setReturnedReason(String returnedReason) {
        this.returnedReason = returnedReason == null ? null : returnedReason.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column edit_orders.business_customer_id
     *
     * @return the value of edit_orders.business_customer_id
     *
     * @mbg.generated
     */
    public Integer getBusinessCustomerId() {
        return businessCustomerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column edit_orders.business_customer_id
     *
     * @param businessCustomerId the value for edit_orders.business_customer_id
     *
     * @mbg.generated
     */
    public void setBusinessCustomerId(Integer businessCustomerId) {
        this.businessCustomerId = businessCustomerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column edit_orders.equipment_status
     *
     * @return the value of edit_orders.equipment_status
     *
     * @mbg.generated
     */
    public String getEquipmentStatus() {
        return equipmentStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column edit_orders.equipment_status
     *
     * @param equipmentStatus the value for edit_orders.equipment_status
     *
     * @mbg.generated
     */
    public void setEquipmentStatus(String equipmentStatus) {
        this.equipmentStatus = equipmentStatus == null ? null : equipmentStatus.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column edit_orders.business_level
     *
     * @return the value of edit_orders.business_level
     *
     * @mbg.generated
     */
    public String getBusinessLevel() {
        return businessLevel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column edit_orders.business_level
     *
     * @param businessLevel the value for edit_orders.business_level
     *
     * @mbg.generated
     */
    public void setBusinessLevel(String businessLevel) {
        this.businessLevel = businessLevel == null ? null : businessLevel.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table edit_orders
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
        sb.append(", orderId=").append(orderId);
        sb.append(", acceptOrderPeopleId=").append(acceptOrderPeopleId);
        sb.append(", installTime=").append(installTime);
        sb.append(", editTime=").append(editTime);
        sb.append(", operatorId=").append(operatorId);
        sb.append(", returnedReason=").append(returnedReason);
        sb.append(", businessCustomerId=").append(businessCustomerId);
        sb.append(", equipmentStatus=").append(equipmentStatus);
        sb.append(", businessLevel=").append(businessLevel);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}