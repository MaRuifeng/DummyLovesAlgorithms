package linkedList;

import org.junit.Assert;
import org.junit.Test;

public class LRUCachePlainTest {

    @Test
    public final void shouldBeAbleToAccessAllStoredEntriesWhenCapacityIsNotExceeded() throws Exception {
        LRUCachePlain cache = new LRUCachePlain(3);
        cache.set(1, 1);
        cache.set(2, 2);
        cache.set(3, 3);

        Assert.assertTrue(cache.contains(1));
        Assert.assertTrue(cache.contains(2));
        Assert.assertTrue(cache.contains(3));
    }

    @Test
    public final void shouldEvictTheLeastRecentlyUsedEntryWhenCapacityIsExceeded() throws Exception {
        LRUCachePlain cache = new LRUCachePlain(2);

        cache.set(1, 1);
        cache.set(2, 2);
        cache.set(3, 3);

        Assert.assertFalse(cache.contains(1));
        Assert.assertTrue(cache.contains(2));
        Assert.assertTrue(cache.contains(3));
    }

    @Test
    public final void shouldMakeAnEntryTheMostRecentlyUsedAfterGetCall() throws Exception {
        LRUCachePlain cache = new LRUCachePlain(2);

        cache.set(1, 1);
        cache.set(2, 2);

        cache.get(1);

        cache.set(3, 3);

        Assert.assertTrue(cache.contains(1));
        Assert.assertFalse(cache.contains(2));
        Assert.assertTrue(cache.contains(3));
    }

    @Test
    public final void shouldMakeAnEntryTheMostRecentlyUsedAfterSetCall() throws Exception {
        LRUCachePlain cache = new LRUCachePlain(2);

        cache.set(1, 1);
        cache.set(2, 2);
        cache.set(3, 3);

        cache.set(1, 11);

        Assert.assertTrue(cache.contains(1));
        Assert.assertFalse(cache.contains(2));
        Assert.assertTrue(cache.contains(3));
    }

    @Test (expected = Exception.class)
    public final void shouldThrowAnExceptionWhenInitializedWithNegativeCapacity() throws Exception {
        new LRUCachePlain(-1);
    }

    @Test (expected = Exception.class)
    public final void shouldThrowAnExceptionWhenInitializedWithZeroCapacity() throws Exception {
        new LRUCachePlain(0);
    }
}
