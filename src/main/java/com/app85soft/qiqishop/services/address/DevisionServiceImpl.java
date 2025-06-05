package com.app85soft.qiqishop.services.address;

import com.app85soft.qiqishop.dto.response.address.DivisionRes;
import com.app85soft.qiqishop.entities.address.District;
import com.app85soft.qiqishop.entities.address.Ward;
import com.app85soft.qiqishop.repositories.address.DistrictRepository;
import com.app85soft.qiqishop.repositories.address.ProvinceRepository;
import com.app85soft.qiqishop.repositories.address.WardRepository;
import com.app85soft.qiqishop.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DevisionServiceImpl extends BaseService implements DivisionService {
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;

    @Override
    public List<DivisionRes> getDivisions(Integer parentId, int level) {
        if(level == 1) {
            if (parentId == null || parentId == 0) {
                return provinceRepository.findAll().stream()
                        .map(p -> DivisionRes.builder()
                                .id(p.getId())
                                .ghnId(p.getGhnId())
                                .code(p.getCode())
                                .name(p.getName())
                                .build())
                        .sorted(Comparator.comparing(DivisionRes::getName, String.CASE_INSENSITIVE_ORDER))
                        .toList();
            }
        }

        if(level == 2) {
            List<District> districts = districtRepository.findAllByProvinceId(parentId);
            if (!districts.isEmpty()) {
                return districts.stream()
                        .map(d -> DivisionRes.builder()
                                .id(d.getId())
                                .ghnId(d.getGhnId())
                                .code(d.getCode())
                                .name(d.getName())
                                .build())
                        .sorted(Comparator.comparing(DivisionRes::getName, String.CASE_INSENSITIVE_ORDER))
                        .toList();
            }
        }

        if(level == 3) {
            List<Ward> wards = wardRepository.findAllByDistrictId(parentId);
            return wards.stream()
                    .map(w -> DivisionRes.builder()
                            .id(w.getId())
                            .code(w.getCode())
                            .name(w.getName())
                            .build())
                    .sorted(Comparator.comparing(DivisionRes::getName, String.CASE_INSENSITIVE_ORDER))
                    .toList();
        }
        return List.of();
    }
}
