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

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Typed;

/**
 * Represents all discovered (static) types and their annotations.
 * <ul>
 * <li>This bean can be injected using: <code>@Inject DiscoveredTypes
 * discoveredTypes;</code>
 * <li>The contained {@link DiscoveredType}s can be iterated directly using
 * <code>for (DiscoveredType type : discoveredTypes) {...}</code>
 * <li>To get all types annotated by a given annotation, use:
 * <code>Collection<DiscoveredType> typesByAnnotation = discoveredTypes.annotatedWith(...);</code>
 * </ul>
 * 
 * @author Johannes Troppacher
 */
@ApplicationScoped
@Typed({ DiscoveredTypes.class })
public class DiscoveredTypes implements Iterable<DiscoveredType>, Serializable {

	private static final long serialVersionUID = 3352821394722277602L;

	private final Map<Class<? extends Annotation>, List<DiscoveredType>> discoveredTypes = new HashMap<>();
	private AtomicBoolean discovered = new AtomicBoolean(false);

	/**
	 * Creates {@link DiscoveredTypes} to use it outside CDI programmatically.
	 * <p>
	 * Classes, that are not annotated in any way, can be queried using the
	 * {@link Discoverable}-Annotation.
	 * 
	 * @param types {@link Collection} of {@link Class}es.
	 * @return {@link DiscoveredTypes}
	 */
	public static final DiscoveredTypes of(Collection<Class<?>> types) {
		DiscoveredTypes discoveredTypes = new DiscoveredTypes();
		discoveredTypes.initializeWith(types.stream().map(DiscoveredType::ofDiscoverable).collect(Collectors.toList()));
		return discoveredTypes;
	}

	/**
	 * Sets the {@link DiscoveredType}s during startup.
	 * <p>
	 * May only be used internally to set the discovered types during startup by the
	 * CDI extension.
	 * 
	 * @param types - {@link Collection} of {@link DiscoveredType}s.
	 */
	public void initializeWith(Collection<? extends DiscoveredType> types) {
		if (discovered.getAndSet(true)) {
			throw new IllegalStateException("May only be called once by the cdi extension during system startup");
		}
		groupByAnnotationType(types);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<DiscoveredType> iterator() {
		return getDiscoveredTypes().iterator();
	}

	/**
	 * Gets the {@link Collection} of {@link DiscoveredType}s, that are annotated
	 * with the given {@link Annotation}-{@link Class} .
	 * 
	 * @param annotationType - {@link Class} of the {@link Annotation}.
	 * @return {@link Collection} of {@link DiscoveredType}s.
	 */
	public Collection<DiscoveredType> annotatedWith(Class<? extends Annotation> annotationType) {
		if (!this.discoveredTypes.containsKey(annotationType)) {
			return Collections.emptyList();
		}
		return new ArrayList<>(this.discoveredTypes.get(annotationType));
	}

	/**
	 * Gets a {@link Collection} of {@link DiscoveredType}s, that are annotated with
	 * any (at least one) of the given {@link Annotation}-{@link Class}-Types.
	 * <p>
	 * Use {@link Arrays#asList(Object...)} to convert semicolon separated
	 * annotation types or an array of annotation types as parameter.
	 * 
	 * @param annotationTypes - {@link Iterable} (e.g. a {@link List}) of
	 *                        {@link Annotation}s.
	 * @return {@link Collection} of {@link DiscoveredType}s.
	 */
	public Collection<DiscoveredType> annotatedWithAnyOf(Iterable<Class<? extends Annotation>> annotationTypes) {
		Set<DiscoveredType> discoveredTypes = new HashSet<>();
		for (Class<? extends Annotation> annotationType : annotationTypes) {
			discoveredTypes.addAll(annotatedWith(annotationType));
		}
		return discoveredTypes;
	}

	private Collection<DiscoveredType> getDiscoveredTypes() {
		Set<DiscoveredType> distinctDiscoveredTypes = new HashSet<>();
		for (List<DiscoveredType> discoveredTypeList : this.discoveredTypes.values()) {
			distinctDiscoveredTypes.addAll(discoveredTypeList);
		}
		return Collections.unmodifiableCollection(distinctDiscoveredTypes);
	}

	private void groupByAnnotationType(Iterable<? extends DiscoveredType> discovered) {
		for (DiscoveredType discoveredType : discovered) {
			for (Class<? extends Annotation> annotation : discoveredType.getAnnotationTypes()) {
				addDiscoveredType(discoveredType, annotation);
			}
		}
	}

	private void addDiscoveredType(DiscoveredType discoveredType, Class<? extends Annotation> annotation) {
		if (!discoveredTypes.containsKey(annotation)) {
			discoveredTypes.put(annotation, new ArrayList<>());
		}
		discoveredTypes.get(annotation).add(discoveredType);
	}

	@Override
	public String toString() {
		return "DiscoveredTypes [discoveredTypes=" + discoveredTypes + ", discovered=" + discovered + "]";
	}
}