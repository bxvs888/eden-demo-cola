/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.demo.domain.rbac.domainservice

import org.ylzl.eden.demo.domain.menu.entity.Menu
import org.ylzl.eden.demo.domain.menu.entity.MenuStatus
import org.ylzl.eden.demo.domain.menu.gateway.MenuGateway
import org.ylzl.eden.demo.domain.menu.valueobject.MenuPath
import org.ylzl.eden.demo.domain.permission.entity.Permission
import org.ylzl.eden.demo.domain.permission.entity.PermissionType
import org.ylzl.eden.demo.domain.permission.gateway.PermissionGateway
import org.ylzl.eden.demo.domain.permission.valueobject.PermissionCode
import org.ylzl.eden.demo.domain.role.entity.Role
import org.ylzl.eden.demo.domain.role.entity.RoleStatus
import org.ylzl.eden.demo.domain.role.gateway.RoleGateway
import org.ylzl.eden.demo.domain.role.valueobject.RoleCode
import org.ylzl.eden.demo.domain.role.valueobject.RoleName
import spock.lang.Specification

import java.time.LocalDateTime

class RbacDomainServiceTest extends Specification {

    def roleGateway = Mock(RoleGateway)
    def permissionGateway = Mock(PermissionGateway)
    def menuGateway = Mock(MenuGateway)
    def rbacDomainService = new RbacDomainService(roleGateway, permissionGateway, menuGateway)

    def "getPermissionCodes filters disabled roles and deduplicates codes"() {
        given:
        def now = LocalDateTime.now()
        def enabledRole = Role.reconstitute(1L, RoleCode.of('ADMIN'), RoleName.of('Admin'), 'enabled', RoleStatus.ENABLED, 1, now, now)
        def disabledRole = Role.reconstitute(2L, RoleCode.of('AUDITOR'), RoleName.of('Auditor'), 'disabled', RoleStatus.DISABLED, 2, now, now)
        def userRead = Permission.reconstitute(10L, PermissionCode.of('user:read'), 'Read user', PermissionType.BUTTON, 0L, 'read', 1, now, now)
        def userWrite = Permission.reconstitute(11L, PermissionCode.of('user:write'), 'Write user', PermissionType.BUTTON, 0L, 'write', 2, now, now)

        roleGateway.findByIds([1L, 2L]) >> [enabledRole, disabledRole]
        roleGateway.findPermissionIdsByRoleId(1L) >> [10L, 11L, 10L]
        permissionGateway.findByIds(_ as List<Long>) >> [userRead, userWrite]

        when:
        def codes = rbacDomainService.getPermissionCodes([1L, 2L])

        then:
        codes == ['user:read', 'user:write'] as Set
        0 * roleGateway.findPermissionIdsByRoleId(2L)
    }

    def "buildMenuTree sorts children under root menus"() {
        given:
        def now = LocalDateTime.now()
        def root = Menu.reconstitute(1L, 'System', MenuPath.of('/system'), 'icon-system', 0L, 1, MenuStatus.VISIBLE, 'SystemView', now, now)
        def childB = Menu.reconstitute(3L, 'Users', MenuPath.of('/system/users'), 'icon-users', 1L, 2, MenuStatus.VISIBLE, 'UserView', now, now)
        def childA = Menu.reconstitute(2L, 'Roles', MenuPath.of('/system/roles'), 'icon-roles', 1L, 1, MenuStatus.VISIBLE, 'RoleView', now, now)

        when:
        def tree = rbacDomainService.buildMenuTree([childB, root, childA])

        then:
        tree*.name == ['System']
        tree[0].children*.name == ['Roles', 'Users']
    }
}
