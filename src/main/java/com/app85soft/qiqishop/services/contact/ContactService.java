package com.app85soft.qiqishop.services.contact;

import com.app85soft.qiqishop.dto.request.contact.AddContactReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.contact.ContactRes;
import com.app85soft.qiqishop.entities.contact.Contact;

import java.util.List;

public interface ContactService {
    BaseResponse<List<ContactRes>> getContacts(String searchKeyword, int page);

    Contact addContact(AddContactReq req);

    ContactRes getContactDetail(int id);
}
