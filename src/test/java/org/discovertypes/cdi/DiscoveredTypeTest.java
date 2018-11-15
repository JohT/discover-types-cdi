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
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

import javax.inject.Qualifier;

import org.discovertypes.cdi.DiscoveredTypeTest.MetaAnnotationWrappesIgnore;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@MetaAnnotationWrappesIgnore
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DiscoveredTypeTest {

	/**
	 * class under test.
	 */
	private DiscoveredType discoveredType = DiscoveredType.of(DiscoveredTypeTest.class);

	@Test
	public void containsAnnotatedClass() {
		assertEquals(DiscoveredTypeTest.class, discoveredType.getAnnotatedClass());
	}

	@Test
	public void containsAnnotationByType() {
		FixMethodOrder annotation = discoveredType.getAnnotation(FixMethodOrder.class);
		assertEquals(FixMethodOrder.class, annotation.annotationType());
		assertEquals(MethodSorters.NAME_ASCENDING, annotation.value());
	}

	@Test
	public void doesNotContainAnnotationByType() {
		Documented annotation = discoveredType.getAnnotation(Documented.class);
		assertNull(annotation);
	}

	@Test
	public void containsAnnotationLocationByType() {
		AnnotationLocation location = discoveredType.getAnnotationLocation(FixMethodOrder.class);
		assertEquals(AnnotationLocation.TYPE, location);
	}

	@Test
	public void doesNotContainAnnotationLocationByType() {
		AnnotationLocation location = discoveredType.getAnnotationLocation(Documented.class);
		assertNull(location);
	}

	@Test
	public void containsAllAnnotations() {
		Collection<DiscoveredAnnotation> annotations = discoveredType.getAnnotations();
		assertThat(annotations, hasItem(metaAnnotation()));
		assertThat(annotations, hasItem(fixMethodOrderAnnoation()));
	}

	@Test
	public void ofDiscoverableAddsDiscoverableAnnotation() {
		discoveredType = DiscoveredType.ofDiscoverable(String.class);
		Collection<DiscoveredAnnotation> annotations = discoveredType.getAnnotations();
		assertThat(annotations, hasItem(discoverableMethodAnnotation()));
	}

	@Test
	public void containsMetaAnnotationsOfAnnotations() {
		Collection<DiscoveredAnnotation> annotations = discoveredType.getAnnotations();
		assertThat(annotations, hasItem(ignoreAnnotation()));
	}

	@Test
	public void discoversCdiQualifiers() {
		Collection<Annotation> annotations = asList(discoveredType.getQualifiers());
		assertThat(annotations,
				hasItem(MetaAnnotationWrappesIgnore.class.getAnnotation(MetaAnnotationWrappesIgnore.class)));
	}

	@Test
	public void notDiscoverableAsNotIgnoredBean() {
		discoveredType = DiscoveredType.of(DiscoveredTypeTest.class);
		assertFalse(discoveredType.isIgnoredBean());
	}

	@Test
	public void discoverableNotIgnoredBean() {
		discoveredType = DiscoveredType.of(AnnotatedDiscoverable.class);
		assertFalse(discoveredType.isIgnoredBean());
	}

	@Test
	public void discoverableIgnoredBean() {
		discoveredType = DiscoveredType.of(AnnotatedDiscoverableIgnoredBean.class);
		assertTrue(discoveredType.isIgnoredBean());
	}

	@Test
	public void doesNotContainDiscoverableAnnotation() {
		Collection<DiscoveredAnnotation> annotations = discoveredType.getAnnotations();
		assertThat(annotations, hasItem(metaAnnotation()));
		assertThat(annotations, hasItem(fixMethodOrderAnnoation()));
	}

	@Test
	public void equalsIfSameType() {
		assertEquals(DiscoveredType.of(getClass()), DiscoveredType.of(getClass()));
	}

	@Test
	public void notEqualsIfDifferentAnnotations() {
		assertThat(DiscoveredType.of(getClass()), is(not(DiscoveredType.ofDiscoverable(getClass()))));
	}

	@Test
	public void notEqualComparedToNull() {
		assertFalse(DiscoveredType.of(getClass()).equals(null));
	}

	@Test
	@SuppressWarnings("unlikely-arg-type")
	public void notEqualComparedToAnotherType() {
		assertFalse(DiscoveredType.of(getClass()).equals(""));
	}

	private static DiscoveredAnnotation metaAnnotation() {
		return new DiscoveredAnnotation(DiscoveredTypeTest.class.getAnnotation(MetaAnnotationWrappesIgnore.class),
				AnnotationLocation.TYPE);
	}

	private static DiscoveredAnnotation fixMethodOrderAnnoation() {
		return new DiscoveredAnnotation(DiscoveredTypeTest.class.getAnnotation(FixMethodOrder.class),
				AnnotationLocation.TYPE);
	}

	private static DiscoveredAnnotation ignoreAnnotation() {
		return new DiscoveredAnnotation(MetaAnnotationWrappesIgnore.class.getAnnotation(Ignore.class),
				AnnotationLocation.TYPE);
	}

	private static DiscoveredAnnotation discoverableMethodAnnotation() {
		return new DiscoveredAnnotation(
				DiscoveredType.class.getAnnotation(Discoverable.class),
				AnnotationLocation.TYPE);
	}

	@MetaAnnotationWrappesIgnore // should be supported and should not lead to an endless loop.
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.TYPE })
	@Ignore(MetaAnnotationWrappesIgnore.REASON_FOR_IGNORE)
	@Qualifier
	public static @interface MetaAnnotationWrappesIgnore {

		public static final String REASON_FOR_IGNORE = "ReasonForIgnore";
	}

	@Discoverable
	public static class AnnotatedDiscoverable {

	}

	@Discoverable(ignoreBean = true)
	public static class AnnotatedDiscoverableIgnoredBean {

	}
}