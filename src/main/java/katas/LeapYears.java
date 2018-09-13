package katas;

final class LeapYears {
    static boolean isLeapYear(final int year) {
        return year % 400 == 0 && year % 4000 != 0 || year % 4 == 0 && year % 100 != 0;
    }
}
