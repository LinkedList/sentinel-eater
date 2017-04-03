package cz.linkedlist;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

/**
 * Class representing a single continuous checking task
 *
 * @author Martin Macko <https://github.com/LinkedList>.
 */
@Data
@AllArgsConstructor
public class DownloadTask {

    private UTMCode utm;
    private Double cloudiness;
    private LocalDate date;

    public DownloadTask() {
    }
}
