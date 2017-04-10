package cz.linkedlist;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author Martin Macko <https://github.com/LinkedList>
 */
public class AsyncUtil {

    @SuppressWarnings("unchecked")
    public static <T> CompletableFuture<List<T>> all(List<CompletableFuture<T>> futures) {
        CompletableFuture<T>[] arr = new CompletableFuture[futures.size()];

        for (int i = 0; i < futures.size(); i++) {
            final CompletableFuture<T> f = futures.get(i);
            arr[i] = f;
        }

        CompletableFuture<Void> cfs = CompletableFuture.allOf(arr);

        return cfs.thenApply(v -> futures.stream().map(CompletableFuture::join).collect(Collectors.toList()));
    }
}
