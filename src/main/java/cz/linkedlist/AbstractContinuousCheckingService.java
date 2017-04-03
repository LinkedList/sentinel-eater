package cz.linkedlist;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.Collection;

/**
 * @author Martin Macko <https://github.com/LinkedList>.
 */
@RequiredArgsConstructor
public abstract class AbstractContinuousCheckingService implements ContinuousCheckingService {

    private final JdbcTemplate jdbc;

    @Override
    public void register(UTMCode utm, Double cloudiness) {
        jdbc.update(
                "insert into tasks VALUES (?, ?, ?)",
                utm.toString(),
                LocalDate.now(),
                cloudiness
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<DownloadTask> list() {
        return jdbc.query("select * from tasks", new BeanPropertyRowMapper(DownloadTask.class));
    }

    protected abstract void createTask(DownloadTask task);
}
