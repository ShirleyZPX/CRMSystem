package com.springboot.crm.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditOrdersExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table edit_orders
     *
     * @mbg.generated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table edit_orders
     *
     * @mbg.generated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table edit_orders
     *
     * @mbg.generated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table edit_orders
     *
     * @mbg.generated
     */
    public EditOrdersExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table edit_orders
     *
     * @mbg.generated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table edit_orders
     *
     * @mbg.generated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table edit_orders
     *
     * @mbg.generated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table edit_orders
     *
     * @mbg.generated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table edit_orders
     *
     * @mbg.generated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table edit_orders
     *
     * @mbg.generated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table edit_orders
     *
     * @mbg.generated
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table edit_orders
     *
     * @mbg.generated
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table edit_orders
     *
     * @mbg.generated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table edit_orders
     *
     * @mbg.generated
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table edit_orders
     *
     * @mbg.generated
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andOrderIdIsNull() {
            addCriterion("order_id is null");
            return (Criteria) this;
        }

        public Criteria andOrderIdIsNotNull() {
            addCriterion("order_id is not null");
            return (Criteria) this;
        }

        public Criteria andOrderIdEqualTo(Integer value) {
            addCriterion("order_id =", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotEqualTo(Integer value) {
            addCriterion("order_id <>", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdGreaterThan(Integer value) {
            addCriterion("order_id >", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("order_id >=", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdLessThan(Integer value) {
            addCriterion("order_id <", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdLessThanOrEqualTo(Integer value) {
            addCriterion("order_id <=", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdIn(List<Integer> values) {
            addCriterion("order_id in", values, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotIn(List<Integer> values) {
            addCriterion("order_id not in", values, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdBetween(Integer value1, Integer value2) {
            addCriterion("order_id between", value1, value2, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotBetween(Integer value1, Integer value2) {
            addCriterion("order_id not between", value1, value2, "orderId");
            return (Criteria) this;
        }

        public Criteria andAcceptOrderPeopleIdIsNull() {
            addCriterion("accept_order_people_id is null");
            return (Criteria) this;
        }

        public Criteria andAcceptOrderPeopleIdIsNotNull() {
            addCriterion("accept_order_people_id is not null");
            return (Criteria) this;
        }

        public Criteria andAcceptOrderPeopleIdEqualTo(Integer value) {
            addCriterion("accept_order_people_id =", value, "acceptOrderPeopleId");
            return (Criteria) this;
        }

        public Criteria andAcceptOrderPeopleIdNotEqualTo(Integer value) {
            addCriterion("accept_order_people_id <>", value, "acceptOrderPeopleId");
            return (Criteria) this;
        }

        public Criteria andAcceptOrderPeopleIdGreaterThan(Integer value) {
            addCriterion("accept_order_people_id >", value, "acceptOrderPeopleId");
            return (Criteria) this;
        }

        public Criteria andAcceptOrderPeopleIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("accept_order_people_id >=", value, "acceptOrderPeopleId");
            return (Criteria) this;
        }

        public Criteria andAcceptOrderPeopleIdLessThan(Integer value) {
            addCriterion("accept_order_people_id <", value, "acceptOrderPeopleId");
            return (Criteria) this;
        }

        public Criteria andAcceptOrderPeopleIdLessThanOrEqualTo(Integer value) {
            addCriterion("accept_order_people_id <=", value, "acceptOrderPeopleId");
            return (Criteria) this;
        }

        public Criteria andAcceptOrderPeopleIdIn(List<Integer> values) {
            addCriterion("accept_order_people_id in", values, "acceptOrderPeopleId");
            return (Criteria) this;
        }

        public Criteria andAcceptOrderPeopleIdNotIn(List<Integer> values) {
            addCriterion("accept_order_people_id not in", values, "acceptOrderPeopleId");
            return (Criteria) this;
        }

        public Criteria andAcceptOrderPeopleIdBetween(Integer value1, Integer value2) {
            addCriterion("accept_order_people_id between", value1, value2, "acceptOrderPeopleId");
            return (Criteria) this;
        }

        public Criteria andAcceptOrderPeopleIdNotBetween(Integer value1, Integer value2) {
            addCriterion("accept_order_people_id not between", value1, value2, "acceptOrderPeopleId");
            return (Criteria) this;
        }

        public Criteria andInstallTimeIsNull() {
            addCriterion("install_time is null");
            return (Criteria) this;
        }

        public Criteria andInstallTimeIsNotNull() {
            addCriterion("install_time is not null");
            return (Criteria) this;
        }

        public Criteria andInstallTimeEqualTo(Date value) {
            addCriterion("install_time =", value, "installTime");
            return (Criteria) this;
        }

        public Criteria andInstallTimeNotEqualTo(Date value) {
            addCriterion("install_time <>", value, "installTime");
            return (Criteria) this;
        }

        public Criteria andInstallTimeGreaterThan(Date value) {
            addCriterion("install_time >", value, "installTime");
            return (Criteria) this;
        }

        public Criteria andInstallTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("install_time >=", value, "installTime");
            return (Criteria) this;
        }

        public Criteria andInstallTimeLessThan(Date value) {
            addCriterion("install_time <", value, "installTime");
            return (Criteria) this;
        }

        public Criteria andInstallTimeLessThanOrEqualTo(Date value) {
            addCriterion("install_time <=", value, "installTime");
            return (Criteria) this;
        }

        public Criteria andInstallTimeIn(List<Date> values) {
            addCriterion("install_time in", values, "installTime");
            return (Criteria) this;
        }

        public Criteria andInstallTimeNotIn(List<Date> values) {
            addCriterion("install_time not in", values, "installTime");
            return (Criteria) this;
        }

        public Criteria andInstallTimeBetween(Date value1, Date value2) {
            addCriterion("install_time between", value1, value2, "installTime");
            return (Criteria) this;
        }

        public Criteria andInstallTimeNotBetween(Date value1, Date value2) {
            addCriterion("install_time not between", value1, value2, "installTime");
            return (Criteria) this;
        }

        public Criteria andEditTimeIsNull() {
            addCriterion("edit_time is null");
            return (Criteria) this;
        }

        public Criteria andEditTimeIsNotNull() {
            addCriterion("edit_time is not null");
            return (Criteria) this;
        }

        public Criteria andEditTimeEqualTo(Date value) {
            addCriterion("edit_time =", value, "editTime");
            return (Criteria) this;
        }

        public Criteria andEditTimeNotEqualTo(Date value) {
            addCriterion("edit_time <>", value, "editTime");
            return (Criteria) this;
        }

        public Criteria andEditTimeGreaterThan(Date value) {
            addCriterion("edit_time >", value, "editTime");
            return (Criteria) this;
        }

        public Criteria andEditTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("edit_time >=", value, "editTime");
            return (Criteria) this;
        }

        public Criteria andEditTimeLessThan(Date value) {
            addCriterion("edit_time <", value, "editTime");
            return (Criteria) this;
        }

        public Criteria andEditTimeLessThanOrEqualTo(Date value) {
            addCriterion("edit_time <=", value, "editTime");
            return (Criteria) this;
        }

        public Criteria andEditTimeIn(List<Date> values) {
            addCriterion("edit_time in", values, "editTime");
            return (Criteria) this;
        }

        public Criteria andEditTimeNotIn(List<Date> values) {
            addCriterion("edit_time not in", values, "editTime");
            return (Criteria) this;
        }

        public Criteria andEditTimeBetween(Date value1, Date value2) {
            addCriterion("edit_time between", value1, value2, "editTime");
            return (Criteria) this;
        }

        public Criteria andEditTimeNotBetween(Date value1, Date value2) {
            addCriterion("edit_time not between", value1, value2, "editTime");
            return (Criteria) this;
        }

        public Criteria andOperatorIdIsNull() {
            addCriterion("operator_id is null");
            return (Criteria) this;
        }

        public Criteria andOperatorIdIsNotNull() {
            addCriterion("operator_id is not null");
            return (Criteria) this;
        }

        public Criteria andOperatorIdEqualTo(Integer value) {
            addCriterion("operator_id =", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdNotEqualTo(Integer value) {
            addCriterion("operator_id <>", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdGreaterThan(Integer value) {
            addCriterion("operator_id >", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("operator_id >=", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdLessThan(Integer value) {
            addCriterion("operator_id <", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdLessThanOrEqualTo(Integer value) {
            addCriterion("operator_id <=", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdIn(List<Integer> values) {
            addCriterion("operator_id in", values, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdNotIn(List<Integer> values) {
            addCriterion("operator_id not in", values, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdBetween(Integer value1, Integer value2) {
            addCriterion("operator_id between", value1, value2, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdNotBetween(Integer value1, Integer value2) {
            addCriterion("operator_id not between", value1, value2, "operatorId");
            return (Criteria) this;
        }

        public Criteria andReturnedReasonIsNull() {
            addCriterion("returned_reason is null");
            return (Criteria) this;
        }

        public Criteria andReturnedReasonIsNotNull() {
            addCriterion("returned_reason is not null");
            return (Criteria) this;
        }

        public Criteria andReturnedReasonEqualTo(String value) {
            addCriterion("returned_reason =", value, "returnedReason");
            return (Criteria) this;
        }

        public Criteria andReturnedReasonNotEqualTo(String value) {
            addCriterion("returned_reason <>", value, "returnedReason");
            return (Criteria) this;
        }

        public Criteria andReturnedReasonGreaterThan(String value) {
            addCriterion("returned_reason >", value, "returnedReason");
            return (Criteria) this;
        }

        public Criteria andReturnedReasonGreaterThanOrEqualTo(String value) {
            addCriterion("returned_reason >=", value, "returnedReason");
            return (Criteria) this;
        }

        public Criteria andReturnedReasonLessThan(String value) {
            addCriterion("returned_reason <", value, "returnedReason");
            return (Criteria) this;
        }

        public Criteria andReturnedReasonLessThanOrEqualTo(String value) {
            addCriterion("returned_reason <=", value, "returnedReason");
            return (Criteria) this;
        }

        public Criteria andReturnedReasonLike(String value) {
            addCriterion("returned_reason like", value, "returnedReason");
            return (Criteria) this;
        }

        public Criteria andReturnedReasonNotLike(String value) {
            addCriterion("returned_reason not like", value, "returnedReason");
            return (Criteria) this;
        }

        public Criteria andReturnedReasonIn(List<String> values) {
            addCriterion("returned_reason in", values, "returnedReason");
            return (Criteria) this;
        }

        public Criteria andReturnedReasonNotIn(List<String> values) {
            addCriterion("returned_reason not in", values, "returnedReason");
            return (Criteria) this;
        }

        public Criteria andReturnedReasonBetween(String value1, String value2) {
            addCriterion("returned_reason between", value1, value2, "returnedReason");
            return (Criteria) this;
        }

        public Criteria andReturnedReasonNotBetween(String value1, String value2) {
            addCriterion("returned_reason not between", value1, value2, "returnedReason");
            return (Criteria) this;
        }

        public Criteria andBusinessCustomerIdIsNull() {
            addCriterion("business_customer_id is null");
            return (Criteria) this;
        }

        public Criteria andBusinessCustomerIdIsNotNull() {
            addCriterion("business_customer_id is not null");
            return (Criteria) this;
        }

        public Criteria andBusinessCustomerIdEqualTo(Integer value) {
            addCriterion("business_customer_id =", value, "businessCustomerId");
            return (Criteria) this;
        }

        public Criteria andBusinessCustomerIdNotEqualTo(Integer value) {
            addCriterion("business_customer_id <>", value, "businessCustomerId");
            return (Criteria) this;
        }

        public Criteria andBusinessCustomerIdGreaterThan(Integer value) {
            addCriterion("business_customer_id >", value, "businessCustomerId");
            return (Criteria) this;
        }

        public Criteria andBusinessCustomerIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("business_customer_id >=", value, "businessCustomerId");
            return (Criteria) this;
        }

        public Criteria andBusinessCustomerIdLessThan(Integer value) {
            addCriterion("business_customer_id <", value, "businessCustomerId");
            return (Criteria) this;
        }

        public Criteria andBusinessCustomerIdLessThanOrEqualTo(Integer value) {
            addCriterion("business_customer_id <=", value, "businessCustomerId");
            return (Criteria) this;
        }

        public Criteria andBusinessCustomerIdIn(List<Integer> values) {
            addCriterion("business_customer_id in", values, "businessCustomerId");
            return (Criteria) this;
        }

        public Criteria andBusinessCustomerIdNotIn(List<Integer> values) {
            addCriterion("business_customer_id not in", values, "businessCustomerId");
            return (Criteria) this;
        }

        public Criteria andBusinessCustomerIdBetween(Integer value1, Integer value2) {
            addCriterion("business_customer_id between", value1, value2, "businessCustomerId");
            return (Criteria) this;
        }

        public Criteria andBusinessCustomerIdNotBetween(Integer value1, Integer value2) {
            addCriterion("business_customer_id not between", value1, value2, "businessCustomerId");
            return (Criteria) this;
        }

        public Criteria andEquipmentStatusIsNull() {
            addCriterion("equipment_status is null");
            return (Criteria) this;
        }

        public Criteria andEquipmentStatusIsNotNull() {
            addCriterion("equipment_status is not null");
            return (Criteria) this;
        }

        public Criteria andEquipmentStatusEqualTo(String value) {
            addCriterion("equipment_status =", value, "equipmentStatus");
            return (Criteria) this;
        }

        public Criteria andEquipmentStatusNotEqualTo(String value) {
            addCriterion("equipment_status <>", value, "equipmentStatus");
            return (Criteria) this;
        }

        public Criteria andEquipmentStatusGreaterThan(String value) {
            addCriterion("equipment_status >", value, "equipmentStatus");
            return (Criteria) this;
        }

        public Criteria andEquipmentStatusGreaterThanOrEqualTo(String value) {
            addCriterion("equipment_status >=", value, "equipmentStatus");
            return (Criteria) this;
        }

        public Criteria andEquipmentStatusLessThan(String value) {
            addCriterion("equipment_status <", value, "equipmentStatus");
            return (Criteria) this;
        }

        public Criteria andEquipmentStatusLessThanOrEqualTo(String value) {
            addCriterion("equipment_status <=", value, "equipmentStatus");
            return (Criteria) this;
        }

        public Criteria andEquipmentStatusLike(String value) {
            addCriterion("equipment_status like", value, "equipmentStatus");
            return (Criteria) this;
        }

        public Criteria andEquipmentStatusNotLike(String value) {
            addCriterion("equipment_status not like", value, "equipmentStatus");
            return (Criteria) this;
        }

        public Criteria andEquipmentStatusIn(List<String> values) {
            addCriterion("equipment_status in", values, "equipmentStatus");
            return (Criteria) this;
        }

        public Criteria andEquipmentStatusNotIn(List<String> values) {
            addCriterion("equipment_status not in", values, "equipmentStatus");
            return (Criteria) this;
        }

        public Criteria andEquipmentStatusBetween(String value1, String value2) {
            addCriterion("equipment_status between", value1, value2, "equipmentStatus");
            return (Criteria) this;
        }

        public Criteria andEquipmentStatusNotBetween(String value1, String value2) {
            addCriterion("equipment_status not between", value1, value2, "equipmentStatus");
            return (Criteria) this;
        }

        public Criteria andBusinessLevelIsNull() {
            addCriterion("business_level is null");
            return (Criteria) this;
        }

        public Criteria andBusinessLevelIsNotNull() {
            addCriterion("business_level is not null");
            return (Criteria) this;
        }

        public Criteria andBusinessLevelEqualTo(String value) {
            addCriterion("business_level =", value, "businessLevel");
            return (Criteria) this;
        }

        public Criteria andBusinessLevelNotEqualTo(String value) {
            addCriterion("business_level <>", value, "businessLevel");
            return (Criteria) this;
        }

        public Criteria andBusinessLevelGreaterThan(String value) {
            addCriterion("business_level >", value, "businessLevel");
            return (Criteria) this;
        }

        public Criteria andBusinessLevelGreaterThanOrEqualTo(String value) {
            addCriterion("business_level >=", value, "businessLevel");
            return (Criteria) this;
        }

        public Criteria andBusinessLevelLessThan(String value) {
            addCriterion("business_level <", value, "businessLevel");
            return (Criteria) this;
        }

        public Criteria andBusinessLevelLessThanOrEqualTo(String value) {
            addCriterion("business_level <=", value, "businessLevel");
            return (Criteria) this;
        }

        public Criteria andBusinessLevelLike(String value) {
            addCriterion("business_level like", value, "businessLevel");
            return (Criteria) this;
        }

        public Criteria andBusinessLevelNotLike(String value) {
            addCriterion("business_level not like", value, "businessLevel");
            return (Criteria) this;
        }

        public Criteria andBusinessLevelIn(List<String> values) {
            addCriterion("business_level in", values, "businessLevel");
            return (Criteria) this;
        }

        public Criteria andBusinessLevelNotIn(List<String> values) {
            addCriterion("business_level not in", values, "businessLevel");
            return (Criteria) this;
        }

        public Criteria andBusinessLevelBetween(String value1, String value2) {
            addCriterion("business_level between", value1, value2, "businessLevel");
            return (Criteria) this;
        }

        public Criteria andBusinessLevelNotBetween(String value1, String value2) {
            addCriterion("business_level not between", value1, value2, "businessLevel");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table edit_orders
     *
     * @mbg.generated do_not_delete_during_merge
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table edit_orders
     *
     * @mbg.generated
     */
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}