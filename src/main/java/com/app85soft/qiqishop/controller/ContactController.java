package com.app85soft.qiqishop.controller;

import com.app85soft.qiqishop.dto.request.category.AddCategoryReq;
import com.app85soft.qiqishop.dto.request.contact.AddContactReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.category.CategoryListRes;
import com.app85soft.qiqishop.dto.response.contact.ContactRes;
import com.app85soft.qiqishop.services.contact.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
@Slf4j
public class ContactController {
    private final ContactService contactService;

    @Operation(summary = "Add new contact")
    @PostMapping("contact/add")
    public ResponseEntity<BaseResponse<?>> addContact(@RequestBody @Valid AddContactReq req) {
        return ResponseEntity.ok(new BaseResponse<>(contactService.addContact(req)));
    }

    @Operation(summary = "Get list contact.")
    @GetMapping("v1/contact/list")
    public ResponseEntity<BaseResponse<List<ContactRes>>> getContacts(@RequestParam int page,
                                                                      @Parameter(description = "[name, email]")
                                                                      @RequestParam(required = false) String searchKeyword) {
        return ResponseEntity.ok(contactService.getContacts(searchKeyword, page));
    }

    @Operation(summary = "Get contact detail.")
    @GetMapping("v1/contact/detail/{id}")
    public ResponseEntity<BaseResponse<ContactRes>> getContactDetail(@PathVariable("id") int id) {
        return ResponseEntity.ok(new BaseResponse<>(contactService.getContactDetail(id)));
    }
}
