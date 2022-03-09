package com.Test.model.DBmodel;

import com.Test.model.DBmodel.SxoAnswer;
import com.Test.model.DBmodel.SxoAnswerExample;
import com.Test.model.DBmodel.SxoAnswerWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SxoAnswerMapper {
    int countByExample(SxoAnswerExample example);

    int deleteByExample(SxoAnswerExample example);

    int insert(SxoAnswerWithBLOBs record);

    int insertSelective(SxoAnswerWithBLOBs record);

    List<SxoAnswerWithBLOBs> selectByExampleWithBLOBs(SxoAnswerExample example);

    List<SxoAnswer> selectByExample(SxoAnswerExample example);

    int updateByExampleSelective(@Param("record") SxoAnswerWithBLOBs record, @Param("example") SxoAnswerExample example);

    int updateByExampleWithBLOBs(@Param("record") SxoAnswerWithBLOBs record, @Param("example") SxoAnswerExample example);

    int updateByExample(@Param("record") SxoAnswer record, @Param("example") SxoAnswerExample example);
}