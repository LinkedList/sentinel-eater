package cz.linkedlist;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SettableListenableFuture;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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

    @Test
    public void testAllOfFail() {
        SettableListenableFuture<Integer> future1 = new SettableListenableFuture<>();
        future1.setException(new NullPointerException());
        SettableListenableFuture<Integer> future2 = new SettableListenableFuture<>();
        future2.setException(new NullPointerException());

        final ListenableFuture<List<Integer>> listListenableFuture = AsyncUtil.allOf(Arrays.asList(future2));
        listListenableFuture.addCallback(list -> {}, throwable -> assertThat(listListenableFuture.isCancelled(), is(true)));
    }


}