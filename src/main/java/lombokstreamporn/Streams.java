package lombokstreamporn;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toCollection;

import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Stream;

import com.google.common.base.Equivalence;
import com.google.common.base.Equivalence.Wrapper;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Streams {

	public <T> Predicate<T> alreadySeen(Function<? super T, ?> keyExtractor) {
		Set<Object> seen = ConcurrentHashMap.newKeySet();
		return e -> seen.add(keyExtractor.apply(e));
	}

	public <T> Stream<T> distinct(Stream<T> stream, Function<? super T, ? extends Object> keyExtractor) {
		return stream.filter(alreadySeen(keyExtractor));
	}
	
	// -----------------------------------------------------------------------------------------------------------
	// - unused code
	// -----------------------------------------------------------------------------------------------------------

	// using guava
	public <T> Stream<T> distinct_(Stream<T> stream,
			com.google.common.base.Function<? super T, ? extends Object> keyExtractor) {
		return stream.map(Equivalence.equals().onResultOf(keyExtractor)::wrap).distinct().map(Wrapper::get);
	}

	// another approach is collecting to a TreeSet (but this is not lazy like the
	// two examples above
	public <T, U extends Comparable<? super U>> Collector<T, ?, TreeSet<T>> distinctCollector(
			Function<? super T, ? extends U> keyExtractor) {
		return toCollection(() -> new TreeSet<T>(comparing(keyExtractor)));
	}

}
