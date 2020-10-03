package services;

import domains.*;
import domains.operations.InformationOperations;
import models.Remainder;
import repositories.Database;
import utils.RemainderDateType;
import utils.RemainderEventType;
import utils.database.DatabaseFactory;
import utils.database.DatabaseType;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class RemainderService {
    private Database<Student> studentDatabase = DatabaseFactory.getDatabase(DatabaseType.STUDENT);
    private Database<Teacher> teacherDatabase = DatabaseFactory.getDatabase(DatabaseType.TEACHER);
    private Database<Staff> staffDatabase = DatabaseFactory.getDatabase(DatabaseType.STAFF);
    private Database<Relation> relationDatabase = DatabaseFactory.getDatabase(DatabaseType.RELATION);
    private Database<PatronSaintDate> patronSaintDateDatabase = DatabaseFactory.getDatabase(DatabaseType.PATRON_SAINT_DATE);

    private int month = LocalDate.now().getMonthValue();
    private int day = LocalDate.now().getDayOfMonth();
    private int daysInMonth = getDaysInMonth(month, LocalDate.now().isLeapYear());

    private ExecutorService executorService = Executors.newCachedThreadPool();
    private static Map<RemainderDateType, List<Remainder>> remainderCache = new ConcurrentHashMap<>();
    private static boolean isRemainderReady = false;

    public static Map<RemainderDateType, List<Remainder>> getRemainder() {
        if (!isRemainderReady) {
            new RemainderService().prepareRemainder();
        }
        return remainderCache;
    }

    public static void refresh() {
        new RemainderService().prepareRemainder();
    }

    private void prepareRemainder() {
        executorService.submit(this::preparePatronSaintDateRemainder);
        executorService.submit(() -> preparePersonDateRemainder(studentDatabase));
        executorService.submit(() -> preparePersonDateRemainder(teacherDatabase));
        executorService.submit(() -> preparePersonDateRemainder(staffDatabase));
        executorService.submit(() -> preparePersonDateRemainder(relationDatabase));
        isRemainderReady = true;
    }

    private void preparePatronSaintDateRemainder() {
        List<PatronSaintDate> list = patronSaintDateDatabase.findAll();
        for (PatronSaintDate patronSaintDate : list) {
            RemainderDateType type = calculateDifferenceInDays(patronSaintDate.getDate());
            if (type != RemainderDateType.NONE) {
                remainderCache.get(type).add(new Remainder(patronSaintDate.getDate(), RemainderEventType.PATRON_SAINT, patronSaintDate.getName()));
            }
        }
    }

    private void preparePersonDateRemainder(Database database) {
        List<InformationOperations> list = database.findAll();
        for (InformationOperations info : list) {
            compareDifferentDatesInPersonDomain(info);
        }
    }

    private void compareDifferentDatesInPersonDomain(InformationOperations info) {
        RemainderDateType type = calculateDifferenceInDays(info.getBirthday());
        if (type != RemainderDateType.NONE) {
            remainderCache.get(type).add(new Remainder(info.getBirthday(), RemainderEventType.BIRTHDAY, info.getName()));
        }
        type = calculateDifferenceInDays(info.getBaptismalDate());
        if (type != RemainderDateType.NONE) {
            remainderCache.get(type).add(new Remainder(info.getBaptismalDate(), RemainderEventType.BAPTISMAL, info.getName()));
        }
        type = calculateDifferenceInDays(info.getConfirmationDate());
        if (type != RemainderDateType.NONE) {
            remainderCache.get(type).add(new Remainder(info.getConfirmationDate(), RemainderEventType.CONFIRMATION, info.getName()));
        }
        type = calculateDifferenceInDays(info.getMarriageDate());
        if (type != RemainderDateType.NONE) {
            remainderCache.get(type).add(new Remainder(info.getConfirmationDate(), RemainderEventType.MARRIAGE, info.getName()));
        }
        type = calculateDifferenceInDays(info.getDeathDate());
        if (type != RemainderDateType.NONE) {
            remainderCache.get(type).add(new Remainder(info.getDeathDate(), RemainderEventType.DEATH, info.getName()));
        }
    }

    private int getDaysInMonth(int month, boolean isLeapYear) {
        if (month == 2) return isLeapYear ? 28 : 29;
        if (month < 8) return month % 2 == 0 ? 30 : 31;
        return month % 2 == 0 ? 31 : 30;
    }

    private RemainderDateType calculateDifferenceInDays(LocalDate date) {
        if (date == null) return RemainderDateType.NONE;
        int m = date.getMonthValue();
        int d = date.getDayOfMonth();
        if (m == month) {
            int diff = d - day;
            if (d == day) {
                return RemainderDateType.TODAY;
            } else if (diff == 1) {
                return RemainderDateType.ONE_DAY;
            } else if (diff == 7) {
                return RemainderDateType.ONE_WEEK;
            } else if (diff == 15 || diff == 14) {
                return RemainderDateType.HALF_MONTH;
            } else if (diff == 29 || diff == 30) {
                return RemainderDateType.ONE_MONTH;
            }
        } else if (m - month == 1) {
            int diff = d - day;
            if (diff == 0) {
                return RemainderDateType.ONE_MONTH;
            } else if (diff < 0) {
                diff += daysInMonth;
                if (diff == 1) {
                    return RemainderDateType.ONE_DAY;
                } else if (diff == 7) {
                    return RemainderDateType.ONE_WEEK;
                } else if (diff == 15) {
                    return RemainderDateType.HALF_MONTH;
                }
            }
        }
        return RemainderDateType.NONE;
    }
}
