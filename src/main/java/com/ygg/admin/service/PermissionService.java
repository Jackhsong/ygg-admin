package com.ygg.admin.service;

import org.apache.shiro.authz.Permission;

public interface PermissionService
{
    public Permission createPermission(Permission permission);
    
    public void deletePermission(Long permissionId);
}
