package com.codeLamb.mapper;

import com.codeLamb.VO.IndexVlogVO;
import com.codeLamb.pojo.Vlog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface VlogMapperCustom {

    List<IndexVlogVO> getIndexVlog(@Param("paramMap") Map<String, Object> map);

    List<IndexVlogVO> getVlogDetailById(@Param("paramMap") Map<String, Object> map);

    void updateVlogToPrivateOrPublic(@Param("paramMap") Map<String, Object> map);

    List<Vlog> getMyVlogByUserId(@Param("paramMap") Map<String, Object> map);
}