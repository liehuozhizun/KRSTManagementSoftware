package org.krst.app.services;

import org.krst.app.domains.*;
import org.krst.app.domains.operations.InformationOperations;
import org.krst.app.models.Remainder;
import org.krst.app.repositories.*;
import org.krst.app.utils.CommonUtils;
import org.krst.app.utils.RemainderDateType;
import org.krst.app.utils.RemainderEventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class RemainderService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private RelationRepository relationRepository;
    @Autowired
    private PatronSaintDateRepository patronSaintDateRepository;

    private final int month = CommonUtils.getCurrentZonedTime().getMonthValue();
    private final int day = CommonUtils.getCurrentZonedTime().getDayOfMonth();
    private final int daysInMonth = getDaysInMonth(month, CommonUtils.getCurrentZonedTime().toLocalDate().isLeapYear());

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final Map<RemainderDateType, List<Remainder>> remainderCache = new ConcurrentHashMap<>();
    private boolean isRemainderReady = false;

    @PostConstruct
    public void init() {
        refresh();
    }

    public Map<RemainderDateType, List<Remainder>> getRemainder() {
        if (!isRemainderReady) prepareRemainder();
        return remainderCache;
    }

    public void refresh() {
        isRemainderReady = false;
        remainderCache.clear();
        remainderCache.put(RemainderDateType.TODAY, new ArrayList<>());
        remainderCache.put(RemainderDateType.ONE_DAY, new ArrayList<>());
        remainderCache.put(RemainderDateType.ONE_WEEK, new ArrayList<>());
        remainderCache.put(RemainderDateType.HALF_MONTH, new ArrayList<>());
        remainderCache.put(RemainderDateType.ONE_MONTH, new ArrayList<>());
        prepareRemainder();
    }

    private void prepareRemainder() {
        executorService.submit(this::preparePatronSaintDateRemainder);
        executorService.submit(() -> preparePersonDateRemainder(studentRepository));
        executorService.submit(() -> preparePersonDateRemainder(teacherRepository));
        executorService.submit(() -> preparePersonDateRemainder(staffRepository));
        executorService.submit(() -> preparePersonDateRemainder(relationRepository));
        isRemainderReady = true;
    }

    private void preparePatronSaintDateRemainder() {
        List<PatronSaintDate> list = patronSaintDateRepository.findAll();
        for (PatronSaintDate patronSaintDate : list) {
            RemainderDateType type = calculateDifferenceInDays(patronSaintDate.getDate());
            if (type != RemainderDateType.NONE) {
                remainderCache.get(type).add(new Remainder(patronSaintDate.getDate(), RemainderEventType.PATRON_SAINT, patronSaintDate.getName()));
            }
        }
    }

    private void preparePersonDateRemainder(JpaRepository repository) {
        List<InformationOperations> list = repository.findAll();
        for (InformationOperations info : list) {
            compareDifferentDatesInPersonDomain(info);
        }
    }

    private void compareDifferentDatesInPersonDomain(InformationOperations info) {
        RemainderDateType type;
        if (info.getIsGregorianCalendar())
            type = RemainderDateType.NONE;
        else
            type = calculateDifferenceInDays(info.getBirthday());
        if (type != RemainderDateType.NONE) {
            remainderCache.get(type).add(new Remainder(info.getBirthday(), RemainderEventType.BIRTHDAY, info.getName() + " [" + info.getBaptismalName() + "-" + info.getId() +"]"));
        }
        type = calculateDifferenceInDays(info.getBaptismalDate());
        if (type != RemainderDateType.NONE) {
            remainderCache.get(type).add(new Remainder(info.getBaptismalDate(), RemainderEventType.BAPTISMAL, info.getName() + " [" + info.getBaptismalName() + "-" + info.getId() +"]"));
        }
        type = calculateDifferenceInDays(info.getConfirmationDate());
        if (type != RemainderDateType.NONE) {
            remainderCache.get(type).add(new Remainder(info.getConfirmationDate(), RemainderEventType.CONFIRMATION, info.getName() + " [" + info.getBaptismalName() + "-" + info.getId() +"]"));
        }
        type = calculateDifferenceInDays(info.getMarriageDate());
        if (type != RemainderDateType.NONE) {
            remainderCache.get(type).add(new Remainder(info.getMarriageDate(), RemainderEventType.MARRIAGE, info.getName() + " [" + info.getBaptismalName() + "-" + info.getId() +"]"));
        }
        type = calculateDifferenceInDays(info.getDeathDate());
        if (type != RemainderDateType.NONE) {
            remainderCache.get(type).add(new Remainder(info.getDeathDate(), RemainderEventType.DEATH, info.getName() + " [" + info.getBaptismalName() + "-" + info.getId() +"]"));
        }
    }

    private int getDaysInMonth(int month, boolean isLeapYear) {
        if (month == 2) return isLeapYear ? 28 : 29;
        if (month < 8) return month % 2 == 0 ? 30 : 31;
        return month % 2 == 0 ? 31 : 30;
    }

    private RemainderDateType calculateDifferenceInDays(final LocalDate date) {
        if (date == null) return RemainderDateType.NONE;
        int m = date.getMonthValue();
        int d = date.getDayOfMonth();
        if (m == month) {
            int diff = d - day;
            if (diff < 0) {
                return RemainderDateType.NONE;
            } else if (d == day) {
                return RemainderDateType.TODAY;
            } else if (diff == 1) {
                return RemainderDateType.ONE_DAY;
            } else if (diff <= 7) {
                return RemainderDateType.ONE_WEEK;
            } else if (diff == 14 || diff == 15) {
                return RemainderDateType.HALF_MONTH;
            } else if (diff == 29 || diff == 30) {
                return RemainderDateType.ONE_MONTH;
            }
        } else if (m - month == 1) {
            int diff = d - day;
            if (diff == 0 || diff == -1) {
                return RemainderDateType.ONE_MONTH;
            } else if (diff < 0) {
                diff += daysInMonth;
                if (diff < 0) {
                    return RemainderDateType.NONE;
                } else if (diff == 1) {
                    return RemainderDateType.ONE_DAY;
                } else if (diff <= 7) {
                    return RemainderDateType.ONE_WEEK;
                } else if (diff == 14 || diff == 15) {
                    return RemainderDateType.HALF_MONTH;
                }
            }
        }
        return RemainderDateType.NONE;
    }
}
