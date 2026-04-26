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

package org.ylzl.eden.demo.app.user.executor.command

import org.springframework.context.ApplicationEventPublisher
import org.ylzl.eden.cola.dto.Response
import org.ylzl.eden.demo.client.user.dto.command.UserAddCmd
import org.ylzl.eden.demo.domain.user.domainservice.UserDomainService
import org.ylzl.eden.demo.domain.user.entity.User
import org.ylzl.eden.demo.domain.user.gateway.UserGateway
import org.ylzl.eden.demo.domain.user.valueobject.Email
import org.ylzl.eden.demo.domain.user.valueobject.Login
import org.ylzl.eden.demo.domain.user.valueobject.Password
import spock.lang.Specification

import java.time.LocalDateTime

class UserAddCmdExeTest extends Specification {

    def userDomainService = Mock(UserDomainService)
    def userGateway = Mock(UserGateway)
    def eventPublisher = Mock(ApplicationEventPublisher)
    def userAddCmdExe = new UserAddCmdExe(userDomainService, userGateway, eventPublisher)

    def "execute saves user and publishes domain events"() {
        given:
        def user = User.reconstitute(1L, Login.of('demoUser'), Email.of('demo@example.com'), Password.fromEncrypted('hashed-password'), User.UserStatus.ACTIVE, LocalDateTime.now(), LocalDateTime.now())
        userDomainService.registerUser('demoUser', 'demo@example.com', 'Abcd1234') >> user

        when:
        Response result = userAddCmdExe.execute(new UserAddCmd('demoUser', 'Abcd1234', 'demo@example.com'))

        then:
        result.success
        1 * userGateway.save(user)
    }
}
