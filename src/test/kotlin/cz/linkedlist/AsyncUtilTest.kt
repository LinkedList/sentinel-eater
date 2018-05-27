package cz.linkedlist

import org.junit.Assert
import org.junit.Test
import org.springframework.scheduling.annotation.AsyncResult
import org.springframework.util.concurrent.ListenableFuture
import org.springframework.util.concurrent.SettableListenableFuture

import java.util.Arrays

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>
 */
class AsyncUtilTest {

    @Test
    fun testAllOf() {
        val future1 = AsyncResult(1)
        val future2 = AsyncResult(2)

        val listListenableFuture = AsyncUtil.allOf(Arrays.asList<ListenableFuture<Int>>(future1, future2))
        listListenableFuture.addCallback({ list ->
            assertThat(list, hasSize(2))
            assertThat(list, hasItem(1))
            assertThat(list, hasItem(2))
        }) { throwable -> Assert.fail() }

    }

    @Test
    fun testAllOfFail() {
        val future1 = SettableListenableFuture<Int>()
        future1.setException(NullPointerException())
        val future2 = SettableListenableFuture<Int>()
        future2.setException(NullPointerException())

        val listListenableFuture = AsyncUtil.allOf(Arrays.asList(future2))
        listListenableFuture.addCallback({ list -> }) { throwable ->
            assertThat(
                listListenableFuture.isCancelled,
                `is`(true)
            )
        }
    }


}