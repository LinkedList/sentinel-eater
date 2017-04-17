package cz.linkedlist;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private List<TileSet.Contents> contents = new ArrayList<>();

    public DownloadTask() {
    }

}
