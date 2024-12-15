package org.example.glamll.product.service.impl;

import org.apache.commons.lang.StringUtils;
import org.example.common.constant.ProductConstant;
import org.example.glamll.product.dao.AttrAttrgroupRelationDao;
import org.example.glamll.product.dao.AttrGroupDao;
import org.example.glamll.product.dao.CategoryDao;
import org.example.glamll.product.entity.AttrAttrgroupRelationEntity;
import org.example.glamll.product.entity.AttrGroupEntity;
import org.example.glamll.product.entity.CategoryEntity;
import org.example.glamll.product.service.CategoryService;
import org.example.glamll.product.vo.AttrGroupRelationVo;
import org.example.glamll.product.vo.AttrRespVO;
import org.example.glamll.product.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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

        //保存关联关系
        if (attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
            attrAttrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());
            attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
        }

    }

    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type) {
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<AttrEntity>().eq("attr_type", "base".equalsIgnoreCase(type) ? ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() : ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());

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

            if ("base".equalsIgnoreCase(type)){
                AttrAttrgroupRelationEntity attrId = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
                if (attrId != null && attrId.getAttrGroupId() != null) {
                    AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrId.getAttrGroupId());
                    attrRespVO.setGroupName(attrGroupEntity.getAttrGroupName());
                }
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

        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            AttrAttrgroupRelationEntity relation = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
            if (relation !=null){
                attrRespVO.setAttrGroupId(relation.getAttrGroupId());
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(relation.getAttrGroupId());
                if (attrGroupEntity != null){
                    attrRespVO.setGroupName(attrGroupEntity.getAttrGroupName());
                }
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
        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
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

    /**
     * 根据分组id查找关联的所有基本属性
     * @param attrgroupId
     * @return
     */
    @Override
    public List<AttrEntity> getAttrRelationAttr(Long attrgroupId) {
        List<AttrAttrgroupRelationEntity> relationEntities = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrgroupId));

        List<Long> attrIds = relationEntities.stream().map((relation) -> {
            return relation.getAttrId();
        }).collect(Collectors.toList());

        if (attrIds.isEmpty()){
            return Collections.emptyList();
        }

        Collection<AttrEntity> attrEntities = this.listByIds(attrIds);
        return (List<AttrEntity>) attrEntities;
    }

    @Override
    public void deleteRelation(List<AttrAttrgroupRelationEntity> entities) {
//        List<AttrAttrgroupRelationEntity> entities = Arrays.asList(vos).stream().map((item) -> {
//            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
//            BeanUtils.copyProperties(item, relationEntity);
//            return relationEntity;
//        }).collect(Collectors.toList());

        attrAttrgroupRelationDao.deleteBatchRelation(entities);
    }


}