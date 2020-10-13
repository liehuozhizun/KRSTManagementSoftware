package org.krst.app.services;

import org.krst.app.domains.Attribute;
import org.krst.app.domains.Staff;
import org.krst.app.utils.database.DatabaseFactory;
import org.krst.app.utils.database.DatabaseType;

import java.util.List;

public class CacheService {

    private static CacheService cacheService;
    private List<Staff> staffs;
    private List<Attribute> attributes;

    private CacheService () {

    }

    public static CacheService get() {
        return cacheService == null ? (cacheService = new CacheService()) : cacheService;
    }

    public void refreshAllCache() {
        refreshStaffCache();
    }

    public List<Staff> getStaffCache() {
        if (staffs == null) {
            refreshStaffCache();
        }
        return staffs;
    }

    public CacheService refreshStaffCache() {
        staffs = DatabaseFactory.getDatabase(DatabaseType.STAFF).findAll();
        return cacheService;
    }

    public List<Attribute> getAttributeCache() {
        if (attributes == null) {
            refreshAttributeCache();
        }
        return attributes;
    }

    public CacheService refreshAttributeCache() {
        attributes = DatabaseFactory.getDatabase(DatabaseType.ATTRIBUTE).findAll();
        return cacheService;
    }
}
