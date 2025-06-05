package com.app85soft.qiqishop.external;

import com.app85soft.qiqishop.configuration.GhnConfig;

import com.app85soft.qiqishop.dto.request.ghn.GhnCreateOrderReq;
import com.app85soft.qiqishop.dto.response.ghn.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class GhnClient {
    private final GhnConfig ghnConfig;
    private final RestTemplate restTemplate;

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", ghnConfig.getToken());
        headers.set("ShopId", ghnConfig.getShopId());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public List<ProvinceRes> getProvinces() {
        String url = ghnConfig.getBaseUrl() + "/master-data/province";
        var res = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(buildHeaders()),
                GhnRes.ProvinceListResponse.class);
        return res.getBody().getData();
    }

    public List<DistrictRes> getDistricts(int provinceId) {
        String url = ghnConfig.getBaseUrl() + "/master-data/district";
        Map<String, Integer> body = Map.of("province_id", provinceId);
        var res = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body, buildHeaders()),
                GhnRes.DistrictListResponse.class);
        return res.getBody().getData();
    }

    public List<WardRes> getWards(int districtId) {
        String url = ghnConfig.getBaseUrl() + "/master-data/ward";
        Map<String, Integer> body = Map.of("district_id", districtId);
        var res = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body, buildHeaders()),
                GhnRes.WardListResponse.class);
        var bodyData = res.getBody();
        return bodyData != null && bodyData.getData() != null ? bodyData.getData() : List.of(); // tránh null
    }

    public Integer calculateShippingFee(Map<String, Object> body) {
        String url = ghnConfig.getBaseUrl() + "/v2/shipping-order/fee";
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, buildHeaders());
        var response = restTemplate.exchange(url, HttpMethod.POST, entity, GhnRes.FeeResponse.class);
        return response.getBody() != null && response.getBody().getData() != null
                ? response.getBody().getData().getTotal()
                : 0;
    }

    public GhnCreateOrderRes createShippingOrder(GhnCreateOrderReq body) {
        String url = ghnConfig.getBaseUrl() + "/v2/shipping-order/create";
        HttpEntity<GhnCreateOrderReq> entity = new HttpEntity<>(body, buildHeaders());
        ResponseEntity<GhnCreateOrderRes> response = restTemplate.exchange(url, HttpMethod.POST, entity, GhnCreateOrderRes.class);

        return response.getBody();
    }

}
