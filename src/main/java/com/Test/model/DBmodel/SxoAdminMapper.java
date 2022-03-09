package com.Test.model.DBmodel;

import com.Test.model.DBmodel.SxoAdmin;
import com.Test.model.DBmodel.SxoAdminExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SxoAdminMapper {
    int countByExample(SxoAdminExample example);

    int deleteByExample(SxoAdminExample example);

    int insert(SxoAdmin record);

    int insertSelective(SxoAdmin record);

    List<SxoAdmin> selectByExample(SxoAdminExample example);

    int updateByExampleSelective(@Param("record") SxoAdmin record, @Param("example") SxoAdminExample example);

    int updateByExample(@Param("record") SxoAdmin record, @Param("example") SxoAdminExample example);
}