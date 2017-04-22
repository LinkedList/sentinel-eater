package cz.linkedlist;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
public class AsyncUtilTest {

    @Test
    public void testAllOf() {
        ListenableFuture<Integer> future1 = new AsyncResult<>(1);
        ListenableFuture<Integer> future2 = new AsyncResult<>(2);

        final ListenableFuture<List<Integer>> listListenableFuture = AsyncUtil.allOf(Arrays.asList(future1, future2));
        listListenableFuture.addCallback(list -> {
            assertThat(list, hasSize(2));
            assertThat(list, hasItem(1));
            assertThat(list, hasItem(2));
        }, throwable -> Assert.fail());

    }

}