package org.krst.app.services;

import org.krst.app.domains.Login;
import org.krst.app.models.Status;
import org.krst.app.repositories.AdminRepository;
import org.krst.app.utils.Constants;
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

    public Status checkLoginExists() {
        switch ((int) adminRepository.count()) {
            case 0: return Status.DATA_NOT_FOUND;
            case 1: return Status.SUCCESS;
            default: return Status.ERROR;
        }
    }

    public Status verify(String password) {
        Login login = adminRepository.findAll().get(0);
        if (login.getRetryRemainingTimes() <= 0) {
            return Status.CONSTRAINT_VIOLATION;
        }

        boolean passwordMatch = password.equals(login.getPassword());
        if (passwordMatch) {
            login.setRetryRemainingTimes(Constants.MOST_RETRY_LOGIN_TIMES);
            remainingRetryTimes = Constants.MOST_RETRY_LOGIN_TIMES;
        } else {
            login.setRetryRemainingTimes(login.getRetryRemainingTimes() - 1);
            remainingRetryTimes = login.getRetryRemainingTimes();
        }
        adminRepository.saveAndFlush(login);
        return passwordMatch ? Status.SUCCESS : Status.ERROR;
    }

    public Status unlimitedVerify(String password) {
        Login login = adminRepository.findAll().get(0);

        boolean passwordMatch = password.equals(login.getPassword());
        return passwordMatch ? Status.SUCCESS : Status.ERROR;
    }

    public void changePassword(String newPassword) {
        Login login = adminRepository.findAll().get(0);
        login.setPassword(newPassword);
        login.setRetryRemainingTimes(Constants.MOST_RETRY_LOGIN_TIMES);
        adminRepository.saveAndFlush(login);
    }
}
