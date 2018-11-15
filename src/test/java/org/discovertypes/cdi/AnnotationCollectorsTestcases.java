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

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.Ignore;

@Ignore
class AnnotationCollectorsTestcases {

	public static final DiscoveredAnnotation discoveredTypeOf(Annotation annotation) {
		return new DiscoveredAnnotation(annotation, AnnotationLocation.TYPE);
	}

	public static final TestAnnotation testAnnotation() {
		return TestMetaAnnotation.class.getAnnotation(TestAnnotation.class);
	}

	public static final TestMetaAnnotation testMetaAnnotation() {
		return TestAnnotatedSuperClass.class.getAnnotation(TestMetaAnnotation.class);
	}

	@Target({ ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
	@Retention(RetentionPolicy.RUNTIME)
	@TestAnnotation
	public static @interface TestMetaAnnotation {

	}

	@Target({ ElementType.TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface TestAnnotation {

	}

	@TestMetaAnnotation
	public static class TestAnnotatedSuperClass {

	}

	public static class TestSubClassOfAnnotatedSuper extends TestAnnotatedSuperClass {

	}

	public static class TestAnnotatedMethod {

		@TestMetaAnnotation
		public void method() {

		}
	}

	public static class TestAnnotatedMethodParameter {

		public void method(@TestMetaAnnotation String parameter) {

		}
	}

	public static class TestAnnotatedConstructor {

		@TestMetaAnnotation
		public TestAnnotatedConstructor() {

		}
	}

	public static class TestAnnotatedConstructorParameter {

		public TestAnnotatedConstructorParameter(@TestMetaAnnotation String parameter) {

		}
	}

	public static class TestAnnotatedField {

		@TestMetaAnnotation
		public String value;

	}
}