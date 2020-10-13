package org.krst.app.repositories;

import org.jetbrains.annotations.NotNull;
import org.krst.app.domains.Login;
import org.krst.app.models.Status;
import org.krst.app.utils.Constants;

import java.util.Optional;

public class AdminDatabase extends Database<Login> {

    public AdminDatabase() {
        databaseName = Constants.ADMIN_DATABASE;
    }

    @Override
    public Optional findById(Class c, String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Login> findById(Class c, Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Status save(Login object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Status delete(Login object) {
        throw new UnsupportedOperationException();
    }

    public Status verify(@NotNull String password) {
        Login login = findAll().get(0);
        if (login.getRetryRemainingTimes() <= 0) {
            return Status.CONSTRAINT_VIOLATION;
        }

        boolean passwordMatch = password.equals(login.getPassword());
        if (passwordMatch) {
            login.setRetryRemainingTimes(3);
            remainingRetryTimes = 3;
        } else {
            login.setRetryRemainingTimes(login.getRetryRemainingTimes() - 1);
            remainingRetryTimes = login.getRetryRemainingTimes();
        }
        update(login);
        return passwordMatch ? Status.SUCCESS : Status.ERROR;
    }

    private int remainingRetryTimes = 0;

    public int getRemainingRetryTimes() {
        return remainingRetryTimes;
    }

    public boolean changePassword(@NotNull String newPassword) {
        Login login = findAll().get(0);
        login.setPassword(newPassword);
        return Status.SUCCESS == update(login);
    }
}
