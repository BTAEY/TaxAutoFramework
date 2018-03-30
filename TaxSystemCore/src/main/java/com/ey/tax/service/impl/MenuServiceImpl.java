package com.ey.tax.service.impl;

import com.ey.tax.core.dao.MenuDAO;
import com.ey.tax.model.MenuModel;
import com.ey.tax.service.IMenuService;
import com.ey.tax.vo.MenuVo;
import org.apache.commons.collections.CollectionUtils;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveAction;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements IMenuService {
    private static final Integer TOP_LEVEL_MENU=1;

    @Autowired
    private MenuDAO menuDAO;

    @Autowired
    private Mapper mapper;

    @Override
    public List<MenuVo> loadMenus(Long userId) {
        List<MenuModel> menuModels = menuDAO.findMenuItemsByUserId(userId);

        Map<Integer, List<MenuModel>> menuListGroupByLevel = menuModels.stream().collect(Collectors.groupingBy(MenuModel::getMenuLevel));

        Map<Integer, List<MenuModel>> sortedMenuListMap = new LinkedHashMap<>();
        menuListGroupByLevel.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEachOrdered(e -> sortedMenuListMap.put(e.getKey(), e.getValue()));

        Map<Long,MenuVo> parentMap = new HashMap<>();
        recursiveLoadMenuList(sortedMenuListMap,parentMap,TOP_LEVEL_MENU);
        List<MenuVo> result = parentMap.values().stream().collect(Collectors.toList());

        return result;
    }

    private void recursiveLoadMenuList(Map<Integer, List<MenuModel>> sortedMenuListMap,Map<Long,MenuVo> parentMap,Integer index) {
        List<MenuModel> topLevelMenuList = sortedMenuListMap.get(index);
        if(CollectionUtils.isNotEmpty(topLevelMenuList)){
            Map<Long,MenuVo> nextLevelMenuMap = new LinkedHashMap<>();
            for(MenuModel menuModel:topLevelMenuList){
                MenuVo menuVo = mapper.map(menuModel,MenuVo.class);
                nextLevelMenuMap.put(menuModel.getId(),menuVo);
                Long parentId = menuModel.getPid();
                if(parentId != null){
                    MenuVo parentMenuVo = parentMap.get(parentId);
                    parentMenuVo.setHasChild(true);
                    parentMenuVo.getChildren().add(menuVo);
                }else{
                    parentMap.put(menuModel.getId(),menuVo);
                }
            }
            recursiveLoadMenuList(sortedMenuListMap,nextLevelMenuMap,++index);
        }else{
            return ;
        }
    }
}
