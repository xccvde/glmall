package org.example.glamll.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.common.utils.PageUtils;
import org.example.glamll.order.entity.OrderEntity;

import java.util.Map;

/**
 * 订单
 *
 * @author zxc
 * @email z2485861264@gmail.com
 * @date 2024-08-04 19:17:15
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

