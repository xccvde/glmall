package org.example.glamll.product.dao;

import org.apache.ibatis.annotations.Param;
import org.example.glamll.product.entity.AttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 属性&属性分组关联
 * 
 * @author zxc
 * @email z2485861264@gmail.com
 * @date 2024-08-03 21:17:41
 */
@Mapper
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {

    void deleteBatchRelation(@Param("entities") List<AttrAttrgroupRelationEntity> entities);
}
