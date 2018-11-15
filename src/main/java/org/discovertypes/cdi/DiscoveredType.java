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

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.enterprise.inject.spi.AnnotatedType;
import javax.inject.Qualifier;

/**
 * Represents a discovered type and its annotations.
 * <p>
 * It is not made to represent types that are not annotated. It is possible to
 * create a new {@link DiscoveredType} of a class that may not be annotated
 * using {@link #ofDiscoverable(Class)}. This results in a
 * {@link DiscoveredType}, that contains least one "default"
 * {@link Discoverable} annotation, even if the type originally isn'T annotated.
 * 
 * @author Johannes Troppacher
 */
@Discoverable(ignoreBean = true)
public class DiscoveredType implements Serializable {

	private static final long serialVersionUID = 200834838543847822L;

	private final Class<?> annotatedClass;
	private final Collection<Annotation> qualifiers = new ArrayList<>();
	private final Map<Class<? extends Annotation>, DiscoveredAnnotation> annotationTypes = new HashMap<>();

	/**
	 * Creates a new {@link DiscoveredType} of the given {@link Class}.
	 * 
	 * @param annotatedType - {@link Class}
	 * @return {@link DiscoveredType}
	 */
	public static <T> DiscoveredType of(Class<T> annotatedType) {
		return new DiscoveredType(annotatedType);
	}

	/**
	 * Creates a new {@link DiscoveredType} of the given {@link AnnotatedType}.
	 * 
	 * @param annotatedType - {@link AnnotatedType}
	 * @return {@link DiscoveredType}
	 */
	public static <T> DiscoveredType of(AnnotatedType<T> annotatedType) {
		return new DiscoveredType(annotatedType.getJavaClass());
	}

	/**
	 * Creates a new {@link DiscoveredType} of the given {@link Class} and treats it
	 * like it is annotated with the {@link Discoverable}-Annotation.
	 * <p>
	 * By calling {@link #getAnnotation(Class)} with <code>@Discoverable</code> as
	 * parameter, a default result is returned, event if the class originally isn't
	 * annotated with {@link Discoverable}.
	 * 
	 * @param annotatedType - {@link Class}
	 * @return {@link DiscoveredType}
	 */
	public static <T> DiscoveredType ofDiscoverable(Class<T> annotatedType) {
		return of(annotatedType).withAdditionalAnnotation(new DiscoveredAnnotation(
				DiscoveredType.class.getAnnotation(Discoverable.class), AnnotationLocation.TYPE));
	}

	protected DiscoveredType(Class<?> javaClass) {
		this.annotatedClass = javaClass;
		getAllQualifiers(this.qualifiers, asList(javaClass.getAnnotations()));
		for (DiscoveredAnnotation annotation : AnnotationCollectors.allAnnotationsOf(javaClass)) {
			withAdditionalAnnotation(annotation);
		}
	}

	protected DiscoveredType withAdditionalAnnotation(DiscoveredAnnotation annotation) {
		annotationTypes.put(annotation.annotationType(), annotation);
		return this;
	}

	/**
	 * Gets the type as {@link Class}.
	 * 
	 * @return {@link Class}
	 */
	public Class<?> getAnnotatedClass() {
		return annotatedClass;
	}

	/**
	 * Gets all annotations, including method-annotations, field-annotations and all
	 * indirect ones (annotations of these annotations).
	 * 
	 * @return
	 */
	public Set<Class<? extends Annotation>> getAnnotationTypes() {
		return Collections.unmodifiableSet(annotationTypes.keySet());
	}

	/**
	 * Gets all annotations of the {@link DiscoveredType}, or
	 * 
	 * @return
	 */
	public Collection<DiscoveredAnnotation> getAnnotations() {
		return Collections.unmodifiableCollection(annotationTypes.values());
	}

	/**
	 * Gets the annotation of the given annoation type or <code>null</code>, if the
	 * {@link DiscoveredType} is not annotated with it.
	 * 
	 * @param type - {@link Class}
	 * @return {@link Annotation}
	 */
	@SuppressWarnings("unchecked")
	public <T extends Annotation> T getAnnotation(Class<T> type) {
		if (!annotationTypes.containsKey(type)) {
			return null;
		}
		return (T) annotationTypes.get(type).getAnnotation();
	}

	/**
	 * Gets the {@link AnnotationLocation} of the given annotation type or
	 * <code>null</code>, if the {@link DiscoveredType} is not annotated with it.
	 * 
	 * @param type - {@link Class}
	 * @return {@link AnnotationLocation}
	 */
	public <T> AnnotationLocation getAnnotationLocation(Class<T> type) {
		if (!annotationTypes.containsKey(type)) {
			return null;
		}
		return annotationTypes.get(type).getLocation();
	}

	/**
	 * Gets the CDI qualifier annotations.
	 * 
	 * @return {@link Collection}
	 */
	public Annotation[] getQualifiers() {
		return qualifiers.toArray(new Annotation[qualifiers.size()]);
	}

	/**
	 * Is <code>true</code>, if the {@link DiscoveredType} is annotated with
	 * {@link Discoverable}, and {@link Discoverable#ignoreBean()} is set to
	 * <code>true</code>. In every other case the result is <code>false</code>.
	 * 
	 * @return <code>true</code>, if fulfilled.
	 */
	public boolean isIgnoredBean() {
		Discoverable annotation = getAnnotation(Discoverable.class);
		return (annotation != null) ? annotation.ignoreBean() : false;
	}

	private static void getAllQualifiers(final Collection<? super Annotation> qualifiers,
			Collection<? extends Annotation> annotations) {
		for (Annotation annotation : annotations) {
			if (annotation.annotationType().isAnnotationPresent(Qualifier.class)) {
				qualifiers.add(annotation);
			}
		}
	}

	@Override
	public boolean equals(final Object other) {
		if (other == null) {
			return false;
		}
		if (!getClass().equals(other.getClass())) {
			return false;
		}
		DiscoveredType castOther = (DiscoveredType) other;
		return Objects.equals(annotatedClass, castOther.annotatedClass)
				&& Objects.equals(annotationTypes, castOther.annotationTypes);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(annotatedClass);
	}

	@Override
	public String toString() {
		return "DiscoveredType [annotatedClass=" + annotatedClass + ", qualifiers=" + qualifiers + ", annotationTypes="
				+ annotationTypes + "]";
	}
}