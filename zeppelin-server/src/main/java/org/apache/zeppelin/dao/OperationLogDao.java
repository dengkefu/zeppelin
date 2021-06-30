package org.apache.zeppelin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.zeppelin.entity.log.OperationLog;

/**
 * @author duncan.fu
 */
@Mapper
public interface OperationLogDao {

    int insert(OperationLog record);

    int insertSelective(OperationLog record);

    OperationLog selectByPrimaryKey(@Param("id") Integer id);
}