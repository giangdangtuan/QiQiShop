package com.app85soft.qiqishop.repositories.otp;

import com.app85soft.qiqishop.entities.otp.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpRepository extends JpaRepository<Otp, Integer>, OtpRepositoryCustom{
}
