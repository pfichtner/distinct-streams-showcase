package lombokstreamporn;

import static lombokstreamporn.Streams.distinctCollector;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import lombok.experimental.ExtensionMethod;

@ExtensionMethod(Streams.class)
class StreamFilterTest {

	@Test
	void canFilterViaDistinct() {
		Person[] persons = givenTwoPersonsWithIdenticalLastnames("Miller");
		Stream<Person> filteredStream = whenFilteringStreamOfPersons(persons);
		thenTheStreamOnlyContains(filteredStream, persons[0]);
	}

	@Test
	void canCollectDistinct() {
		Person[] persons = givenTwoPersonsWithIdenticalLastnames("Miller");
		Stream<Person> filteredStream = whenCollectingDistinct(persons);
		thenTheStreamOnlyContains(filteredStream, persons[0]);
	}

	Person[] givenTwoPersonsWithIdenticalLastnames(String lastname) {
		return new Person[] { //
				new Person("A", lastname), //
				new Person("B", lastname) //
		};
	}

	Stream<Person> whenFilteringStreamOfPersons(Person[] persons) {
		// this is where the lombok magic resists: java streams do NOT have a distinct
		// method with Function parameter
		return Arrays.stream(persons).distinct(Person::getLastname);
	}

	Stream<Person> whenCollectingDistinct(Person[] persons) {
		// here the distinct function is done using a terminal operation (collect), so
		// we do have to create a stream again
		return Arrays.stream(persons).collect(distinctCollector(Person::getLastname)).stream();
	}

	void thenTheStreamOnlyContains(Stream<Person> stream, Person person) {
		assertThat(stream).containsOnly(person);
	}

}
