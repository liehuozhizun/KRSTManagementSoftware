package org.krst.app.services;

import org.krst.app.domains.*;
import org.krst.app.domains.InformationOperations;
import org.krst.app.models.Remainder;
import org.krst.app.repositories.*;
import org.krst.app.utils.CommonUtils;
import org.krst.app.utils.RemainderDateType;
import org.krst.app.utils.RemainderEventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
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
    private PersonRepository personRepository;
    @Autowired
    private PatronSaintDateRepository patronSaintDateRepository;

    private final int month = CommonUtils.getCurrentZonedTime().getMonthValue();
    private final int day = CommonUtils.getCurrentZonedTime().getDayOfMonth();
    private final int daysInMonth = getDaysInMonth(month, CommonUtils.getCurrentZonedTime().toLocalDate().isLeapYear());

    private final Map<RemainderDateType, List<Remainder>> remainderCache = new HashMap<>(5);
    private boolean isRemainderReady = false;

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
        sortRemainderCache();
    }

    private void sortRemainderCache() {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        executorService.execute(() -> Collections.sort(remainderCache.get(RemainderDateType.TODAY)));
        executorService.execute(() -> Collections.sort(remainderCache.get(RemainderDateType.ONE_DAY)));
        executorService.execute(() -> Collections.sort(remainderCache.get(RemainderDateType.ONE_WEEK)));
        executorService.execute(() -> Collections.sort(remainderCache.get(RemainderDateType.HALF_MONTH)));
        executorService.execute(() -> Collections.sort(remainderCache.get(RemainderDateType.ONE_MONTH)));
        try {
            executorService.shutdown();
            executorService.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void prepareRemainder() {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        executorService.execute(new PreparePatronSaintDateRemainder());
        executorService.execute(new PreparePeopleDateRemainder<>(studentRepository.findAll()));
        executorService.execute(new PreparePeopleDateRemainder<>(teacherRepository.findAll()));
        executorService.execute(new PreparePeopleDateRemainder<>(staffRepository.findAll()));
        executorService.execute(new PreparePersonDateRemainder<>(personRepository.findAll()));
        isRemainderReady = true;
        try {
            executorService.shutdown();
            executorService.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class PreparePatronSaintDateRemainder implements Runnable {
        private final List<PatronSaintDate> list;
        public PreparePatronSaintDateRemainder() {
            this.list = patronSaintDateRepository.findAll();
        }

        @Override
        public void run() {
            list.forEach(patronSaintDate -> {
                RemainderDateType type = calculateDifferenceInDays(patronSaintDate.getDate());
                if (type != RemainderDateType.NONE) {
                    remainderCache.get(type).add(new Remainder(patronSaintDate.getDate(), RemainderEventType.PATRON_SAINT, patronSaintDate.getName()));
                }
            });
        }
    }

    private class PreparePeopleDateRemainder<T> implements Runnable {
        private final List<T> list;

        public PreparePeopleDateRemainder(List<T> list) {
            this.list = list;
        }

        @Override
        public void run() {
            list.forEach(info -> compareDifferentDatesInPersonDomain((InformationOperations)info));
        }

        private void compareDifferentDatesInPersonDomain(InformationOperations info) {
            RemainderDateType type;
            if (info.getIsGregorianCalendar() == null || info.getIsGregorianCalendar())
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
    }

    private class PreparePersonDateRemainder<T> implements Runnable {
        private final List<T> list;

        public PreparePersonDateRemainder(List<T> list) {
            this.list = list;
        }

        @Override
        public void run() {
            list.forEach(info -> {
                Person person = (Person)info;
                RemainderDateType type;
                if (person.getIsGregorianCalendar() == null || person.getIsGregorianCalendar())
                    type = RemainderDateType.NONE;
                else
                    type = calculateDifferenceInDays(person.getBirthday());
                if (type != RemainderDateType.NONE) {
                    remainderCache.get(type).add(new Remainder(person.getBirthday(), RemainderEventType.BIRTHDAY, person.getName() + " [" + person.getBaptismalName() + "-" + person.getId() +"]"));
                }
            });
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
