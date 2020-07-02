package com.springboot.crm.dao;

import com.springboot.crm.entity.OrderHardware;
import com.springboot.crm.entity.OrderHardwareExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderHardwareMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_hardware
     *
     * @mbg.generated
     */
    long countByExample(OrderHardwareExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_hardware
     *
     * @mbg.generated
     */
    int deleteByExample(OrderHardwareExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_hardware
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_hardware
     *
     * @mbg.generated
     */
    int insert(OrderHardware record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_hardware
     *
     * @mbg.generated
     */
    int insertSelective(OrderHardware record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_hardware
     *
     * @mbg.generated
     */
    List<OrderHardware> selectByExample(OrderHardwareExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_hardware
     *
     * @mbg.generated
     */
    OrderHardware selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_hardware
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") OrderHardware record, @Param("example") OrderHardwareExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_hardware
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") OrderHardware record, @Param("example") OrderHardwareExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_hardware
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(OrderHardware record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_hardware
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(OrderHardware record);
}