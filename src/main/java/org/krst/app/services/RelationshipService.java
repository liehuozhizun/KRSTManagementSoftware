package org.krst.app.services;

import org.krst.app.configurations.Logger;
import org.krst.app.domains.*;
import org.krst.app.domains.operations.InformationOperations;
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

    @Autowired private Logger logger;

    /*
     * Add relationship to each other
     * Input  : T, relative A (current person)
     *          Relation.Type, type of A
     *          S, relative B (the one who will be passively built relationship)
     *          Relation.Type, type of B
     *          String, the relationship of A to B
     *          String, the relationship of B to A
     * Output : T, the updated relative model
     */
    public <T extends InformationOperations, S extends InformationOperations> T addRelationShip(
            T A, Relation.Type AType,
            S B, Relation.Type BType,
            String relationshipA2B,
            String relationshipB2A
    ) {
        if (A == null || B == null || relationshipA2B == null || relationshipB2A == null) {
            return null;
        }

        try {
            Relation A2BRelation = new Relation(relationshipB2A, A.getName(), AType, A.getId());
            Relation B2ARelation = new Relation(relationshipA2B, B.getName(), BType, B.getId());

            B.getRelationships().add(A2BRelation);
            A.getRelationships().add(B2ARelation);

            saveModelToRepository(BType, B);
            return saveModelToRepository(AType, A);
        } catch (Exception e) {
            logger.logError(this.getClass().toString(), "新增亲属关系失败：错误原因 - ", e.getMessage());
            return null;
        }
    }

    public void addRelationship(Relation.Type curType, String curId, String id, String name, String relationship, Relation.Type type) {
        switch (curType) {
            case STUDENT:
                studentRepository.addRelationship(curId, id, name, relationship, type);
            case TEACHER:
                teacherRepository.addRelationship(curId, id, name, relationship, type);
            case STAFF:
                staffRepository.addRelationship(curId, id, name, relationship, type);
            case PERSON:
                personRepository.addRelationship(curId, id, name, relationship, type);
        }
    }

    public Optional<?> getDataModel(Relation.Type type, String id) {
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

    /*
     * Update relationship for both relatives
     * Input  : Relation.Type, type of A
     *          String, id of A
     *          Relation.Type, type of B
     *          String, id of B
     *          String, the relationship of A to B
     *          String, the relationship of B to A
     * Output : T, the updated relative model
     */
    public void updateRelationship(Relation.Type AType, String AId, Relation.Type BType, String BId, String relationshipA2B, String relationshipB2A) {
        switch (AType) {
            case STUDENT:
                studentRepository.updateRelationship(AId, BId, relationshipB2A, BType);
            case TEACHER:
                teacherRepository.updateRelationship(AId, BId, relationshipB2A, BType);
            case STAFF:
                staffRepository.updateRelationship(AId, BId, relationshipB2A, BType);
            case PERSON:
                personRepository.updateRelationship(AId, BId, relationshipB2A, BType);
        }

        switch (BType) {
            case STUDENT:
                studentRepository.updateRelationship(AId, BId, relationshipA2B, AType);
            case TEACHER:
                teacherRepository.updateRelationship(AId, BId, relationshipA2B, AType);
            case STAFF:
                staffRepository.updateRelationship(AId, BId, relationshipA2B, AType);
            case PERSON:
                personRepository.updateRelationship(AId, BId, relationshipA2B, AType);
        }
    }

    /*
     * Update id in relationship within other's if change any id
     * Input  : Set<Relation>, the relationship records of the current person who changed the id
     *          String, oldId, the old id of the current person
     *          String, new id of the current person
     *          String, new name of the current person
     * Output : nothing
     */
    public void updateIdAndName(Set<Relation> relationship, String oldId, String newId, String newName) {
        relationship.forEach(relation -> {
            switch (relation.getType()) {
                case STUDENT:
                    studentRepository.updateRelationshipIdAndName(relation.getId(), oldId, newId, newName, relation.getType());
                case TEACHER:
                    teacherRepository.updateRelationshipIdAndName(relation.getId(), oldId, newId, newName, relation.getType());
                case STAFF:
                    staffRepository.updateRelationshipIdAndName(relation.getId(), oldId, newId, newName, relation.getType());
                case PERSON:
                    personRepository.updateRelationshipIdAndName(relation.getId(), oldId, newId, newName, relation.getType());
            }
        });
    }

    /*
     * Check if this person exists in database by id
     * Input  : Relation.Type, type of this person
     *          String, id of this person
     * Output : String, null if this person doesn't exist
     *                  actual name if this person exists
     */
    public String checkExistenceById(Relation.Type type, String id) {
        return getNameById(type, id);
    }

    /*
     * Check if this person exists in database by name
     * Input  : Relation.Type, type of this person
     *          String, name of this person
     * Output : String, null if this person doesn't exist
     *                  id if this person exists
     */
    public String checkExistenceByName(Relation.Type type, String name) {
        return getIdByName(type, name);
    }

    private <T> T saveModelToRepository(Relation.Type type, T data) {
        switch (type) {
            case STUDENT:
                return (T) studentRepository.save((Student) data);
            case TEACHER:
                return (T) teacherRepository.save((Teacher) data);
            case STAFF:
                return (T) staffRepository.save((Staff) data);
            case PERSON:
                return (T) personRepository.save((Person) data);
            default:
                return null;
        }
    }

    private String getIdByName(Relation.Type type, String name) {
        switch (type) {
            case STUDENT:
                return studentRepository.findTopByName(name).orElse(new Student()).getId();
            case TEACHER:
                return teacherRepository.findTopByName(name).orElse(new Teacher()).getId();
            case STAFF:
                return staffRepository.findTopByName(name).orElse(new Staff()).getId();
            case PERSON:
                return personRepository.findTopByName(name).orElse(new Person()).getId();
            default:
                return null;
        }
    }

    private String getNameById(Relation.Type type, String id) {
        Optional data = getDataModelById(type, id);
        return data.isPresent() ? ((InformationOperations)data.get()).getName() : null;
    }

    private Optional getDataModelById(Relation.Type type, String id) {
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
}
