package org.example.glamll.coupon.dao;

import org.example.glamll.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author zxc
 * @email z2485861264@gmail.com
 * @date 2024-08-04 15:56:46
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}