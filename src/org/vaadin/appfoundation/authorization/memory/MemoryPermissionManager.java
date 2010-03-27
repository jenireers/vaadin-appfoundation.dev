package org.vaadin.appfoundation.authorization.memory;

import org.vaadin.appfoundation.authorization.PermissionManager;
import org.vaadin.appfoundation.authorization.Resource;
import org.vaadin.appfoundation.authorization.Role;

/**
 * An implementation of the {@link PermissionManager} interface in which all the
 * permission details are kept in memory. No permission are persisted.
 * 
 * @author Kim
 * 
 */
public class MemoryPermissionManager implements PermissionManager {

    /**
     * Contains the "allowed" permissions for those permission where an explicit
     * action has been defined.
     */
    private final PermissionMap allowed;

    /**
     * Contains the "denied" permissions for those permission where an explicit
     * action has been defined.
     */
    private final PermissionMap denied;

    /**
     * Contains all global "allowed" permissions, these include permissions set
     * with allowDefault and allowAll.
     */
    private final PermissionMap globalAllowed;

    /**
     * Contains all global "denied" permissions, these include permissions set
     * with denyDefault and denyAll.
     */
    private final PermissionMap globalDenied;

    public MemoryPermissionManager() {
        allowed = new PermissionMap();
        denied = new PermissionMap();
        globalAllowed = new PermissionMap();
        globalDenied = new PermissionMap();
    }

    /**
     * {@inheritDoc}
     */
    public void allow(Role role, String action, Resource resource) {
        checkArguments(role, resource);

        if (denied.contains(role, action, resource)) {
            denied.remove(role, action, resource);
        }

        allowed.put(role, action, resource);
    }

    /**
     * {@inheritDoc}
     */
    public void allowAll(Role role, Resource resource) {
        checkArguments(role, resource);

        denied.removeAll(role, resource);
        globalDenied.removeAll(role, resource);
        globalAllowed.put(role, "all", resource);
    }

    /**
     * {@inheritDoc}
     */
    public void deny(Role role, String action, Resource resource) {
        checkArguments(role, resource);

        if (allowed.contains(role, action, resource)) {
            allowed.remove(role, action, resource);
        }

        denied.put(role, action, resource);
    }

    /**
     * {@inheritDoc}
     */
    public void denyAll(Role role, Resource resource) {
        checkArguments(role, resource);

        allowed.removeAll(role, resource);
        globalAllowed.removeAll(role, resource);
        globalDenied.put(role, "all", resource);
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasAccess(Role role, String action, Resource resource) {
        checkArguments(role, resource);

        if (allowed.contains(role, action, resource)) {
            return true;
        }

        if (denied.contains(role, action, resource)) {
            return false;
        }

        if (globalAllowed.contains(role, "all", resource)) {
            return true;
        }

        if (globalDenied.contains(role, "all", resource)) {
            return false;
        }

        if (globalAllowed.hasPermissions(resource)
                || allowed.hasPermissions(resource)) {
            return false;
        }

        return true;
    }

    /**
     * Checks that both the role and the resource is set.
     * 
     * @param role
     * @param resource
     * @throws IllegalArgumentException
     *             Thrown if either role or resource is null
     */
    private void checkArguments(Role role, Resource resource) {
        if (role == null) {
            throw new IllegalArgumentException("Role may not be null");
        }

        if (resource == null) {
            throw new IllegalArgumentException("Role may not be null");
        }
    }

}