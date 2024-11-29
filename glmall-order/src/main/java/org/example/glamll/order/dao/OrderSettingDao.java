package org.example.glamll.order.dao;

import org.example.glamll.order.entity.OrderSettingEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单配置信息
 * 
 * @author zxc
 * @email z2485861264@gmail.com
 * @date 2024-08-04 19:17:15
 */
@Mapper
public interface OrderSettingDao extends BaseMapper<OrderSettingEntity> {
	
}