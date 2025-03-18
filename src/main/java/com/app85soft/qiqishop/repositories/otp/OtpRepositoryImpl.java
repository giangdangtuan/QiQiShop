package com.app85soft.qiqishop.repositories.otp;

import com.app85soft.qiqishop.dto.constant.VerifyStatus;
import com.app85soft.qiqishop.dto.request.otp.SendOtpReq;
import com.app85soft.qiqishop.entities.otp.Otp;
import com.app85soft.qiqishop.entities.otp.QOtp;
import com.app85soft.qiqishop.repositories.BaseRepository;
import com.querydsl.core.BooleanBuilder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class OtpRepositoryImpl extends BaseRepository implements OtpRepositoryCustom {
    private final QOtp qOtp = QOtp.otp1;

    @Override
    @Transactional
    public void updateStatusVerify(SendOtpReq request) {

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(qOtp.status.eq(VerifyStatus.VERIFY_PENDING));
        if (request.getEmail() != null) {
            builder.and(qOtp.email.eq(request.getEmail()));
        }
        if (request.getPhone() != null) {
            builder.and(qOtp.phone.eq(request.getPhone()));
        }

        query().update(qOtp)
                .where(builder)
                .set(qOtp.status, VerifyStatus.OTHER)
                .execute();

    }

    @Override
    public Otp findSessionAuth(String phone, String email, String otp) {

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qOtp.status.eq(VerifyStatus.VERIFY_PENDING));
        builder.and(qOtp.deleted.eq(false));
        builder.and(qOtp.phone.eq(phone));
        builder.and(qOtp.otp.eq(otp));
        builder.and(qOtp.email.eq(email));

        return query().from(qOtp)
                .where(builder)
                .select(qOtp)
                .fetchOne();
    }
}
