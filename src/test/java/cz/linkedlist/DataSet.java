package cz.linkedlist;

import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
@Data
public class DataSet {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("uuuu/M/d/");

    private final UTMCode code;
    private final LocalDate date;
    private final Integer setOrder;

    public String toString() {
        return "tiles/" + code.toString() + date.format(DATE_FORMAT) + setOrder + "/";
    }
}
