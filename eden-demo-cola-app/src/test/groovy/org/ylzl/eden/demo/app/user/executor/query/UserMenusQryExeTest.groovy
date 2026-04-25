package org.ylzl.eden.demo.app.user.executor.query

import org.ylzl.eden.demo.app.menu.assembler.MenuAssembler
import org.ylzl.eden.demo.client.user.dto.query.UserMenusQry
import org.ylzl.eden.demo.domain.menu.entity.Menu
import org.ylzl.eden.demo.domain.menu.entity.MenuStatus
import org.ylzl.eden.demo.domain.menu.valueobject.MenuPath
import org.ylzl.eden.demo.domain.rbac.domainservice.RbacDomainService
import org.ylzl.eden.demo.domain.user.entity.User
import org.ylzl.eden.demo.domain.user.gateway.UserGateway
import spock.lang.Specification

import java.time.LocalDateTime

class UserMenusQryExeTest extends Specification {

    def userGateway = Mock(UserGateway)
    def rbacDomainService = Mock(RbacDomainService)
    def menuAssembler = Spy(MenuAssembler)
    def userMenusQryExe = new UserMenusQryExe(userGateway, rbacDomainService, menuAssembler)

    def "execute builds menu tree from rbac service result"() {
        given:
        def now = LocalDateTime.now()
        def rootMenu = Menu.reconstitute(1L, 'System', MenuPath.of('/system'), 'icon-system', 0L, 1, MenuStatus.VISIBLE, 'SystemView', now, now)
        def childMenu = Menu.reconstitute(2L, 'Users', MenuPath.of('/system/users'), 'icon-users', 1L, 1, MenuStatus.VISIBLE, 'UserView', now, now)
        def rootNode = new RbacDomainService.MenuTreeNode(id: 1L, name: 'System', path: '/system', icon: 'icon-system', component: 'SystemView', sort: 1)
        def childNode = new RbacDomainService.MenuTreeNode(id: 2L, name: 'Users', path: '/system/users', icon: 'icon-users', component: 'UserView', sort: 1)
        rootNode.children = [childNode]

        userGateway.findById(1L) >> Optional.of(User.reconstitute(1L, null, null, null, User.UserStatus.ACTIVE, now, now))
        userGateway.findRoleIdsByUserId(1L) >> [1L]
        rbacDomainService.getUserMenus([1L]) >> [rootMenu, childMenu]
        rbacDomainService.buildMenuTree([rootMenu, childMenu]) >> [rootNode]

        when:
        def response = userMenusQryExe.execute(new UserMenusQry(1L))

        then:
        response.data*.name == ['System']
        response.data[0].children*.name == ['Users']
    }
}
