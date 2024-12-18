package org.example.glamll.product.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.example.glamll.product.entity.AttrEntity;
import org.example.glamll.product.service.AttrService;
import org.example.glamll.product.vo.AttrGroupWithAttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.common.utils.PageUtils;
import org.example.common.utils.Query;

import org.example.glamll.product.dao.AttrGroupDao;
import org.example.glamll.product.entity.AttrGroupEntity;
import org.example.glamll.product.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        String key = (String) params.get("key");
        // select * from pms_attr_group where catelog_id = catelogId and (attr_group_id = key or attr_group_name like %key%)
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>();
        if (!StringUtils.isEmpty(key)){
            wrapper.and((obj)->{
                obj.eq("attr_group_id", key).or().like("attr_group_name", key);
            });
        }

        if(catelogId == 0) {
            IPage<AttrGroupEntity> page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    wrapper
            );
            return new PageUtils(page);
        }else{
            wrapper.eq("catelog_id", catelogId);
            IPage<AttrGroupEntity> page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    wrapper
            );
            return new PageUtils(page);
        }
    }

    @Override
    public List<AttrGroupWithAttrVo> getAttrGroupWithAttrsByCatelogId(Long catelogId) {
        List<AttrGroupEntity> list = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        List<AttrGroupWithAttrVo> collect = list.stream().map((item) -> {
            AttrGroupWithAttrVo vo = new AttrGroupWithAttrVo();
            BeanUtils.copyProperties(item, vo);
            List<AttrEntity> attrRelationAttr = attrService.getAttrRelationAttr(item.getAttrGroupId());
            vo.setAttrs(attrRelationAttr);
            return vo;
        }).collect(Collectors.toList());
        return collect;
    }

}