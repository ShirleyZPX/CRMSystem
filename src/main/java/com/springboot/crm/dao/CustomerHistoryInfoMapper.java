package com.springboot.crm.dao;

import com.springboot.crm.entity.CustomerHistoryInfo;
import com.springboot.crm.entity.CustomerHistoryInfoExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomerHistoryInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table customer_history_info
     *
     * @mbg.generated
     */
    long countByExample(CustomerHistoryInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table customer_history_info
     *
     * @mbg.generated
     */
    int deleteByExample(CustomerHistoryInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table customer_history_info
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table customer_history_info
     *
     * @mbg.generated
     */
    int insert(CustomerHistoryInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table customer_history_info
     *
     * @mbg.generated
     */
    int insertSelective(CustomerHistoryInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table customer_history_info
     *
     * @mbg.generated
     */
    List<CustomerHistoryInfo> selectByExample(CustomerHistoryInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table customer_history_info
     *
     * @mbg.generated
     */
    CustomerHistoryInfo selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table customer_history_info
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") CustomerHistoryInfo record, @Param("example") CustomerHistoryInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table customer_history_info
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") CustomerHistoryInfo record, @Param("example") CustomerHistoryInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table customer_history_info
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(CustomerHistoryInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table customer_history_info
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(CustomerHistoryInfo record);
}