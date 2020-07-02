package com.springboot.crm.dao;

import com.springboot.crm.entity.BusinessPackage;
import com.springboot.crm.entity.BusinessPackageExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface BusinessPackageMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table business_package
     *
     * @mbg.generated
     */
    long countByExample(BusinessPackageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table business_package
     *
     * @mbg.generated
     */
    int deleteByExample(BusinessPackageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table business_package
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table business_package
     *
     * @mbg.generated
     */
    int insert(BusinessPackage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table business_package
     *
     * @mbg.generated
     */
    int insertSelective(BusinessPackage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table business_package
     *
     * @mbg.generated
     */
    List<BusinessPackage> selectByExample(BusinessPackageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table business_package
     *
     * @mbg.generated
     */
    BusinessPackage selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table business_package
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") BusinessPackage record, @Param("example") BusinessPackageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table business_package
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") BusinessPackage record, @Param("example") BusinessPackageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table business_package
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(BusinessPackage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table business_package
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(BusinessPackage record);
}