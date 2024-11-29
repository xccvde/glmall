package org.example.glamll.product.service.impl;

import org.apache.commons.lang.StringUtils;
import org.example.glamll.product.dao.AttrAttrgroupRelationDao;
import org.example.glamll.product.dao.AttrGroupDao;
import org.example.glamll.product.dao.CategoryDao;
import org.example.glamll.product.entity.AttrAttrgroupRelationEntity;
import org.example.glamll.product.entity.AttrGroupEntity;
import org.example.glamll.product.entity.CategoryEntity;
import org.example.glamll.product.service.CategoryService;
import org.example.glamll.product.vo.AttrRespVO;
import org.example.glamll.product.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.common.utils.PageUtils;
import org.example.common.utils.Query;

import org.example.glamll.product.dao.AttrDao;
import org.example.glamll.product.entity.AttrEntity;
import org.example.glamll.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    AttrAttrgroupRelationDao attrAttrgroupRelationDao;
    @Autowired
    AttrGroupDao attrGroupDao;
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        this.save(attrEntity);
        AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
        attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
        attrAttrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());
        attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
    }

    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId) {
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();

        if (catelogId != 0) {
            queryWrapper.eq("catelog_id", catelogId);
        }

        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.and((obj) -> {
                obj.eq("attr_id", key).or().like("attr_name", key);
            });
        }

        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                queryWrapper
        );

        PageUtils pageUtils = new PageUtils(page);

        List<AttrEntity> records = page.getRecords();
        List<AttrRespVO> respVOS = records.stream().map((attrEntity) -> {
            AttrRespVO attrRespVO = new AttrRespVO();
            BeanUtils.copyProperties(attrEntity, attrRespVO);

            AttrAttrgroupRelationEntity attrId = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
            if (attrId != null && attrId.getAttrGroupId() != null) {
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrId.getAttrGroupId());
                attrRespVO.setGroupName(attrGroupEntity.getAttrGroupName());
            }

            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            if (categoryEntity != null) {
                attrRespVO.setCatelogName(categoryEntity.getName());
            }
            return attrRespVO;
        }).collect(Collectors.toList());

        pageUtils.setList(respVOS);

        return pageUtils;
    }

    @Override
    public AttrRespVO getAttrInfo(Long attrId) {

        AttrEntity attrEntity = this.getById(attrId);
        AttrRespVO attrRespVO = new AttrRespVO();
        BeanUtils.copyProperties(attrEntity, attrRespVO);

        AttrAttrgroupRelationEntity relation = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
        if (relation !=null){
            attrRespVO.setAttrGroupId(relation.getAttrGroupId());
            AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(relation.getAttrGroupId());
            if (attrGroupEntity != null){
                attrRespVO.setGroupName(attrGroupEntity.getAttrGroupName());
            }
        }

        Long catelogId = attrEntity.getCatelogId();
        Long[] categoryPath = categoryService.findCategoryPath(catelogId);
        attrRespVO.setCatelogPath(categoryPath);
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        if (categoryEntity != null){
            attrRespVO.setCatelogName(categoryEntity.getName());
        }

        return attrRespVO;
    }

    @Transactional
    @Override
    public void updateAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        this.updateById(attrEntity);

        AttrAttrgroupRelationEntity relation = new AttrAttrgroupRelationEntity();
        relation.setAttrGroupId(attr.getAttrGroupId());
        relation.setAttrId(attr.getAttrId());

        Integer count = attrAttrgroupRelationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));
        if (count>0){
            attrAttrgroupRelationDao.update(relation, new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));
        }else {
            attrAttrgroupRelationDao.insert(relation);
        }
    }

}