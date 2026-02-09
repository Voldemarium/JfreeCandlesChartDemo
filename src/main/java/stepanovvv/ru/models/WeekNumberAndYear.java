package stepanovvv.ru.models;

import lombok.Data;


@Data
public class WeekNumberAndYear {
    int numberOfWeek;
    int year;

    public WeekNumberAndYear(int numberOfWeek, int year) {
        this.numberOfWeek = numberOfWeek;
        this.year = year;
    }

    public WeekNumberAndYear(WeekNumberAndYear weekNumberAndYear) {
        this.numberOfWeek = weekNumberAndYear.getNumberOfWeek();
        this.year = weekNumberAndYear.getYear();
    }
}
