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

package org.ylzl.eden.demo.app.user.executor.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ylzl.eden.cola.dto.MultiResponse;
import org.ylzl.eden.demo.client.user.dto.query.UserPermissionsQry;
import org.ylzl.eden.demo.domain.rbac.domainservice.RbacDomainService;
import org.ylzl.eden.demo.domain.user.gateway.UserGateway;
import org.ylzl.eden.spring.framework.error.ClientAssert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 查询用户权限执行器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class UserPermissionsQryExe {

	private final UserGateway userGateway;
	private final RbacDomainService rbacDomainService;

	public MultiResponse<String> execute(UserPermissionsQry qry) {
		ClientAssert.isTrue(userGateway.findById(qry.getUserId()).isPresent(), "USER-404", "用户不存在");
		List<Long> roleIds = userGateway.findRoleIdsByUserId(qry.getUserId());
		if (roleIds == null || roleIds.isEmpty()) {
			return MultiResponse.of(Collections.emptyList());
		}
		return MultiResponse.of(new ArrayList<>(rbacDomainService.getPermissionCodes(roleIds)));
	}
}
