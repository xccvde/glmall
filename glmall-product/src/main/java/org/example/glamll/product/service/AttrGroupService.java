package org.example.glamll.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.common.utils.PageUtils;
import org.example.glamll.product.entity.AttrGroupEntity;
import org.example.glamll.product.vo.AttrGroupWithAttrVo;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author zxc
 * @email z2485861264@gmail.com
 * @date 2024-08-03 21:17:41
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, Long catelogId);

    List<AttrGroupWithAttrVo> getAttrGroupWithAttrsByCatelogId(Long catelogId);
}

