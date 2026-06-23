package com.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.entity.Policy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 政策公告Mapper接口
 */
@Mapper
public interface PolicyMapper extends BaseMapper<Policy> {

    /**
     * 根据分类查询政策列表
     */
    List<Policy> selectListByCategory(@Param("category") Integer category, @Param("offset") Integer offset, @Param("size") Integer size);

    /**
     * 增加浏览次数
     */
    int incrementViewCount(@Param("id") Long id);
}
