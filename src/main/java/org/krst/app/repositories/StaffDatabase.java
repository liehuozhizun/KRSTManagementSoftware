package org.krst.app.repositories;

import org.krst.app.domains.Evaluation;
import org.krst.app.domains.Staff;
import org.krst.app.models.Status;
import org.krst.app.utils.Constants;

import java.util.HashSet;

public class StaffDatabase extends Database<Staff> {

    public StaffDatabase() {
        databaseName = Constants.STAFF_DATABASE;
    }

    public Status addEvaluation(Staff staff, Evaluation evaluation) {
        if (staff.getEvaluations() == null) staff.setEvaluations(new HashSet<>());
        staff.getEvaluations().add(evaluation);
        return update(staff);
    }

    public Status updateEvaluation(Staff staff, Evaluation evaluation) {
        if (staff.getEvaluations() == null) return Status.SUCCESS;
        staff.getEvaluations().removeIf(x -> x.getId().equals(evaluation.getId()));
        staff.getEvaluations().add(evaluation);
        return update(staff);
    }

    public Status deleteEvaluation(Staff staff, Evaluation evaluation) {
        if (staff.getEvaluations() == null) return Status.SUCCESS;
        staff.getEvaluations().removeIf(x -> x.getId().equals(evaluation.getId()));
        return update(staff);
    }
}