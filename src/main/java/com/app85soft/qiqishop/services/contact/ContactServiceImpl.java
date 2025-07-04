package com.app85soft.qiqishop.services.contact;

import com.app85soft.qiqishop.component.Translator;
import com.app85soft.qiqishop.dto.request.contact.AddContactReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.contact.ContactRes;
import com.app85soft.qiqishop.entities.contact.Contact;
import com.app85soft.qiqishop.entities.role.constant.PermissionKey;
import com.app85soft.qiqishop.entities.role.constant.PermissionType;
import com.app85soft.qiqishop.entities.user.User;
import com.app85soft.qiqishop.exceptions.BusinessException;
import com.app85soft.qiqishop.repositories.contact.ContactRepository;
import com.app85soft.qiqishop.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl extends BaseService implements ContactService {
    private final ContactRepository contactRepository;


    @Override
    public BaseResponse<List<ContactRes>> getContacts(String searchKeyword, int page) {
        User user = getUser(PermissionKey.READ, PermissionType.ACCOUNT);

        long count = contactRepository.countContact(searchKeyword);
        List<ContactRes> contacts = contactRepository.getContacts(searchKeyword, page);
        return new BaseResponse<>(contacts, count);
    }

    @Override
    public Contact addContact(AddContactReq req) {
        Contact newContact = new Contact();
        newContact.setName(req.getName());
        newContact.setEmail(req.getEmail());
        newContact.setContent(req.getContent());

        return contactRepository.save(newContact);
    }

    @Override
    public ContactRes getContactDetail(int id) {
        User user = getUser(PermissionKey.READ, PermissionType.ACCOUNT);
        ContactRes contact = contactRepository.getContactDetail(id);
        if (contact == null) {
            throw new BusinessException(Translator.toLocale("id_not_exist"), HttpStatus.BAD_REQUEST);
        }

        return contact;
    }
}
