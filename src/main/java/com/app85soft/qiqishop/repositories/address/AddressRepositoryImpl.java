package com.app85soft.qiqishop.repositories.address;

import com.app85soft.qiqishop.dto.response.address.AddressRes;
import com.app85soft.qiqishop.dto.response.category.CategoryListRes;
import com.app85soft.qiqishop.entities.address.*;
import com.app85soft.qiqishop.repositories.BaseRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Repository
public class AddressRepositoryImpl extends BaseRepository implements AddressRepositoryCustom {
    private final QAddress qAddress = QAddress.address;
    private final QProvince qProvince = QProvince.province;
    private final QDistrict qDistrict = QDistrict.district;
    private final QWard qWard = QWard.ward;

    @Override
    public Address getAddressToUpdate(int addressId, int userId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qAddress.id.eq(addressId));
        builder.and(qAddress.userId.eq(userId));
        builder.and(qAddress.deleted.eq(false));
        return query().from(qAddress)
                .where(builder)
                .select(qAddress)
                .fetchOne();
    }

    @Override
    public Address getAddressToUnsetDefault(int userId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qAddress.isDefault.eq(true));
        builder.and(qAddress.userId.eq(userId));
        builder.and(qAddress.deleted.eq(false));
        return query().from(qAddress)
                .where(builder)
                .select(qAddress)
                .fetchOne();
    }

    @Override
    public List<Address> findByUserIdAndDeletedFalse(int userId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qAddress.userId.eq(userId));
        builder.and(qAddress.deleted.eq(false));

        return query().select(qAddress)
                .from(qAddress)
                .where(builder)
                .fetch();
    }

    @Override
    public List<AddressRes> getAddresses(int userId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qAddress.userId.eq(userId));
        builder.and(qAddress.deleted.eq(false));

        return query().from(qAddress)
                .leftJoin(qProvince).on(qAddress.provinceId.eq(qProvince.id))
                .leftJoin(qDistrict).on(qAddress.districtId.eq(qDistrict.id))
                .leftJoin(qWard).on(qAddress.wardId.eq(qWard.id))
                .where(builder)
                .select(Projections.fields(AddressRes.class,
                                qAddress.id,
                                qAddress.userId,
                                qAddress.consignee,
                                qAddress.phone,
                                qProvince.id.as("provinceId"),
                                qProvince.name.as("provinceName"),
                                qDistrict.id.as("districtId"),
                                qDistrict.name.as("districtName"),
                                qWard.id.as("wardId"),
                                qWard.name.as("wardName"),
                                qAddress.detailAddress,
                                qAddress.isDefault
                        )
                )
                .fetch();
    }

    @Override
    public AddressRes getAddress(int addressId, int userId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qAddress.userId.eq(userId));
        builder.and(qAddress.id.eq(addressId));
        builder.and(qAddress.deleted.eq(false));

        return query().from(qAddress)
                .leftJoin(qProvince).on(qAddress.provinceId.eq(qProvince.id))
                .leftJoin(qDistrict).on(qAddress.districtId.eq(qDistrict.id))
                .leftJoin(qWard).on(qAddress.wardId.eq(qWard.id))
                .where(builder)
                .select(Projections.fields(AddressRes.class,
                                qAddress.id,
                                qAddress.userId,
                                qAddress.consignee,
                                qAddress.phone,
                                qProvince.name.as("provinceName"),
                                qProvince.ghnId.as("provinceGhnId"),
                                qDistrict.name.as("districtName"),
                                qDistrict.ghnId.as("districtGhnId"),
                                qWard.name.as("wardName"),
                                qWard.code.as("wardGhnCode"),
                                qAddress.detailAddress,
                                qAddress.isDefault
                        )
                )
                .fetchOne();
    }

    @Override
    public AddressRes getDefaultAddress(int userId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qAddress.userId.eq(userId));
        builder.and(qAddress.isDefault.eq(true));
        builder.and(qAddress.deleted.eq(false));

        return query().from(qAddress)
                .leftJoin(qProvince).on(qAddress.provinceId.eq(qProvince.id))
                .leftJoin(qDistrict).on(qAddress.districtId.eq(qDistrict.id))
                .leftJoin(qWard).on(qAddress.wardId.eq(qWard.id))
                .where(builder)
                .select(Projections.fields(AddressRes.class,
                                qAddress.id,
                                qAddress.userId,
                                qAddress.consignee,
                                qAddress.phone,
                                qProvince.name.as("provinceName"),
                                qProvince.ghnId.as("provinceGhnId"),
                                qDistrict.name.as("districtName"),
                                qDistrict.ghnId.as("districtGhnId"),
                                qWard.name.as("wardName"),
                                qWard.code.as("wardGhnCode"),
                                qAddress.detailAddress,
                                qAddress.isDefault
                        )
                )
                .fetchOne();
    }

    @Override
    @Transactional
    public void deleteAddresses(List<Integer> addressIds) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qAddress.id.in(addressIds));
        builder.and(qAddress.deleted.eq(false));

        query().update(qAddress)
                .set(qAddress.deleted, true)
                .where(builder)
                .execute();
    }

    @Override
    public List<Integer> getAllIdToCheckExist(List<Integer> addressIds, int userId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qAddress.id.in(addressIds));
        builder.and(qAddress.userId.eq(userId));
        builder.and(qAddress.deleted.eq(false));

        return query().from(qAddress)
                .where(builder)
                .select(qAddress.id)
                .fetch();
    }
}
