package org.krst.app.services;

import org.jetbrains.annotations.NotNull;
import org.krst.app.domains.Login;
import org.krst.app.models.Status;
import org.krst.app.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private AdminRepository adminRepository;

    private int remainingRetryTimes = 0;

    public int getRemainingRetryTimes() {
        return remainingRetryTimes;
    }

    public Status verify(@NotNull String password) {
        Login login = adminRepository.findAll().get(0);
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
        adminRepository.saveAndFlush(login);
        return passwordMatch ? Status.SUCCESS : Status.ERROR;
    }

    public void changePassword(@NotNull String newPassword) {
        Login login = adminRepository.findAll().get(0);
        login.setPassword(newPassword);
    }
}
