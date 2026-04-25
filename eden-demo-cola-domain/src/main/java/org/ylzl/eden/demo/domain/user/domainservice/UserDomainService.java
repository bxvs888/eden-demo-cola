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

package org.ylzl.eden.demo.domain.user.domainservice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.ylzl.eden.demo.domain.user.ability.UserAbility;
import org.ylzl.eden.demo.domain.user.entity.User;
import org.ylzl.eden.demo.domain.user.gateway.UserGateway;
import org.ylzl.eden.demo.domain.user.valueobject.Email;
import org.ylzl.eden.demo.domain.user.valueobject.Login;
import org.ylzl.eden.demo.domain.user.valueobject.Password;
import org.ylzl.eden.spring.framework.error.ClientAssert;

@RequiredArgsConstructor
@Service
public class UserDomainService {

	private final UserGateway userGateway;
	private final UserAbility userAbility;

	public User registerUser(String loginStr, String emailStr, String passwordStr) {
		Login login = Login.of(loginStr);
		Email email = Email.of(emailStr);
		ClientAssert.isTrue(userAbility.validatePasswordStrength(passwordStr),
			"USER-REG-003", "Password rejected by extension strategy");
		Password password = Password.fromPlainText(passwordStr);

		ClientAssert.isTrue(!userGateway.existsByLogin(login),
			"USER-REG-001", "Login already exists");
		ClientAssert.isTrue(!userGateway.existsByEmail(email),
			"USER-REG-002", "Email already exists");

		User user = User.create(login, email, password);
		userAbility.validateRegister(user);
		userAbility.afterRegister(user);
		return user;
	}

	public User authenticate(String loginStr, String plainPassword) {
		Login login = Login.of(loginStr);

		User user = userGateway.findByLogin(login);
		ClientAssert.notNull(user, "AUTH-001", "User does not exist");
		ClientAssert.isTrue(user.canLogin(), "AUTH-002", "User status does not allow login");
		ClientAssert.isTrue(user.verifyPassword(plainPassword), "AUTH-003", "Password is incorrect");

		return user;
	}

	public void changeEmail(User user, String newEmailStr) {
		Email newEmail = Email.of(newEmailStr);

		ClientAssert.isTrue(!userGateway.existsByEmailExcludeUser(newEmail, user.getId()),
			"USER-EMAIL-001", "Email is already used by another user");

		user.changeEmail(newEmail);
	}
}