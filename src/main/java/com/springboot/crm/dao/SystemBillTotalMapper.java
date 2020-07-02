package com.springboot.crm.dao;

import com.springboot.crm.entity.SystemBillTotal;
import com.springboot.crm.entity.SystemBillTotalExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface SystemBillTotalMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_bill_total
     *
     * @mbg.generated
     */
    long countByExample(SystemBillTotalExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_bill_total
     *
     * @mbg.generated
     */
    int deleteByExample(SystemBillTotalExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_bill_total
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_bill_total
     *
     * @mbg.generated
     */
    int insert(SystemBillTotal record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_bill_total
     *
     * @mbg.generated
     */
    int insertSelective(SystemBillTotal record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_bill_total
     *
     * @mbg.generated
     */
    List<SystemBillTotal> selectByExample(SystemBillTotalExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_bill_total
     *
     * @mbg.generated
     */
    SystemBillTotal selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_bill_total
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") SystemBillTotal record, @Param("example") SystemBillTotalExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_bill_total
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") SystemBillTotal record, @Param("example") SystemBillTotalExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_bill_total
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(SystemBillTotal record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_bill_total
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(SystemBillTotal record);
}