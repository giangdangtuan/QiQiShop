package com.app85soft.qiqishop.configuration;

import com.app85soft.qiqishop.repositories.address.ProvinceRepository;
import com.app85soft.qiqishop.services.address.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartupRunner implements ApplicationRunner {

    private final AddressService addressService;
    private final ProvinceRepository provinceRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (provinceRepository.count() == 0) {
            addressService.syncAll();
        }
    }
}
