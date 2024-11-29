package org.example.glamll.product.dao;

import org.example.glamll.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author zxc
 * @email z2485861264@gmail.com
 * @date 2024-08-03 21:17:41
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
