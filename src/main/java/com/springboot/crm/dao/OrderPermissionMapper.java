package com.springboot.crm.dao;

import com.springboot.crm.entity.OrderPermission;
import com.springboot.crm.entity.OrderPermissionExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface OrderPermissionMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_permission
     *
     * @mbg.generated
     */
    long countByExample(OrderPermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_permission
     *
     * @mbg.generated
     */
    int deleteByExample(OrderPermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_permission
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_permission
     *
     * @mbg.generated
     */
    int insert(OrderPermission record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_permission
     *
     * @mbg.generated
     */
    int insertSelective(OrderPermission record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_permission
     *
     * @mbg.generated
     */
    List<OrderPermission> selectByExample(OrderPermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_permission
     *
     * @mbg.generated
     */
    OrderPermission selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_permission
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") OrderPermission record, @Param("example") OrderPermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_permission
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") OrderPermission record, @Param("example") OrderPermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_permission
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(OrderPermission record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_permission
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(OrderPermission record);
}