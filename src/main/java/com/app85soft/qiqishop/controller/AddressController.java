package com.app85soft.qiqishop.controller;

import com.app85soft.qiqishop.dto.request.IdsRequest;
import com.app85soft.qiqishop.dto.request.address.AddAddressReq;
import com.app85soft.qiqishop.dto.request.address.UpdateAddressReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.address.AddressRes;
import com.app85soft.qiqishop.dto.response.address.DivisionRes;
import com.app85soft.qiqishop.dto.response.cart.CartRes;
import com.app85soft.qiqishop.services.address.AddressService;
import com.app85soft.qiqishop.services.address.DivisionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;
    private final DivisionService divisionService;

    @PostMapping("/sync")
    public ResponseEntity<String> sync() {
        addressService.syncAll();
        return ResponseEntity.ok("Đồng bộ dữ liệu GHN thành công!");
    }

    @Operation(summary = "Add new address")
    @PostMapping("v1/address/add")
    public ResponseEntity<BaseResponse<?>> addAddress(@RequestBody @Valid AddAddressReq req) {
        return ResponseEntity.ok(new BaseResponse<>(addressService.addAddress(req)));
    }

    @Operation(summary = "Update address")
    @PostMapping("v1/address/update")
    public ResponseEntity<BaseResponse<List<AddressRes>>> updateAddress(@RequestBody @Valid UpdateAddressReq request) {
        return ResponseEntity.ok(new BaseResponse<>(addressService.updateAddress(request)));
    }

    @Operation(summary = "Get list address.")
    @GetMapping("v1/address/list")
    public ResponseEntity<BaseResponse<List<AddressRes>>> getAddresses() {
        return ResponseEntity.ok(new BaseResponse<>(addressService.getAddresses()));
    }

    @Operation(summary = "Delete address.")
    @PostMapping("v1/address/delete")
    public ResponseEntity<BaseResponse<List<AddressRes>>> deleteAddresses(@RequestBody @Valid IdsRequest request) {
        return ResponseEntity.ok(new BaseResponse<>(addressService.deleteAddresses(request)));
    }

    @Operation(summary = "Get list division.")
    @GetMapping("v1/division/list")
    public ResponseEntity<BaseResponse<List<DivisionRes>>> getDivisions(@RequestParam(required = false) Integer parentId) {
        return ResponseEntity.ok(new BaseResponse<>(divisionService.getDivisions(parentId)));
    }
}
