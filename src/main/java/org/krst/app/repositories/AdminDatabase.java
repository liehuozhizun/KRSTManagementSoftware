package org.krst.app.repositories;

import org.krst.app.domains.Login;
import org.krst.app.utils.Constants;

import java.util.List;

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

    public boolean verify(String password) {
        Login login = findAll().get(0);
        if (login.getRetryRemainingTimes() <= 0) {
            return false;
        }

        boolean passwordMatch = login.getPassword().equals(password);
        if (passwordMatch) {
            login.setRetryRemainingTimes(3);
            remainingRetryTimes = 3;
        } else {
            login.setRetryRemainingTimes(login.getRetryRemainingTimes() - 1);
            remainingRetryTimes = login.getRetryRemainingTimes();
            passwordMatch = false;
        }
        update(login);
        return passwordMatch;
    }

    private int remainingRetryTimes = 0;

    public int getRemainingRetryTimes() {
        return remainingRetryTimes;
    }
}
