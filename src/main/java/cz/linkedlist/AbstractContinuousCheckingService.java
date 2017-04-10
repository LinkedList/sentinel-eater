package cz.linkedlist;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.TaskScheduler;

import java.time.LocalDate;
import java.util.Collection;

/**
 * @author Martin Macko <https://github.com/LinkedList>.
 */
@RequiredArgsConstructor
public abstract class AbstractContinuousCheckingService implements ContinuousCheckingService {

    @Autowired
    private JdbcTemplate jdbc;
    @Autowired
    protected TileListingService listingService;
    @Autowired
    protected TileInfoService infoService;
    @Autowired
    protected TaskScheduler scheduler;

    @Override
    public void register(UTMCode utm, Double cloudiness) {
        jdbc.update(
                "insert into tasks VALUES (?, ?, ?)",
                utm.toString(),
                LocalDate.now(),
                cloudiness
        );
        createTask(new DownloadTask(utm, cloudiness, LocalDate.of(2016, 10, 1)));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<DownloadTask> list() {
        return jdbc.query("select * from tasks", new BeanPropertyRowMapper(DownloadTask.class));
    }

    protected abstract void createTask(DownloadTask task);
}
