package com.springboot.crm.entity;

import java.io.Serializable;
import java.util.Date;

public class Orders implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.business_customer_id
     *
     * @mbg.generated
     */
    private Integer businessCustomerId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.install_time
     *
     * @mbg.generated
     */
    private Date installTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.accept_order_people_id
     *
     * @mbg.generated
     */
    private Integer acceptOrderPeopleId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.returned_reason
     *
     * @mbg.generated
     */
    private String returnedReason;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.operator_id
     *
     * @mbg.generated
     */
    private Integer operatorId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.edit_time
     *
     * @mbg.generated
     */
    private Date editTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table orders
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orders.id
     *
     * @return the value of orders.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orders.id
     *
     * @param id the value for orders.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orders.business_customer_id
     *
     * @return the value of orders.business_customer_id
     *
     * @mbg.generated
     */
    public Integer getBusinessCustomerId() {
        return businessCustomerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orders.business_customer_id
     *
     * @param businessCustomerId the value for orders.business_customer_id
     *
     * @mbg.generated
     */
    public void setBusinessCustomerId(Integer businessCustomerId) {
        this.businessCustomerId = businessCustomerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orders.install_time
     *
     * @return the value of orders.install_time
     *
     * @mbg.generated
     */
    public Date getInstallTime() {
        return installTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orders.install_time
     *
     * @param installTime the value for orders.install_time
     *
     * @mbg.generated
     */
    public void setInstallTime(Date installTime) {
        this.installTime = installTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orders.accept_order_people_id
     *
     * @return the value of orders.accept_order_people_id
     *
     * @mbg.generated
     */
    public Integer getAcceptOrderPeopleId() {
        return acceptOrderPeopleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orders.accept_order_people_id
     *
     * @param acceptOrderPeopleId the value for orders.accept_order_people_id
     *
     * @mbg.generated
     */
    public void setAcceptOrderPeopleId(Integer acceptOrderPeopleId) {
        this.acceptOrderPeopleId = acceptOrderPeopleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orders.returned_reason
     *
     * @return the value of orders.returned_reason
     *
     * @mbg.generated
     */
    public String getReturnedReason() {
        return returnedReason;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orders.returned_reason
     *
     * @param returnedReason the value for orders.returned_reason
     *
     * @mbg.generated
     */
    public void setReturnedReason(String returnedReason) {
        this.returnedReason = returnedReason == null ? null : returnedReason.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orders.operator_id
     *
     * @return the value of orders.operator_id
     *
     * @mbg.generated
     */
    public Integer getOperatorId() {
        return operatorId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orders.operator_id
     *
     * @param operatorId the value for orders.operator_id
     *
     * @mbg.generated
     */
    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orders.edit_time
     *
     * @return the value of orders.edit_time
     *
     * @mbg.generated
     */
    public Date getEditTime() {
        return editTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orders.edit_time
     *
     * @param editTime the value for orders.edit_time
     *
     * @mbg.generated
     */
    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table orders
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
        sb.append(", businessCustomerId=").append(businessCustomerId);
        sb.append(", installTime=").append(installTime);
        sb.append(", acceptOrderPeopleId=").append(acceptOrderPeopleId);
        sb.append(", returnedReason=").append(returnedReason);
        sb.append(", operatorId=").append(operatorId);
        sb.append(", editTime=").append(editTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}