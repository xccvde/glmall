package org.example.glamll.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.common.utils.PageUtils;
import org.example.glamll.coupon.entity.MemberPriceEntity;

import java.util.Map;

/**
 * 商品会员价格
 *
 * @author zxc
 * @email z2485861264@gmail.com
 * @date 2024-08-04 15:56:46
 */
public interface MemberPriceService extends IService<MemberPriceEntity> {

    PageUtils queryPage(Map<String, Object> params);
}
