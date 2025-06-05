package com.app85soft.qiqishop.repositories.user;

import java.util.List;

import com.app85soft.qiqishop.dto.response.file.UploadFileRes;
import com.app85soft.qiqishop.entities.upload_file.QUploadFile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.response.role.RoleDetail;
import com.app85soft.qiqishop.dto.response.user.UserDetailRes;
import com.app85soft.qiqishop.dto.response.user.UserListRes;
import com.app85soft.qiqishop.entities.role.QRole;
import com.app85soft.qiqishop.entities.role.Role;
import com.app85soft.qiqishop.entities.user.QUser;
import com.app85soft.qiqishop.entities.user.User;
import com.app85soft.qiqishop.repositories.BaseRepository;
import static com.app85soft.qiqishop.util.Constants.PAGE_SIZE;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class UserRepositoryImpl extends BaseRepository implements UserRepositoryCustom {
        private final QUser qUser = QUser.user;
        private final QRole qRole = QRole.role;
        private final QUploadFile qUploadFile = QUploadFile.uploadFile;

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
        public long countUser(ActiveStatus status, String searchKeyword) {

                BooleanBuilder builder = new BooleanBuilder();
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
                                .where(builder)
                                .select(qUser.id.count())
                                .fetchOne();
                return count == null ? 0 : count;
        }

        @Override
        public List<UserListRes> getUsers(ActiveStatus status, String searchKeyword, int page) {

                BooleanBuilder builder = new BooleanBuilder();
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
                                .where(builder)
                                .select(Projections.fields(UserListRes.class,
                                                qUser.id, qUser.code, qUser.name, qUser.email,
                                                qUser.phone, qUser.status,
                                                qUser.birthday, qUser.gender,
                                                Projections.fields(RoleDetail.class,
                                                                qRole.id.as("roleId"),
                                                                qRole.type.as("roleType"),
                                                                qRole.name.as("roleName"))
                                                                .as("role")))
                                .offset(page * PAGE_SIZE).limit(PAGE_SIZE)
                                .orderBy(qUser.id.desc())
                                .fetch();
        }

        @Override
        public UserDetailRes getProfileUser(int accountId) {

                BooleanBuilder builder = new BooleanBuilder();
                builder.and(qUser.id.eq(accountId));
                builder.and(qUser.deleted.eq(false));

                return query().from(qUser)
                                .leftJoin(qUploadFile).on(qUploadFile.id.eq(qUser.avatarId)
                                        .and(qUploadFile.deleted.eq(false)))
                                .where(builder)
                                .select(Projections.fields(UserDetailRes.class,
                                                qUser.id, qUser.code, qUser.phone, qUser.name,
                                                qUser.email, qUser.birthday, qUser.gender,
                                                qUser.status, qUser.avatarId,
                                                Projections.fields(RoleDetail.class,
                                                                qRole.id.as("roleId"),
                                                                qRole.type.as("roleType"),
                                                                qRole.name.as("roleName"))
                                                                .as("role"),
                                                Projections.fields(UploadFileRes.class,
                                                                qUploadFile.originUrl,
                                                                qUploadFile.thumbUrl)
                                                                .as("avatar")
                                ))
                                .fetchOne();
        }

        @Override
        public List<Integer> getAllIdToCheckExist(List<Integer> userIds) {

                BooleanBuilder builder = new BooleanBuilder();
                builder.and(qUser.id.in(userIds));
                builder.and(qUser.deleted.eq(false));

                return query().from(qUser)
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
        public User getUserToUpdate(int userId) {

                BooleanBuilder builder = new BooleanBuilder();
                builder.and(qUser.id.eq(userId));
                builder.and(qUser.deleted.eq(false));
                return query().from(qUser)
                                .innerJoin(qRole).on(qRole.id.eq(qUser.roleId))
                                .where(builder)
                                .select(qUser)
                                .fetchOne();
        }

}
