package cz.linkedlist;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class TestContinuousCheckingService extends AbstractContinuousCheckingService {

    public TestContinuousCheckingService(JdbcTemplate jdbc) {
        super(jdbc);
    }

    @Override
    protected void createTask(DownloadTask task) {
    }
}
