package repositories;

import domains.Login;
import utils.Constants;

public class AdminDatabase extends Database<Login> {

    public AdminDatabase() {
        databaseName = Constants.ADMIN_DATABASE;
    }

    @Override
    public Login findById(Class c, String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Login findById(Class c, Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Login save(Login object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Login update(Login object) {
        return super.update(object);
    }

    @Override
    public Login delete(Login object) {
        throw new UnsupportedOperationException();
    }
}
