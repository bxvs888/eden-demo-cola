package org.ylzl.eden.demo.domain.user.domainservice

import org.ylzl.eden.demo.domain.user.ability.UserAbility
import org.ylzl.eden.demo.domain.user.entity.User
import org.ylzl.eden.demo.domain.user.gateway.UserGateway
import spock.lang.Specification

class UserDomainServiceTest extends Specification {

    def userGateway = Mock(UserGateway)
    def userAbility = Mock(UserAbility)
    def userDomainService = new UserDomainService(userGateway, userAbility)

    def "registerUser invokes extension points during registration"() {
        given:
        userAbility.validatePasswordStrength('Abcd1234') >> true
        userGateway.existsByLogin(_) >> false
        userGateway.existsByEmail(_) >> false

        when:
        User user = userDomainService.registerUser('demoUser', 'demo@example.com', 'Abcd1234')

        then:
        user != null
        1 * userAbility.validateRegister(_ as User)
        1 * userAbility.afterRegister(_ as User)
    }

    def "registerUser rejects password when extension strategy fails"() {
        given:
        userAbility.validatePasswordStrength('Abcd1234') >> false

        when:
        userDomainService.registerUser('demoUser', 'demo@example.com', 'Abcd1234')

        then:
        thrown(Exception)
        0 * userGateway._
        0 * userAbility.validateRegister(_)
        0 * userAbility.afterRegister(_)
    }
}
