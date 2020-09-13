package repositories;

import domains.Login;
import utils.Constants;

public class AdminDatabase extends Database<Login> {

    public AdminDatabase() {
        databaseName = Constants.ADMIN_DATABASE;
    }

    @Override
    public <T> T findById(Class c, String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T findById(Class c, Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T save(T object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T update(T object) {
        return super.update(object);
    }

    @Override
    public <T> T delete(T object) {
        throw new UnsupportedOperationException();
    }
}
