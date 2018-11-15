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

import static java.util.Objects.requireNonNull;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.Objects;

/**
 * Represents the {@link Annotation} including the {@link AnnotationLocation},
 * where it had been discovered.
 * 
 * @author Johannes Troppacher
 */
public class DiscoveredAnnotation implements Serializable {

	private static final long serialVersionUID = 2481158664023200956L;

	private final Annotation annotation;
	private final AnnotationLocation location;

	@ConstructorProperties({ "annotation", "location" })
	public DiscoveredAnnotation(Annotation annotation, AnnotationLocation location) {
		this.annotation = requireNonNull(annotation, "annotation");
		this.location = requireNonNull(location, "location");
	}

	public Annotation getAnnotation() {
		return annotation;
	}

	public Class<? extends Annotation> annotationType() {
		return annotation.annotationType();
	}

	public AnnotationLocation getLocation() {
		return location;
	}

	@Override
	public boolean equals(final Object other) {
		if (other == null) {
			return false;
		}
		if (!getClass().equals(other.getClass())) {
			return false;
		}
		DiscoveredAnnotation castOther = (DiscoveredAnnotation) other;
		return Objects.equals(annotation, castOther.annotation) && Objects.equals(location, castOther.location);
	}

	@Override
	public int hashCode() {
		return Objects.hash(annotation, location);
	}

	@Override
	public String toString() {
		return "DiscoveredAnnotation [annotation=" + annotation + ", location=" + location + "]";
	}
}