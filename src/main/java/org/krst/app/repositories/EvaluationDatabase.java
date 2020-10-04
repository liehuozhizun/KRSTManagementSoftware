package org.krst.app.repositories;

import org.krst.app.domains.Evaluation;
import org.krst.app.utils.Constants;

public class EvaluationDatabase extends Database<Evaluation> {

    public EvaluationDatabase() {
        databaseName = Constants.EVALUATION_DATABASE;
    }

}
