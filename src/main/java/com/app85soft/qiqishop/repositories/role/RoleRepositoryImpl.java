package com.app85soft.qiqishop.repositories.role;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.constant.RoleType;
import com.app85soft.qiqishop.dto.response.permission.PermissionRes;
import com.app85soft.qiqishop.dto.response.role.RoleDetail;
import com.app85soft.qiqishop.dto.response.role.RoleRes;
import com.app85soft.qiqishop.entities.role.QRole;
import com.app85soft.qiqishop.entities.role.Role;
import com.app85soft.qiqishop.entities.role.constant.PermissionKey;
import com.app85soft.qiqishop.entities.role.constant.PermissionType;
import com.app85soft.qiqishop.entities.role.permisstion.QPermission;
import com.app85soft.qiqishop.entities.role.role_permission.QRolePermission;
import com.app85soft.qiqishop.repositories.BaseRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.app85soft.qiqishop.util.Constants.PAGE_SIZE;


@Repository
public class RoleRepositoryImpl extends BaseRepository implements RoleRepositoryCustom {
    private final QRole qRole = QRole.role;
    private final QRolePermission qRolePermission = QRolePermission.rolePermission;
    private final QPermission qPermission = QPermission.permission1;

    @Override
    public List<Integer> getAllIdToCheckExist(Role role, List<Integer> ids) {

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qRole.id.in(ids));
        builder.and(qRole.objectId.eq(role.getObjectId()));
        builder.and(qRole.type.eq(role.getType()));
        builder.and(qRole.deleted.eq(false));

        return query().from(qRole)
                .where(builder)
                .select(qRole.id)
                .fetch();
    }

    @Override
    @Transactional
    public void deleteRoleByIds(List<Integer> ids) {

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qRole.id.in(ids));
        builder.and(qRole.deleted.eq(false));


        query().update(qRole)
                .where(builder)
                .set(qRole.deleted, true)
                .execute();
    }

    @Override
    public long countRoles(Role role, ActiveStatus status, String name) {

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qRole.objectId.eq(role.getObjectId()));
        builder.and(qRole.type.eq(role.getType()));
        if (status != null) {
            builder.and(qRole.status.eq(status));
        }
        builder.and(qRole.deleted.eq(false));
        if (!StringUtils.isEmpty(name)) {
            builder.and(qRole.name.contains(name));
        }

        Long countRole = query().from(qRole)
                .select(qRole.id.count())
                .where(builder)
                .fetchOne();
        return countRole != null ? countRole : 0;
    }

    @Override
    public List<RoleRes> getRoles(int page, Role role, ActiveStatus status, String name) {

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qRole.objectId.eq(role.getObjectId()));
        builder.and(qRole.type.eq(role.getType()));
        if (status != null) {
            builder.and(qRole.status.eq(status));
        }
        builder.and(qRole.deleted.eq(false));
        if (!StringUtils.isEmpty(name)) {
            builder.and(qRole.name.contains(name));
        }
        return query().from(qRole)
                .where(builder)
                .select(Projections.fields(RoleRes.class,
                        qRole.id, qRole.objectId, qRole.name,
                        qRole.note, qRole.type, qRole.status))
                .offset(page * PAGE_SIZE).limit(PAGE_SIZE)
                .orderBy(qRole.id.desc())
                .fetch();
    }

    @Override
    public boolean existPermission(int roleId, PermissionType[] groups, PermissionKey key, RoleType roleType) {

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qRolePermission.roleId.eq(roleId));
        builder.and(qPermission.permission.in(groups));
        builder.and(qPermission.status.in(ActiveStatus.ACTIVE));
        if (key != null) {
            builder.and(switch (key) {
                case READ -> qRolePermission.isView.eq(true);
                case CREATE -> qRolePermission.isWrite.eq(true);
                case APPROVAL -> qRolePermission.isApproval.eq(true);
                case DECISION -> qRolePermission.isDecision.eq(true);
            });
        }
        if (roleType != null) {
            builder.andAnyOf(
                    qPermission.type.eq(RoleType.ADMIN),
                    qPermission.type.eq(roleType)
            );
        }

        Long count = query().from(qRolePermission)
                .innerJoin(qPermission).on(qRolePermission.permissionId.eq(qPermission.id))
                .where(builder)
                .select(qRolePermission.id.count())
                .fetchOne();
        return count != null && count > 0;
    }

    @Override
    public Role getRoleForAdmin(int roleId, Role role) {

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qRole.id.eq(roleId));
        builder.and(qRole.objectId.eq(role.getObjectId()));
        builder.and(qRole.type.eq(role.getType()));
        builder.and(qRole.deleted.eq(false));

        return query().from(qRole)
                .where(builder)
                .select(qRole)
                .fetchOne();
    }

    @Override
    public List<PermissionRes> getPermissions(int roleId, Role role) {

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qRole.objectId.eq(role.getObjectId()));
        builder.and(qRole.type.eq(role.getType()));
        builder.and(qRole.deleted.eq(false));
        builder.and(qPermission.status.eq(ActiveStatus.ACTIVE));
        builder.and(qPermission.deleted.eq(false));

        return query().from(qRole)
                .innerJoin(qRolePermission).on(qRole.id.eq(qRolePermission.roleId))
                .rightJoin(qPermission).on(qPermission.id.eq(qRolePermission.permissionId), qRolePermission.roleId.eq(roleId))
                .where(builder)
                .select(Projections.fields(PermissionRes.class,
                        qPermission.id, qPermission.title, qPermission.permission,
                        qPermission.type, qPermission.parentPermission,
                        qRolePermission.isView, qRolePermission.isApproval,
                        qRolePermission.isDecision, qRolePermission.isWrite))
                .fetch();
    }

    @Override
    public RoleDetail getRoleById(Integer roleId) {

        BooleanBuilder builder = new BooleanBuilder();
        if (roleId != null) {
            builder.and(qRole.id.eq(roleId));
        }
        builder.and(qRole.deleted.eq(false));
        return query().from(qRole)
                .where(builder)
                .select(Projections.fields(RoleDetail.class,
                        qRole.id.as("roleId"), qRole.objectId,
                        qRole.type.as("roleType"),
                        qRole.name.as("roleName")))
                .fetchOne();
    }
}
