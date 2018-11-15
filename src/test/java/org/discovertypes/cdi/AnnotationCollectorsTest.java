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

import static java.util.EnumSet.complementOf;
import static org.discovertypes.cdi.AnnotationCollectorsTestcases.testAnnotation;
import static org.discovertypes.cdi.AnnotationCollectorsTestcases.testMetaAnnotation;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;

import org.discovertypes.cdi.AnnotationCollectorsTestcases.TestAnnotatedConstructor;
import org.discovertypes.cdi.AnnotationCollectorsTestcases.TestAnnotatedConstructorParameter;
import org.discovertypes.cdi.AnnotationCollectorsTestcases.TestAnnotatedField;
import org.discovertypes.cdi.AnnotationCollectorsTestcases.TestAnnotatedMethod;
import org.discovertypes.cdi.AnnotationCollectorsTestcases.TestAnnotatedMethodParameter;
import org.discovertypes.cdi.AnnotationCollectorsTestcases.TestAnnotatedSuperClass;
import org.discovertypes.cdi.AnnotationCollectorsTestcases.TestSubClassOfAnnotatedSuper;
import org.junit.Test;

public class AnnotationCollectorsTest {

	private Collection<DiscoveredAnnotation> annotations = new ArrayList<>();

	/**
	 * class under test.
	 */
	private AnnotationCollectors collector;

	@Test
	public void typeAnnotations() {
		collector = AnnotationCollectors.TYPE;
		collector.collectAnnotationsOf(TestAnnotatedSuperClass.class, annotations);
		assertAllAnnotationsFound();
	}

	@Test
	public void allAnnotationsOfType() {
		collector = AnnotationCollectors.TYPE;
		AnnotationCollectors.collectAllAnnotationsOf(TestAnnotatedSuperClass.class, annotations);
		assertAllAnnotationsFound();
	}

	@Test
	public void noTypeAnnotations() {
		for (AnnotationCollectors collector : complementOf(EnumSet.of(AnnotationCollectors.TYPE))) {
			collector.collectAnnotationsOf(TestAnnotatedSuperClass.class, annotations);
			assertNoAnnotationsFound();
		}
	}

	@Test
	public void superClassAnnotations() {
		collector = AnnotationCollectors.SUPER_TYPES;
		collector.collectAnnotationsOf(TestSubClassOfAnnotatedSuper.class, annotations);
		assertAllAnnotationsFound();
	}

	@Test
	public void allAnnotationsOfSuperClass() {
		collector = AnnotationCollectors.SUPER_TYPES;
		AnnotationCollectors.collectAllAnnotationsOf(TestSubClassOfAnnotatedSuper.class, annotations);
		assertAllAnnotationsFound();
	}

	@Test
	public void noSuperClassAnnotations() {
		for (AnnotationCollectors collector : complementOf(EnumSet.of(AnnotationCollectors.SUPER_TYPES))) {
			collector.collectAnnotationsOf(TestSubClassOfAnnotatedSuper.class, annotations);
			assertNoAnnotationsFound();
		}
	}

	@Test
	public void constructorAnnotations() {
		collector = AnnotationCollectors.CONSTRUCTORS;
		collector.collectAnnotationsOf(TestAnnotatedConstructor.class, annotations);
		assertAllAnnotationsFound();
	}

	@Test
	public void allAnnotationsOfConstructor() {
		collector = AnnotationCollectors.CONSTRUCTORS;
		AnnotationCollectors.collectAllAnnotationsOf(TestAnnotatedConstructor.class, annotations);
		assertAllAnnotationsFound();
	}

	@Test
	public void noConstructorAnnotations() {
		for (AnnotationCollectors collector : complementOf(EnumSet.of(AnnotationCollectors.CONSTRUCTORS))) {
			collector.collectAnnotationsOf(TestAnnotatedConstructor.class, annotations);
			assertNoAnnotationsFound();
		}
	}

	@Test
	public void constructorParameterAnnotations() {
		collector = AnnotationCollectors.CONSTRUCTOR_PARAMETERS;
		collector.collectAnnotationsOf(TestAnnotatedConstructorParameter.class, annotations);
		assertAllAnnotationsFound();
	}

	@Test
	public void allAnnotationsOfConstructorParameters() {
		collector = AnnotationCollectors.CONSTRUCTOR_PARAMETERS;
		AnnotationCollectors.collectAllAnnotationsOf(TestAnnotatedConstructorParameter.class, annotations);
		assertAllAnnotationsFound();
	}

	@Test
	public void noConstructorParameterAnnotations() {
		for (AnnotationCollectors collector : complementOf(EnumSet.of(AnnotationCollectors.CONSTRUCTOR_PARAMETERS))) {
			collector.collectAnnotationsOf(TestAnnotatedConstructorParameter.class, annotations);
			assertNoAnnotationsFound();
		}
	}

	@Test
	public void methodAnnotations() {
		collector = AnnotationCollectors.METHODS;
		collector.collectAnnotationsOf(TestAnnotatedMethod.class, annotations);
		assertAllAnnotationsFound();
	}

	@Test
	public void allAnnotationsOfMethod() {
		collector = AnnotationCollectors.METHODS;
		AnnotationCollectors.collectAllAnnotationsOf(TestAnnotatedMethod.class, annotations);
		assertAllAnnotationsFound();
	}

	@Test
	public void noMethodAnnotations() {
		for (AnnotationCollectors collector : complementOf(EnumSet.of(AnnotationCollectors.METHODS))) {
			collector.collectAnnotationsOf(TestAnnotatedMethod.class, annotations);
			assertNoAnnotationsFound();
		}
	}

	@Test
	public void methodParameterAnnotations() {
		collector = AnnotationCollectors.METHOD_PARAMETERS;
		collector.collectAnnotationsOf(TestAnnotatedMethodParameter.class, annotations);
		assertAllAnnotationsFound();
	}

	@Test
	public void allAnnotationsOfMethodParameter() {
		collector = AnnotationCollectors.METHOD_PARAMETERS;
		AnnotationCollectors.collectAllAnnotationsOf(TestAnnotatedMethodParameter.class, annotations);
		assertAllAnnotationsFound();
	}

	@Test
	public void noMethodParameterAnnotations() {
		for (AnnotationCollectors collector : complementOf(EnumSet.of(AnnotationCollectors.METHOD_PARAMETERS))) {
			collector.collectAnnotationsOf(TestAnnotatedMethodParameter.class, annotations);
			assertNoAnnotationsFound();
		}
	}

	@Test
	public void fieldAnnotations() {
		collector = AnnotationCollectors.FIELDS;
		collector.collectAnnotationsOf(TestAnnotatedField.class, annotations);
		assertAllAnnotationsFound();
	}

	@Test
	public void allAnnotationsFields() {
		collector = AnnotationCollectors.FIELDS;
		AnnotationCollectors.collectAllAnnotationsOf(TestAnnotatedField.class, annotations);
		assertAllAnnotationsFound();
	}

	@Test
	public void noFieldAnnotations() {
		for (AnnotationCollectors collector : complementOf(EnumSet.of(AnnotationCollectors.FIELDS))) {
			collector.collectAnnotationsOf(TestAnnotatedField.class, annotations);
			assertNoAnnotationsFound();
		}
	}

	private void assertAllAnnotationsFound() {
		assertThat(annotations, hasItem(new DiscoveredAnnotation(testAnnotation(), collector.getLocation())));
		assertThat(annotations, hasItem(new DiscoveredAnnotation(testMetaAnnotation(), collector.getLocation())));
		assertThat(annotations.size(), is(2));
	}

	private void assertNoAnnotationsFound() {
		assertTrue(annotations.isEmpty());
	}
}
