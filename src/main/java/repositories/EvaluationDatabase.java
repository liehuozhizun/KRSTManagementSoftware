package repositories;

import domains.Evaluation;
import utils.Constants;

public class EvaluationDatabase extends Database<Evaluation> {

    public EvaluationDatabase() {
        databaseName = Constants.EVALUATION_DATABASE;
    }

}
