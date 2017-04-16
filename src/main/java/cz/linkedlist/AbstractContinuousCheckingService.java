package cz.linkedlist;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.time.LocalDate;
import java.util.Collection;

/**
 * @author Martin Macko <https://github.com/LinkedList>.
 */
@RequiredArgsConstructor
public abstract class AbstractContinuousCheckingService implements ContinuousCheckingService {

    /**
     * Default is daily at 01:00
     */
    public static final CronTrigger DEFAULT_CRON_TRIGGER = new CronTrigger("0 0 1 * * ?");

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
        register(utm, cloudiness, DEFAULT_CRON_TRIGGER);
    }

    @Override
    public void register(UTMCode utm, Double cloudiness, CronTrigger trigger) {
        jdbc.update(
                "insert into tasks VALUES (?, ?, ?)",
                utm.toString(),
                LocalDate.now(),
                cloudiness
        );
        createTask(new DownloadTask(utm, cloudiness, LocalDate.now()), trigger);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<DownloadTask> list() {
        return jdbc.query("select * from tasks", new BeanPropertyRowMapper(DownloadTask.class));
    }

    protected abstract void createTask(DownloadTask task, CronTrigger trigger);
}
