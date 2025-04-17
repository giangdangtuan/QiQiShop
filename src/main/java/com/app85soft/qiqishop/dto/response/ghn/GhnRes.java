package com.app85soft.qiqishop.dto.response.ghn;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GhnRes<T> {
    int code;
    String message;
    T data;

    // Response Type Wrapper
    public static class ProvinceListResponse extends GhnRes<List<ProvinceRes>> {}
    public static class DistrictListResponse extends GhnRes<List<DistrictRes>> {}
    public static class WardListResponse extends GhnRes<List<WardRes>> {}
    public static class FeeResponse extends GhnRes<FeeData> {}
}
