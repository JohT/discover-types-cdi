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

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.enterprise.inject.Stereotype;

/**
 * Strategies to collect {@link Annotation}s of a {@link Class}.
 * <p>
 * Feature Reference: "ProcessAnnotatedTypeEventResolvable" (wink-core
 * Implementation)
 * 
 * @author Johannes Troppacher
 */
enum AnnotationCollectors {

	TYPE(AnnotationLocation.TYPE) {
		@Override
		public void collectAnnotationsOf(Class<?> type, final Collection<DiscoveredAnnotation> annotations) {
			addAnnotatedElement(type, annotations);
		}
	},
	SUPER_TYPES(AnnotationLocation.SUPER_TYPE) {
		@Override
		public void collectAnnotationsOf(Class<?> type, final Collection<DiscoveredAnnotation> annotations) {
			Class<?> superclass = type.getSuperclass();
			while (!superclass.equals(Object.class)) {
				addAnnotatedElement(superclass, annotations);
				superclass = superclass.getSuperclass();
			}
		}
	},
	FIELDS(AnnotationLocation.FIELD) {
		@Override
		public void collectAnnotationsOf(Class<?> type, final Collection<DiscoveredAnnotation> annotations) {
			addAnnotatedElements(type.getFields(), annotations);

		}
	},
	CONSTRUCTORS(AnnotationLocation.CONSTRUCTOR) {
		@Override
		public void collectAnnotationsOf(Class<?> type, final Collection<DiscoveredAnnotation> annotations) {
			addAnnotatedElements(type.getConstructors(), annotations);

		}
	},
	CONSTRUCTOR_PARAMETERS(AnnotationLocation.CONSTRUCTOR_PARAMETER) {
		@Override
		public void collectAnnotationsOf(Class<?> type, final Collection<DiscoveredAnnotation> annotations) {
			for (Constructor<?> element : type.getConstructors()) {
				addAnnotatedElements(element.getParameters(), annotations);
			}

		}
	},
	METHODS(AnnotationLocation.METHOD) {
		@Override
		public void collectAnnotationsOf(Class<?> type, final Collection<DiscoveredAnnotation> annotations) {
			addAnnotatedElements(type.getMethods(), annotations);

		}
	},
	METHOD_PARAMETERS(AnnotationLocation.METHOD_PARAMETER) {
		@Override
		public void collectAnnotationsOf(Class<?> type, final Collection<DiscoveredAnnotation> annotations) {
			for (Method element : type.getMethods()) {
				addAnnotatedElements(element.getParameters(), annotations);
			}

		}
	},

	;

	private static final int MAX_RECUSIVE_DEPTH = 1;
	private static final List<Class<? extends Annotation>> IGNORED_ANNOTATIONS = asList(
			Target.class, Retention.class, Stereotype.class, Documented.class);

	private final AnnotationLocation location;

	private AnnotationCollectors(AnnotationLocation location) {
		this.location = location;
	}

	protected AnnotationLocation getLocation() {
		return location;
	}

	/**
	 * Adds the selected kind of annotations of the given type to the
	 * {@link Collection} of {@link Annotation}.
	 * 
	 * @param type        - {@link Class}
	 * @param annotations - {@link Collection} of {@link Annotation}.
	 */
	public abstract void collectAnnotationsOf(Class<?> type, final Collection<DiscoveredAnnotation> annotations);

	/**
	 * Gets all {@link Annotation}s of the given type as a {@link Collection}.
	 * 
	 * @param type       - {@link Class}
	 * @param collection - {@link Collection} of {@link Annotation}.
	 */
	public static final Collection<DiscoveredAnnotation> allAnnotationsOf(Class<?> type) {
		Collection<DiscoveredAnnotation> collection = new ArrayList<>();
		collectAllAnnotationsOf(type, collection);
		return collection;
	}

	/**
	 * Adds all annotations of the given type to the {@link Collection} of
	 * {@link Annotation}.
	 * 
	 * @param type       - {@link Class}
	 * @param collection - {@link Collection} of {@link Annotation}.
	 */
	public static final void collectAllAnnotationsOf(Class<?> type, final Collection<DiscoveredAnnotation> collection) {
		for (AnnotationCollectors collector : values()) {
			collector.collectAnnotationsOf(type, collection);
		}
	}

	protected void addAnnotatedElements(AnnotatedElement[] annotated,
			final Collection<DiscoveredAnnotation> annotations) {
		for (AnnotatedElement annotatedElement : annotated) {
			addAnnotatedElement(annotatedElement, annotations);
		}
	}

	protected void addAnnotatedElement(AnnotatedElement annotated,
			final Collection<DiscoveredAnnotation> annotations) {
		addAnnotatedElement(annotated, annotations, 0);
	}

	private void addAnnotatedElement(AnnotatedElement annotated,
			final Collection<DiscoveredAnnotation> annotations,
			int recursiveDepth) {
		for (Annotation annotation : annotated.getAnnotations()) {
			Class<? extends Annotation> annotationType = annotation.annotationType();
			if (IGNORED_ANNOTATIONS.contains(annotationType)) {
				continue;
			}
			annotations.add(new DiscoveredAnnotation(annotation, getLocation()));
			if (recursiveDepth < MAX_RECUSIVE_DEPTH) {
				addAnnotatedElement(annotationType, annotations, recursiveDepth + 1);
			}
		}
	}
}
