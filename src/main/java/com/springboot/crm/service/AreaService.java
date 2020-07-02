package com.springboot.crm.service;

import com.springboot.crm.entity.Area;

import java.util.List;

public interface AreaService {

    boolean insertArea(Area area);

    List<Area> getAllAreas();

    boolean editArea(Integer areaId, Area area);

    String deleteArea(String areaName, String areaCode);

    List<Area> getAreasByOperatorId(int operatorId);

    Area getAreaById(int areaId);

    List<Integer> getAreaIdsByAreas(List<Area> areas);

    Area getAreaByName(String area);
}
