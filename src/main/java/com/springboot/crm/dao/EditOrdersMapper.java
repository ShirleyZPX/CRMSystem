package com.springboot.crm.dao;

import com.springboot.crm.entity.EditOrders;
import com.springboot.crm.entity.EditOrdersExample;
import com.springboot.crm.entity.OrdersExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface EditOrdersMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table edit_orders
     *
     * @mbg.generated
     */
    long countByExample(OrdersExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table edit_orders
     *
     * @mbg.generated
     */
    int deleteByExample(OrdersExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table edit_orders
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table edit_orders
     *
     * @mbg.generated
     */
    int insert(EditOrders record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table edit_orders
     *
     * @mbg.generated
     */
    int insertSelective(EditOrders record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table edit_orders
     *
     * @mbg.generated
     */
    List<EditOrders> selectByExample(EditOrdersExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table edit_orders
     *
     * @mbg.generated
     */
    EditOrders selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table edit_orders
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") EditOrders record, @Param("example") EditOrdersExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table edit_orders
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") EditOrders record, @Param("example") EditOrdersExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table edit_orders
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(EditOrders record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table edit_orders
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(EditOrders record);
}