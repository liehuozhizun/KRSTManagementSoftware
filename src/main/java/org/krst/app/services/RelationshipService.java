package org.krst.app.services;

import org.krst.app.domains.*;
import org.krst.app.domains.InformationOperations;
import org.krst.app.repositories.PersonRepository;
import org.krst.app.repositories.StaffRepository;
import org.krst.app.repositories.StudentRepository;
import org.krst.app.repositories.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RelationshipService {
    @Autowired private StudentRepository studentRepository;
    @Autowired private TeacherRepository teacherRepository;
    @Autowired private StaffRepository staffRepository;
    @Autowired private PersonRepository personRepository;
    @Autowired private CacheService cacheService;

    /*
     * Add relationship to each other
     * Input  : Relation.Type, type of the current person
     *          String, id of the current person
     *          String, id of the relative
     *          String, name of the relative
     *          String, the relationship of the relative to the current person
     *          Relation.Type, type of relative
     * Output : none
     */
    public void addRelationship(Relation.Type curType, String curId, String id, String name, String relationship, Relation.Type type) {
        switch (curType) {
            case STUDENT:
                studentRepository.addRelationship(curId, id, name, relationship, type.ordinal()); break;
            case TEACHER:
                teacherRepository.addRelationship(curId, id, name, relationship, type.ordinal()); break;
            case STAFF:
                staffRepository.addRelationship(curId, id, name, relationship, type.ordinal()); break;
            case PERSON:
                personRepository.addRelationship(curId, id, name, relationship, type.ordinal()); break;
        }
    }

    /*
     * Update relationship for both relatives
     * Input  : Relation.Type, type of A
     *          String, id of A
     *          Relation.Type, type of B
     *          String, id of B
     *          String, the relationship of A to B
     *          String, the relationship of B to A
     * Output : none
     */
    public void updateRelationship(Relation.Type AType, String AId, Relation.Type BType, String BId, String relationshipA2B, String relationshipB2A) {
        switch (AType) {
            case STUDENT:
                studentRepository.updateRelationship(AId, BId, relationshipB2A, BType.ordinal()); break;
            case TEACHER:
                teacherRepository.updateRelationship(AId, BId, relationshipB2A, BType.ordinal()); break;
            case STAFF:
                staffRepository.updateRelationship(AId, BId, relationshipB2A, BType.ordinal()); break;
            case PERSON:
                personRepository.updateRelationship(AId, BId, relationshipB2A, BType.ordinal()); break;
        }

        switch (BType) {
            case STUDENT:
                studentRepository.updateRelationship(BId, AId, relationshipA2B, AType.ordinal()); break;
            case TEACHER:
                teacherRepository.updateRelationship(BId, AId, relationshipA2B, AType.ordinal()); break;
            case STAFF:
                staffRepository.updateRelationship(BId, AId, relationshipA2B, AType.ordinal()); break;
            case PERSON:
                personRepository.updateRelationship(BId, AId, relationshipA2B, AType.ordinal()); break;
        }
    }

    /*
     * Update id in relationship within other's if change any id
     * Input  : Set<Relation>, the relationship records of the current person who changed the id
     *          String, oldId, the old id of the current person
     *          String, new id of the current person
     *          String, new name of the current person
     * Output : none
     */
    public void updateIdAndName(Set<Relation> relationship, String oldId, String newId, String newName) {
        if (relationship == null) return;
        relationship.forEach(relation -> {
            switch (relation.getType()) {
                case STUDENT:
                    studentRepository.updateRelationshipIdAndName(relation.getId(), oldId, newId, newName, relation.getType().ordinal()); break;
                case TEACHER:
                    teacherRepository.updateRelationshipIdAndName(relation.getId(), oldId, newId, newName, relation.getType().ordinal()); break;
                case STAFF:
                    staffRepository.updateRelationshipIdAndName(relation.getId(), oldId, newId, newName, relation.getType().ordinal()); break;
                case PERSON:
                    personRepository.updateRelationshipIdAndName(relation.getId(), oldId, newId, newName, relation.getType().ordinal()); break;
            }
        });
    }

    /*
     * Remove relationship info
     * Input  : Relation.Type, type of A
     *          String, id of A
     *          String, id of B
     *          Relation.Type, type of B
     * Output : none
     */
    public void removeRelationship(Relation.Type AType, String AId, String BId, Relation.Type BType) {
        switch (AType) {
            case STUDENT:
                studentRepository.removeRelationship(AId, BId, BType.ordinal()); break;
            case TEACHER:
                teacherRepository.removeRelationship(AId, BId, BType.ordinal()); break;
            case STAFF:
                staffRepository.removeRelationship(AId, BId, BType.ordinal());
                cacheService.refreshStaffCache(); break;
            case PERSON:
                personRepository.removeRelationship(AId, BId, BType.ordinal()); break;
        }
    }

    /*
     * Remove relationship info from all tables other than
     * Input  : String, id
     *          Relation.Type, type
     * Output : none
     */
    public void removeRelationship(Relation.Type type, String id) {
        switch (type) {
            case STUDENT:
                teacherRepository.removeRelationship(id, type.ordinal());
                staffRepository.removeRelationship(id, type.ordinal());
                personRepository.removeRelationship(id, type.ordinal());
                break;
            case TEACHER:
                studentRepository.removeRelationship(id, type.ordinal());
                staffRepository.removeRelationship(id, type.ordinal());
                personRepository.removeRelationship(id, type.ordinal());
                break;
            case STAFF:
                studentRepository.removeRelationship(id, type.ordinal());
                teacherRepository.removeRelationship(id, type.ordinal());
                personRepository.removeRelationship(id, type.ordinal());
                break;
            case PERSON:
                studentRepository.removeRelationship(id, type.ordinal());
                teacherRepository.removeRelationship(id, type.ordinal());
                staffRepository.removeRelationship(id, type.ordinal());
                break;
        }
    }

    /*
     * Remove relationship info
     * Input  : Relation.Type, type of the relative
     * Output : List<? extends InformationOperations>, all possible relatives of the type
     */
    public List<? extends InformationOperations> getAllPossibleRelatives(Relation.Type type) {
        switch (type) {
            case STUDENT:
                return studentRepository.findAll();
            case TEACHER:
                return teacherRepository.findAll();
            case STAFF:
                return cacheService.getStaffs();
            case PERSON:
                return personRepository.findAll();
            default:
                return new ArrayList<>();
        }
    }

    /*
     * Obtain the relationship of the relative
     * Input  : Relation.Type, type of A
     *          String, id of A
     *          String, id of B
     *          Relation.Type, type of B
     * Output : String, the relationship of the relative
     */
    public String getRelationship(Relation.Type AType, String AId, String BId, Relation.Type BType) {
        switch (AType) {
            case STUDENT:
                return studentRepository.getRelationship(AId, BId, BType.ordinal());
            case TEACHER:
                return teacherRepository.getRelationship(AId, BId, BType.ordinal());
            case STAFF:
                return staffRepository.getRelationship(AId, BId, BType.ordinal());
            case PERSON:
                return personRepository.getRelationship(AId, BId, BType.ordinal());
            default:
                return null;
        }
    }

    /*
     * Obtain the relevant person data model
     * Input  : Relation.Type, type of the person
     *          String, id of the current person
     * Output : Optional<? extends InformationOperations>, data model of the person
     */
    public Optional<? extends InformationOperations> getDataModel(Relation.Type type, String id) {
        switch (type) {
            case STUDENT:
                return studentRepository.findById(id);
            case TEACHER:
                return teacherRepository.findById(id);
            case STAFF:
                return staffRepository.findById(id);
            case PERSON:
                return personRepository.findById(id);
            default:
                return Optional.empty();
        }
    }
}
