package org.example.glamll.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.common.utils.PageUtils;
import org.example.glamll.ware.entity.PurchaseDetailEntity;

import java.util.Map;

/**
 * 
 *
 * @author zxc
 * @email z2485861264@gmail.com
 * @date 2024-08-04 18:59:25
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

