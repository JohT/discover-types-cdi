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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import java.lang.annotation.Annotation;

import org.junit.Test;

public class DiscoveredAnnotationTest {

	private static final Annotation ANNOTATION = DiscoveredAnnotationTestcases.IGNORE.build().getAnnotation();
	private static final Annotation DIFFERENT_ANNOTATION = DiscoveredAnnotationTestcases.DEPRECATED.build()
			.getAnnotation();
	private static final AnnotationLocation LOCATION = AnnotationLocation.METHOD;
	/**
	 * class under test.
	 */
	private DiscoveredAnnotation discoveredAnnotation = createDiscoveredAnnotation();

	@Test
	public void fieldsContained() {
		assertEquals(ANNOTATION, discoveredAnnotation.getAnnotation());
		assertEquals(LOCATION, discoveredAnnotation.getLocation());
	}

	@Test
	public void equalWhenAllFieldsEqual() {
		DiscoveredAnnotation sameAnnotation = createDiscoveredAnnotation();
		assertThat(discoveredAnnotation, is(sameAnnotation));
	}

	@Test
	public void notEqualWhenAnnotationIsDifferent() {
		DiscoveredAnnotation sameAnnotation = createDiscoveredAnnotationWithDifferentAnnoation();
		assertThat(discoveredAnnotation, is(not(sameAnnotation)));
	}

	@Test
	public void notEqualWhenLocationIsDifferent() {
		DiscoveredAnnotation sameAnnotation = createDiscoveredAnnotationWithDifferentLocation();
		assertThat(discoveredAnnotation, is(not(sameAnnotation)));
	}

	@Test
	public void notEqualComparedToNull() {
		assertFalse(discoveredAnnotation.equals(null));
	}

	@Test
	public void notEqualComparedToAnotherType() {
		assertThat(discoveredAnnotation, is(not("")));
	}

	private static DiscoveredAnnotation createDiscoveredAnnotation() {
		return new DiscoveredAnnotation(ANNOTATION, LOCATION);
	}

	private static DiscoveredAnnotation createDiscoveredAnnotationWithDifferentAnnoation() {
		return new DiscoveredAnnotation(DIFFERENT_ANNOTATION, LOCATION);
	}

	private static DiscoveredAnnotation createDiscoveredAnnotationWithDifferentLocation() {
		return new DiscoveredAnnotation(ANNOTATION, AnnotationLocation.FIELD);
	}
}