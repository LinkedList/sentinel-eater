package cz.linkedlist;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Martin Macko <https://github.com/LinkedList>.
 */
@RequiredArgsConstructor
public abstract class AbstractContinuousCheckingService implements ContinuousCheckingService {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());
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
    @Autowired
    protected TileDownloader downloader;

    @Override
    public void register(UTMCode utm, Double cloudiness, List<TileSet.Contents> contents) {
        register(utm, cloudiness, contents, DEFAULT_CRON_TRIGGER);
    }

    @Override
    public void register(UTMCode utm, Double cloudiness, List<TileSet.Contents> contents, Trigger trigger) {
        log.info("Registering a new task: utm: {}, cloudiness: {}, contents: {}, trigger: {}", utm, cloudiness, contents, trigger);
        //Nasty hack, nasty hack :D
        StringBuilder contentsStr = new StringBuilder();
        contents.forEach(c -> contentsStr.append(c).append(","));

        jdbc.update(
                "insert into tasks VALUES (?, ?, ?, ?)",
                utm.toString(),
                LocalDate.now(),
                cloudiness,
                contentsStr.toString()
        );
        createTask(new DownloadTask(utm, cloudiness, LocalDate.now(), contents), trigger);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<DownloadTask> list() {
        return jdbc.query("select * from tasks", (resultSet, i) -> {
            DownloadTask task = new DownloadTask();
            task.setCloudiness(resultSet.getDouble("cloudiness"));
            task.setDate(resultSet.getDate("date").toLocalDate());
            task.setUtm(UTMCode.of(resultSet.getString("utm")));
            String contents = resultSet.getString("contents");
            if(StringUtils.hasLength(contents)) {
                final List<TileSet.Contents> collect = Arrays.stream(contents.split(","))
                    .map(TileSet.Contents::valueOf)
                        .collect(Collectors.toList());
                task.setContents(collect);
            } else {
                task.setContents(Collections.emptyList());
            }
            return task;
        });
    }

    protected abstract void createTask(DownloadTask task, Trigger trigger);
}
