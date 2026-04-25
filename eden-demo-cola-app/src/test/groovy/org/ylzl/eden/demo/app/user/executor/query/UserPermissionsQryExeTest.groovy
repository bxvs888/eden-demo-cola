package org.ylzl.eden.demo.app.user.executor.query

import org.ylzl.eden.demo.client.user.dto.query.UserPermissionsQry
import org.ylzl.eden.demo.domain.rbac.domainservice.RbacDomainService
import org.ylzl.eden.demo.domain.user.entity.User
import org.ylzl.eden.demo.domain.user.gateway.UserGateway
import spock.lang.Specification

import java.time.LocalDateTime

class UserPermissionsQryExeTest extends Specification {

    def userGateway = Mock(UserGateway)
    def rbacDomainService = Mock(RbacDomainService)
    def userPermissionsQryExe = new UserPermissionsQryExe(userGateway, rbacDomainService)

    def "execute gets permission codes from rbac service"() {
        given:
        userGateway.findById(1L) >> Optional.of(User.reconstitute(1L, null, null, null, User.UserStatus.ACTIVE, LocalDateTime.now(), LocalDateTime.now()))
        userGateway.findRoleIdsByUserId(1L) >> [1L, 2L]
        rbacDomainService.getPermissionCodes([1L, 2L]) >> (['user:read', 'user:write'] as Set)

        when:
        def response = userPermissionsQryExe.execute(new UserPermissionsQry(1L))

        then:
        response.data as Set == ['user:read', 'user:write'] as Set
    }
}
