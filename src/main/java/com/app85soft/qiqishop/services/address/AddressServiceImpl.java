package com.app85soft.qiqishop.services.address;

import com.app85soft.qiqishop.component.Translator;
import com.app85soft.qiqishop.dto.request.IdsRequest;
import com.app85soft.qiqishop.dto.request.address.AddAddressReq;
import com.app85soft.qiqishop.dto.request.address.SetDefaultAddressReq;
import com.app85soft.qiqishop.dto.request.address.UpdateAddressReq;
import com.app85soft.qiqishop.dto.response.address.AddressRes;
import com.app85soft.qiqishop.dto.response.ghn.DistrictRes;
import com.app85soft.qiqishop.dto.response.ghn.ProvinceRes;
import com.app85soft.qiqishop.dto.response.ghn.WardRes;
import com.app85soft.qiqishop.entities.address.Address;
import com.app85soft.qiqishop.entities.address.District;
import com.app85soft.qiqishop.entities.address.Province;
import com.app85soft.qiqishop.entities.address.Ward;
import com.app85soft.qiqishop.entities.user.User;
import com.app85soft.qiqishop.exceptions.BusinessException;
import com.app85soft.qiqishop.external.GhnClient;
import com.app85soft.qiqishop.repositories.address.AddressRepository;
import com.app85soft.qiqishop.repositories.address.DistrictRepository;
import com.app85soft.qiqishop.repositories.address.ProvinceRepository;
import com.app85soft.qiqishop.repositories.address.WardRepository;
import com.app85soft.qiqishop.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl extends BaseService implements AddressService {
    private final GhnClient ghnClient;
    private final AddressRepository addressRepo;
    private final ProvinceRepository provinceRepo;
    private final DistrictRepository districtRepo;
    private final WardRepository wardRepo;

    @Async
    @Transactional
    public void syncAll() {
        var provinces = ghnClient.getProvinces();
        ExecutorService executor = Executors.newFixedThreadPool(6);

        for (ProvinceRes p : provinces) {
            executor.submit(() -> {
                try {
                    Province province = provinceRepo.findByCode(p.getProvinceCode())
                            .map(existing -> {
                                existing.setGhnId(p.getProvinceId());
                                existing.setName(p.getProvinceName());
                                return existing;
                            }).orElseGet(() -> new Province(p.getProvinceId(), p.getProvinceCode(), p.getProvinceName()));
                    Province savedProvince = provinceRepo.save(province);

                    var districts = ghnClient.getDistricts(p.getProvinceId());
                    for (DistrictRes d : districts) {
                        District district = districtRepo.findByCode(d.getDistrictCode())
                                .map(existing -> {
                                    existing.setGhnId(d.getDistrictId());
                                    existing.setName(d.getDistrictName());
                                    existing.setProvinceId(savedProvince.getId());
                                    return existing;
                                }).orElseGet(() -> new District(d.getDistrictId(), d.getDistrictCode(), d.getDistrictName(), savedProvince.getId()));
                        District savedDistrict = districtRepo.save(district);

                        var wards = ghnClient.getWards(d.getDistrictId());
                        for (WardRes w : wards) {
                            Ward ward = wardRepo.findByCode(w.getWardCode())
                                    .map(existing -> {
                                        existing.setName(w.getWardName());
                                        existing.setDistrictId(savedDistrict.getId());
                                        return existing;
                                    }).orElseGet(() -> new Ward(w.getWardCode(), w.getWardName(), savedDistrict.getId()));
                            wardRepo.save(ward);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        executor.shutdown();
        while (!executor.isTerminated()) {}
    }

    @Override
    public AddressRes addAddress(AddAddressReq req) {
        User user = getUser();

        Province province = provinceRepo.findById(req.getProvinceId())
                .orElseThrow(() -> new BusinessException(Translator.toLocale("province_id_not_exist")));

        District district = districtRepo.findById(req.getDistrictId())
                .orElseThrow(() -> new BusinessException(Translator.toLocale("district_id_not_exist")));
        if(district.getProvinceId() != province.getId()) {
            throw new BusinessException(Translator.toLocale("district_not_belong_to_province"));
        }

        Ward ward = wardRepo.findById(req.getWardId())
                .orElseThrow(() -> new BusinessException(Translator.toLocale("ward_id_not_exist")));
        if(ward.getDistrictId() != district.getId()) {
            throw new BusinessException(Translator.toLocale("ward_not_belong_to_district"));
        }

        Address newAddress = new Address();
        newAddress.setUserId(user.getId());
        newAddress.setConsignee(req.getConsignee());
        newAddress.setPhone(req.getPhone());
        newAddress.setProvinceId(req.getProvinceId());
        newAddress.setDistrictId(req.getDistrictId());
        newAddress.setWardId(req.getWardId());
        newAddress.setDetailAddress(req.getDetailAddress());

        List<Address> allUserAddresses = addressRepo.findByUserIdAndDeletedFalse(user.getId());
        if (req.isDefault()) {
            allUserAddresses.forEach(a -> {
                if (a.isDefault()) {
                    a.setDefault(false);
                    addressRepo.save(a);
                }
            });
            newAddress.setDefault(true);
        } else {
            if(allUserAddresses.isEmpty()) {
                newAddress.setDefault(true);
            }
        }

        addressRepo.save(newAddress);

        return AddressRes.builder()
                .id(newAddress.getId())
                .userId(newAddress.getUserId())
                .consignee(newAddress.getConsignee())
                .phone(newAddress.getPhone())
                .provinceName(province.getName())
                .districtName(district.getName())
                .wardName(ward.getName())
                .detailAddress(newAddress.getDetailAddress())
                .isDefault(newAddress.isDefault())
                .build();
    }

    @Override
    public Address setDefaultAddress(SetDefaultAddressReq req) {
        User user = getUser();
        Address oldAddress = addressRepo.getAddressToUnsetDefault(user.getId());
        oldAddress.setDefault(false);
        addressRepo.save(oldAddress);
        Address currentAddress = addressRepo.getAddressToUpdate(req.getId(), user.getId());
        currentAddress.setDefault(true);
        addressRepo.save(currentAddress);
        return currentAddress;
    }

    @Override
    public List<AddressRes> updateAddress(UpdateAddressReq request) {
        User user = getUser();
        Address currentAddress = addressRepo.getAddressToUpdate(request.getId(), user.getId());
        if (currentAddress == null) {
            throw new BusinessException(Translator.toLocale("category_id_not_exist"));
        }
        if (request.getConsignee() != null && !request.getConsignee().isEmpty()) {
            currentAddress.setConsignee(request.getConsignee());
        }
        if (request.getPhone() != null && !request.getPhone().isEmpty()) {
            currentAddress.setPhone(request.getPhone());
        }
        if (request.getProvinceId() != null) {
            provinceRepo.findById(request.getProvinceId())
                    .orElseThrow(() -> new BusinessException(Translator.toLocale("model_id_not_exist")));
            currentAddress.setProvinceId(request.getProvinceId());
        }
        if (request.getDistrictId() != null) {
            District district = districtRepo.findById(request.getDistrictId())
                    .orElseThrow(() -> new BusinessException(Translator.toLocale("model_id_not_exist")));
            if(district.getProvinceId() != currentAddress.getProvinceId()) {
                throw new BusinessException(Translator.toLocale("district_not_belong_to_province"));
            }
            currentAddress.setDistrictId(request.getDistrictId());
        }
        if (request.getWardId() != null) {
            Ward ward = wardRepo.findById(request.getWardId())
                    .orElseThrow(() -> new BusinessException(Translator.toLocale("model_id_not_exist")));
            if(ward.getDistrictId() != currentAddress.getDistrictId()) {
                throw new BusinessException(Translator.toLocale("ward_not_belong_to_district"));
            }
            currentAddress.setWardId(request.getWardId());
        }
        if (request.getDetailAddress() != null && !request.getDetailAddress().isEmpty()) {
            currentAddress.setDetailAddress(request.getDetailAddress());
        }
        if (request.isDefault()) {
            List<Address> allUserAddresses = addressRepo.findByUserIdAndDeletedFalse(user.getId());
            allUserAddresses.forEach(a -> {
                if (a.isDefault()) {
                    a.setDefault(false);
                    addressRepo.save(a);
                }
            });
            currentAddress.setDefault(true);
        }
        addressRepo.save(currentAddress);
        List<Address> addresses = addressRepo.findByUserIdAndDeletedFalse(user.getId());

        List<AddressRes> result = addresses.stream()
                .map(addr -> {
                    Province p = provinceRepo.findById(addr.getProvinceId()).orElse(null);
                    District d = districtRepo.findById(addr.getDistrictId()).orElse(null);
                    Ward w = wardRepo.findById(addr.getWardId()).orElse(null);

                    return AddressRes.builder()
                            .id(addr.getId())
                            .userId(addr.getUserId())
                            .consignee(addr.getConsignee())
                            .phone(addr.getPhone())
                            .provinceName(p != null ? p.getName() : "")
                            .districtName(d != null ? d.getName() : "")
                            .wardName(w != null ? w.getName() : "")
                            .detailAddress(addr.getDetailAddress())
                            .isDefault(addr.isDefault())
                            .build();
                })
                .collect(Collectors.toList());

        return result;
    }

    @Override
    public List<AddressRes> getAddresses() {
        User user = getUser();
        List<AddressRes> addresses = addressRepo.getAddresses(user.getId());
        return addresses;
    }

    @Override
    public List<AddressRes> deleteAddresses(IdsRequest request) {
        User user = getUser();
        List<Integer> addressIds = request.getIds();
        List<Integer> existingIds = addressRepo.getAllIdToCheckExist(addressIds, user.getId());
        List<Integer> nonExistingIds = addressIds.stream().filter(id -> !existingIds.contains(id)).toList();
        if (!nonExistingIds.isEmpty()) {
            throw new BusinessException(Translator.toLocale("id_not_exist"), HttpStatus.BAD_REQUEST);
        }
        List<Address> addressesToDelete = addressRepo.findAllById(addressIds);
        boolean hasDefault = addressesToDelete.stream().anyMatch(Address::isDefault);
        if (hasDefault) {
            throw new BusinessException(Translator.toLocale("cannot_delete_default_address"), HttpStatus.BAD_REQUEST);
        }
        addressRepo.deleteAddresses(addressIds);
        List<AddressRes> addresses = addressRepo.getAddresses(user.getId());
        return addresses;
    }
}
