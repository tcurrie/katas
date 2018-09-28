package katas;

final class LeapYears {
    private LeapYears() {
        throw new UnsupportedOperationException("Do not create.");
    }
    static boolean isLeapYear(final int year) {
        return year % 400 == 0 && year % 4000 != 0 || year % 4 == 0 && year % 100 != 0;
    }
}
