package org.krst.app.services;

import org.krst.app.domains.Attribute;
import org.krst.app.domains.CourseTemplate;
import org.krst.app.domains.Staff;
import org.krst.app.repositories.AttributeRepository;
import org.krst.app.repositories.CourseTemplateRepository;
import org.krst.app.repositories.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CacheService {

    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private AttributeRepository attributeRepository;
    @Autowired
    private CourseTemplateRepository courseTemplateRepository;

    private List<Staff> staffs;
    private List<Attribute> attributes;
    private List<CourseTemplate> courseTemplates;

    public List<Staff> getStaffs() {
        if (staffs == null) refreshStaffCache();
        return staffs;
    }

    public void refreshStaffCache() {
        staffs = staffRepository.findAll();
    }

    public List<Attribute> getAttributes() {
        if (attributes == null) refreshAttributeCache();
        return attributes;
    }

    public void refreshAttributeCache() {
        attributes = attributeRepository.findAll();
    }

    public List<CourseTemplate> getCourseTemplates() {
        if (courseTemplates == null) refreshCourseTemplateCache();
        return courseTemplates;
    }

    public void refreshCourseTemplateCache() {
        courseTemplates = courseTemplateRepository.findAll();
    }
}
