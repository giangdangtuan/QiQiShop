package com.app85soft.qiqishop.services.address;

import com.app85soft.qiqishop.dto.response.address.AddressRes;
import com.app85soft.qiqishop.dto.response.address.DivisionRes;

import java.util.List;

public interface DivisionService {
    List<DivisionRes> getDivisions(Integer parentId);

}
