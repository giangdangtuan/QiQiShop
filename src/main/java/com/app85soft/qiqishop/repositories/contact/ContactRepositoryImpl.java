package com.app85soft.qiqishop.repositories.contact;

import com.app85soft.qiqishop.dto.response.category.CategoryListRes;
import com.app85soft.qiqishop.dto.response.contact.ContactRes;
import com.app85soft.qiqishop.entities.contact.QContact;
import com.app85soft.qiqishop.repositories.BaseRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class ContactRepositoryImpl extends BaseRepository implements ContactRepositoryCustom {
    private final QContact qContact = QContact.contact;

    @Override
    public long countContact(String searchKeyword) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qContact.deleted.eq(false));
        if (searchKeyword != null) {
            builder.andAnyOf(
                    qContact.name.containsIgnoreCase(searchKeyword),
                    qContact.email.containsIgnoreCase(searchKeyword)
            );
        }
        Long count = query().from(qContact)
                .where(builder)
                .select(qContact.id.count())
                .fetchOne();
        return count == null ? 0 : count;
    }

    @Override
    public List<ContactRes> getContacts(String searchKeyword, int page) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qContact.deleted.eq(false));
        if (searchKeyword != null) {
            builder.andAnyOf(
                    qContact.name.containsIgnoreCase(searchKeyword),
                    qContact.email.containsIgnoreCase(searchKeyword)
            );
        }

        return query().from(qContact)
                .where(builder)
                .select(Projections.fields(ContactRes.class,
                        qContact.id,
                        qContact.name,
                        qContact.email,
                        qContact.content,
                        qContact.createdAt
                ))
                .fetch();
    }

    @Override
    public ContactRes getContactDetail(int id) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qContact.id.eq(id));
        builder.and(qContact.deleted.eq(false));

        ContactRes contact = query().from(qContact)
                .where(builder)
                .select(Projections.fields(ContactRes.class,
                        qContact.id,
                        qContact.name,
                        qContact.email,
                        qContact.content,
                        qContact.createdAt
                ))
                .fetchOne();

        return contact;
    }
}
