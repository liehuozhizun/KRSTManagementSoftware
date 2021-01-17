package org.krst.app.services;

import org.krst.app.configurations.Logger;
import org.krst.app.domains.*;
import org.krst.app.domains.operations.InformationOperations;
import org.krst.app.repositories.PersonRepository;
import org.krst.app.repositories.StaffRepository;
import org.krst.app.repositories.StudentRepository;
import org.krst.app.repositories.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class RelationshipService {
    @Autowired private StudentRepository studentRepository;
    @Autowired private TeacherRepository teacherRepository;
    @Autowired private StaffRepository staffRepository;
    @Autowired private PersonRepository personRepository;

    @Autowired private Logger logger;

    /*
     * Add relationship to each other
     * Input  : T, relative A (current person)
     *          Relation.Type, type of A
     *          S, relative B (the one who will be passively built relationship)
     *          Relation.Type, type of B
     *          String, the relationship of A to B
     *          String, the relationship of B to A
     * Output : Boolean, result of relationship adding operation
     *            true, succeed
     *            false, fail
     */
    public <T extends InformationOperations, S extends InformationOperations> Boolean addRelationShip(
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

            saveModelToRepository(AType, A);
            saveModelToRepository(BType, B);
        } catch (Exception e) {
            logger.logError(this.getClass().toString(), "新增亲属关系失败：错误原因 - ", e.getMessage());
            return false;
        }

        return true;
    }

//    public <T extends InformationOperations> Boolean updateRelationship(A, Relation.Type AType, String A2BType, ) {
//
//    }

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

    private <T> void saveModelToRepository(Relation.Type type, T data) {
        switch (type) {
            case STUDENT:
                studentRepository.save((Student) data);
                break;
            case TEACHER:
                teacherRepository.save((Teacher) data);
                break;
            case STAFF:
                staffRepository.save((Staff) data);
                break;
            case PERSON:
                personRepository.save((Person) data);
                break;
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
        switch (type) {
            case STUDENT:
                return studentRepository.findById(id).orElse(new Student()).getName();
            case TEACHER:
                return teacherRepository.findById(id).orElse(new Teacher()).getName();
            case STAFF:
                return staffRepository.findById(id).orElse(new Staff()).getName();
            case PERSON:
                return personRepository.findById(id).orElse(new Person()).getName();
            default:
                return null;
        }
    }
}
