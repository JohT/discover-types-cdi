//Copyright 2018 Johannes Troppacher
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
package org.discovertypes.cdi;

import static java.util.Arrays.asList;
import static org.discovertypes.cdi.DiscoveredAnnotationTestcases.DEPRECATED;
import static org.discovertypes.cdi.DiscoveredAnnotationTestcases.IGNORE;
import static org.discovertypes.cdi.DiscoveredAnnotationTestcases.NAMED;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Documented;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Named;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DiscoveredTypesTest {

	private DiscoveredType stringWithIgnore = DiscoveredType.of(String.class)
			.withAdditionalAnnotation(IGNORE.build());
	private DiscoveredType integerWithDeprecated = DiscoveredType.of(Integer.class)
			.withAdditionalAnnotation(DEPRECATED.build());
	private DiscoveredType longWithIgnoreAndNamed = DiscoveredType.of(Long.class)
			.withAdditionalAnnotation(IGNORE.build())
			.withAdditionalAnnotation(NAMED.build());

	/**
	 * class under test.
	 */
	private DiscoveredTypes discoveredTypes = new DiscoveredTypes();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void setUp() {
		discoveredTypes.initializeWith(asList(stringWithIgnore, integerWithDeprecated, longWithIgnoreAndNamed));
	}

	@Test
	public void filteredByAnnotation() {
		Collection<DiscoveredType> byAnnotation = discoveredTypes.annotatedWith(Ignore.class);
		assertThat(byAnnotation, hasItem(stringWithIgnore));
		assertThat(byAnnotation, hasItem(longWithIgnoreAndNamed));
		assertEquals(2, byAnnotation.size());
	}

	@Test
	public void annotatedWithAnyOfSingleAnnotation() {
		Collection<DiscoveredType> byAnnotation = discoveredTypes.annotatedWithAnyOf(asList(Ignore.class));
		assertThat(byAnnotation, hasItem(stringWithIgnore));
		assertThat(byAnnotation, hasItem(longWithIgnoreAndNamed));
		assertEquals(2, byAnnotation.size());
	}

	@Test
	public void filteredByAnyOfTheGivenAnnotations() {
		Collection<DiscoveredType> byAnnotation = discoveredTypes
				.annotatedWithAnyOf(asList(Ignore.class, Deprecated.class));
		assertThat(byAnnotation, hasItem(stringWithIgnore));
		assertThat(byAnnotation, hasItem(integerWithDeprecated));
		assertThat(byAnnotation, hasItem(longWithIgnoreAndNamed));
		assertEquals(3, byAnnotation.size());
	}

	@Test
	public void providesAnnotationContent() {
		Collection<DiscoveredType> byAnnotation = discoveredTypes.annotatedWith(Named.class);
		Collection<String> suppresWarningValues = namedAnnotationValuesFrom(byAnnotation);
		assertThat(suppresWarningValues, hasItem("testname"));
		assertEquals(1, byAnnotation.size());
	}

	@Test
	public void emptyListWhenAnnotationTypeNotFound() {
		Collection<DiscoveredType> byAnnotation = discoveredTypes.annotatedWith(Documented.class);
		assertTrue(byAnnotation.toString(), byAnnotation.isEmpty());
	}

	@Test
	public void allTypes() {
		List<DiscoveredType> allTypes = iterableAsList(discoveredTypes);
		assertThat(allTypes, hasItems(stringWithIgnore, longWithIgnoreAndNamed, integerWithDeprecated));
		assertEquals(3, allTypes.size());
	}

	@Test
	public void discoveredTypesOfClassList() {
		discoveredTypes = DiscoveredTypes.of(asList(String.class, Integer.class));
		List<DiscoveredType> allTypes = iterableAsList(discoveredTypes);
		assertThat(allTypes, hasItem(DiscoveredType.ofDiscoverable(String.class)));
		assertThat(allTypes, hasItem(DiscoveredType.ofDiscoverable(Integer.class)));
		assertEquals(2, allTypes.size());
	}

	@Test
	public void failIfSomeoneTriesToAddAnnotationsASecondTimeAfterStartUp() {
		exception.expect(IllegalStateException.class);
		exception.expectMessage("startup");
		exception.expectMessage("May only be called once");
		discoveredTypes.initializeWith(asList(stringWithIgnore));
	}

	private static Collection<String> namedAnnotationValuesFrom(Collection<DiscoveredType> types) {
		Collection<String> values = new ArrayList<>();
		for (DiscoveredType discoveredType : types) {
			values.addAll(asList(discoveredType.getAnnotation(Named.class).value()));
		}
		return values;
	}

	private static <T> List<T> iterableAsList(Iterable<T> iterable) {
		List<T> types = new ArrayList<>();
		for (T element : iterable) {
			types.add(element);
		}
		return types;
	}

}
