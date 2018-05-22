package cz.linkedlist

import org.springframework.util.concurrent.ListenableFuture
import org.springframework.util.concurrent.ListenableFutureCallback
import org.springframework.util.concurrent.SettableListenableFuture

import java.util.ArrayList
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author Martin Macko <https:></https:>//github.com/LinkedList>
 */
object AsyncUtil {

    fun <T> allOf(futures: List<ListenableFuture<T>>): ListenableFuture<List<T>> {

        // we will return this ListenableFuture, and modify it from within callbacks on each input future
        val groupFuture = SettableListenableFuture<List<T>>()

        // use a defensive shallow copy of the futures list, to avoid errors that could be caused by
        // someone inserting/removing a future from `futures` list after they call this method
        val futuresCopy = ArrayList(futures)

        // Count the number of completed futures with an AtomicInt (to avoid race conditions)
        val resultCount = AtomicInteger(0)
        for (i in futuresCopy.indices) {
            futuresCopy[i].addCallback(object : ListenableFutureCallback<T> {
                override fun onSuccess(result: T) {
                    val thisCount = resultCount.incrementAndGet()

                    // if this is the last result, build the ArrayList and complete the GroupFuture
                    if (thisCount == futuresCopy.size) {
                        val resultList = ArrayList<T>(futuresCopy.size)
                        try {
                            for (future in futuresCopy) {
                                resultList.add(future.get())
                            }
                            groupFuture.set(resultList)
                        } catch (e: Exception) {
                            // this should never happen, but future.get() forces us to deal with this exception.
                            groupFuture.setException(e)
                        }

                    }
                }

                override fun onFailure(throwable: Throwable) {
                    groupFuture.setException(throwable)

                    // if one future fails, don't waste effort on the others
                    for (future in futuresCopy) {
                        future.cancel(true)
                    }
                }
            })
        }

        return groupFuture
    }
}
