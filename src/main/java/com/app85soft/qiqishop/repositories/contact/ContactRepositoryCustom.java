package com.app85soft.qiqishop.repositories.contact;

import com.app85soft.qiqishop.dto.response.contact.ContactRes;

import java.util.List;

public interface ContactRepositoryCustom {
    long countContact(String searchKeyword);

    List<ContactRes> getContacts(String searchKeyword, int page);

    ContactRes getContactDetail(int id);
}
