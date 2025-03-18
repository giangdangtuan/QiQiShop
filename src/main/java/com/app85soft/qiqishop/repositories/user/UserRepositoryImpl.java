package com.app85soft.qiqishop.repositories.user;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.response.role.RoleDetail;
import com.app85soft.qiqishop.dto.response.user.UserDetailRes;
import com.app85soft.qiqishop.dto.response.user.UserListRes;
import com.app85soft.qiqishop.entities.role.QRole;
import com.app85soft.qiqishop.entities.role.Role;
import com.app85soft.qiqishop.entities.user.QUser;
import com.app85soft.qiqishop.entities.user.User;
import com.app85soft.qiqishop.repositories.BaseRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.app85soft.qiqishop.util.Constants.PAGE_SIZE;

@Slf4j
@Repository
public class UserRepositoryImpl extends BaseRepository implements UserRepositoryCustom {
    private final QUser qUser = QUser.user;
    private final QRole qRole = QRole.role;

    @Override
    public User loginByPhone(String phone) {

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qUser.phone.eq(phone));
        builder.and(qUser.deleted.eq(false));

        return query().from(qUser)
                .where(builder)
                .select(qUser)
                .fetchOne();
    }

    @Override
    public User getUserByPhone(String phone) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qUser.phone.eq(phone));
        builder.and(qUser.deleted.eq(false));

        return query().from(qUser)
                .where(builder)
                .select(qUser)
                .fetchOne();
    }

    @Override
    public boolean existsByCode(String code) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(qUser.code.eq(code));
        builder.and(qUser.deleted.eq(false));

        return query().from(qUser)
                .where(builder)
                .select(qUser.id)
                .fetchFirst() != null;
    }

    @Override
    public long countUser(ActiveStatus status, String searchKeyword, Role role) {

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qRole.objectId.eq(role.getObjectId()));
        builder.and(qRole.type.eq(role.getType()));
        if (status != null) {
            builder.and(qUser.status.eq(status));
        }
        builder.and(qUser.deleted.eq(false));
        if (searchKeyword != null) {
            builder.andAnyOf(
                    qUser.name.contains(searchKeyword),
                    qUser.email.contains(searchKeyword));
        }
        Long count = query().from(qUser)
                .innerJoin(qRole).on(qRole.id.eq(qUser.roleId))
                .where(builder)
                .select(qUser.id.count())
                .fetchOne();
        return count == null ? 0 : count;
    }

    @Override
    public List<UserListRes> getUsers(ActiveStatus status, String searchKeyword, int page, Role role) {

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qRole.objectId.eq(role.getObjectId()));
        builder.and(qRole.type.eq(role.getType()));
        if (status != null) {
            builder.and(qUser.status.eq(status));
        }
        builder.and(qUser.deleted.eq(false));
        if (searchKeyword != null) {
            builder.andAnyOf(
                    qUser.name.contains(searchKeyword),
                    qUser.email.contains(searchKeyword));
        }
        return query().from(qUser)
                .innerJoin(qRole).on(qRole.id.eq(qUser.roleId))
                .where(builder)
                .select(Projections.fields(UserListRes.class,
                        qUser.id, qUser.code, qUser.name, qUser.email,
                        qUser.address, qUser.phone, qUser.status,
                        qUser.birthday, qUser.gender,
                        Projections.fields(RoleDetail.class,
                                        qRole.id.as("roleId"),
                                        qRole.objectId,
                                        qRole.type.as("roleType"),
                                        qRole.name.as("roleName"))
                                .as("role"))
                )
                .offset(page * PAGE_SIZE).limit(PAGE_SIZE)
                .orderBy(qUser.id.desc())
                .fetch();
    }

    @Override
    public UserDetailRes getProfileUser(int accountId, Role role) {

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qUser.id.eq(accountId));
        builder.and(qRole.objectId.eq(role.getObjectId()));
        builder.and(qRole.type.eq(role.getType()));
        builder.and(qUser.deleted.eq(false));

        return query().from(qUser)
                .innerJoin(qRole).on(qRole.id.eq(qUser.roleId))
                .where(builder)
                .select(Projections.fields(UserDetailRes.class,
                        qUser.id, qUser.code, qUser.phone, qUser.name,
                        qUser.email, qUser.address, qUser.birthday, qUser.gender,
                        qUser.status,
                        Projections.fields(RoleDetail.class,
                                        qRole.id.as("roleId"),
                                        qRole.objectId,
                                        qRole.type.as("roleType"),
                                        qRole.name.as("roleName"))
                                .as("role"))
                )
                .fetchOne();
    }

    @Override
    public List<Integer> getAllIdToCheckExist(List<Integer> userIds, Role role) {

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qUser.id.in(userIds));
        builder.and(qRole.objectId.eq(role.getObjectId()));
        builder.and(qRole.type.eq(role.getType()));
        builder.and(qUser.deleted.eq(false));

        return query().from(qUser)
                .innerJoin(qRole).on(qRole.id.eq(qUser.roleId))
                .where(builder)
                .select(qUser.id)
                .fetch();
    }

    @Override
    @Transactional
    public void deleteUsers(List<Integer> userIds) {

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qUser.id.in(userIds));
        builder.and(qUser.deleted.eq(false));

        query().update(qUser)
                .set(qUser.deleted, true)
                .where(builder)
                .execute();
    }

    @Override
    public User getUserToUpdate(int userId, Role role) {

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qUser.id.eq(userId));
        builder.and(qRole.objectId.eq(role.getObjectId()));
        builder.and(qRole.type.eq(role.getType()));
        builder.and(qUser.deleted.eq(false));
        return query().from(qUser)
                .innerJoin(qRole).on(qRole.id.eq(qUser.roleId))
                .where(builder)
                .select(qUser)
                .fetchOne();
    }

}
