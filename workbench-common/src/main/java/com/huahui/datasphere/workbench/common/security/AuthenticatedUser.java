/*
 * Apache License
 * 
 * Copyright (c) 2020 HuahuiData
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.huahui.datasphere.workbench.common.security;

import java.util.Collection;

import org.acumos.cds.domain.MLPUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthenticatedUser implements UserDetails {
	
	private static final long serialVersionUID = 8892331347624397159L;
	private MLPUser user;
	
	/**
	 * Default constructor.
	 * @param user an authenticated user object (principal)
	 */
	public AuthenticatedUser(MLPUser user) {
		assert user != null;
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override	
	public String getPassword() {
		return user.getLoginHash();
	}

	@Override
	public String getUsername() {
		return user.getLoginName();
	}

	@Override
	public boolean isAccountNonExpired() {
			return user.isActive();
	}

	@Override
	public boolean isAccountNonLocked() {
		return user.isActive();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return user.isActive();
	}
}
