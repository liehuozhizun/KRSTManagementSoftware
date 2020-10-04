package org.krst.app.repositories;

import org.krst.app.domains.Evaluation;
import org.krst.app.domains.Staff;
import org.krst.app.utils.Constants;

import java.util.ArrayList;

public class StaffDatabase extends Database<Staff> {

    public StaffDatabase() {
        databaseName = Constants.STAFF_DATABASE;
    }

    public Staff addEvaluation(Staff staff, Evaluation evaluation) {
        if (staff.getEvaluations() == null) staff.setEvaluations(new ArrayList<>());
        staff.getEvaluations().add(evaluation);
        return update(staff);
    }

    public Staff updateEvaluation(Staff staff, Evaluation evaluation) {
        if (staff.getEvaluations() == null) return staff;
        staff.getEvaluations().removeIf(x -> x.getId().equals(evaluation.getId()));
        staff.getEvaluations().add(evaluation);
        return update(staff);
    }

    public Staff deleteEvaluation(Staff staff, Evaluation evaluation) {
        if (staff.getEvaluations() == null) return staff;
        staff.getEvaluations().removeIf(x -> x.getId().equals(evaluation.getId()));
        return update(staff);
    }
}