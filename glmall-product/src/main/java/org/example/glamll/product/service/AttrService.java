package org.example.glamll.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.common.utils.PageUtils;
import org.example.glamll.product.entity.AttrEntity;
import org.example.glamll.product.vo.AttrRespVO;
import org.example.glamll.product.vo.AttrVo;

import java.util.Map;

/**
 * 商品属性
 *
 * @author zxc
 * @email z2485861264@gmail.com
 * @date 2024-08-03 21:17:41
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attr);

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type);

    AttrRespVO getAttrInfo(Long attrId);

    void updateAttr(AttrVo attr);
}

