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
package org.discovertypes.cdi.example.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.discovertypes.cdi.DiscoveredType;
import org.discovertypes.cdi.DiscoveredTypes;
import org.discovertypes.cdi.example.method.MetaMethodAnnotation;
import org.discovertypes.cdi.example.method.OriginalMethodAnnotation;
import org.discovertypes.cdi.example.type.MetaTypeAnnotation;
import org.discovertypes.cdi.example.type.OriginalTypeAnnotation;
import org.junit.Ignore;

/**
 * Example Configuration that collects/uses the discovered types.
 * 
 * @author Johannes Troppacher
 */
@Ignore
@ApplicationScoped
public class ExampleConfiguration {

	private final Collection<Class<?>> originalTypeAnnotated = new ArrayList<>();
	private final Collection<Class<?>> metaTypeAnnotated = new ArrayList<>();

	private final Collection<Class<?>> originalMethodAnnotated = new ArrayList<>();
	private final Collection<Class<?>> metaMethodAnnotated = new ArrayList<>();

	@Inject
	private DiscoveredTypes discoveredTypes;

	public Collection<Class<?>> getOriginalTypeAnnotated() {
		return Collections.unmodifiableCollection(originalTypeAnnotated);
	}

	public Collection<Class<?>> getMetaTypeAnnotated() {
		return Collections.unmodifiableCollection(metaTypeAnnotated);
	}

	public Collection<Class<?>> getOriginalMethodAnnotated() {
		return Collections.unmodifiableCollection(originalMethodAnnotated);
	}

	public Collection<Class<?>> getMetaMethodAnnotated() {
		return Collections.unmodifiableCollection(metaMethodAnnotated);
	}

	@PostConstruct
	public void postConstruct() {
		registerOriginalTypeAnnotated();
		registerMetaTypeAnnotated();
		registerOriginalMethodAnnotated();
		registerMetaMethodAnnotated();
	}

	private void registerOriginalTypeAnnotated() {
		for (DiscoveredType type : discoveredTypes.annotatedWith(OriginalTypeAnnotation.class)) {
			originalTypeAnnotated.add(type.getAnnotatedClass());
		}
	}

	private void registerMetaTypeAnnotated() {
		for (DiscoveredType type : discoveredTypes.annotatedWith(MetaTypeAnnotation.class)) {
			metaTypeAnnotated.add(type.getAnnotatedClass());
		}
	}

	private void registerOriginalMethodAnnotated() {
		for (DiscoveredType type : discoveredTypes.annotatedWith(OriginalMethodAnnotation.class)) {
			originalMethodAnnotated.add(type.getAnnotatedClass());
		}
	}

	private void registerMetaMethodAnnotated() {
		for (DiscoveredType type : discoveredTypes.annotatedWith(MetaMethodAnnotation.class)) {
			metaMethodAnnotated.add(type.getAnnotatedClass());
		}
	}

	@Override
	public String toString() {
		return "ExampleConfiguration [originalTypeAnnotated=" + originalTypeAnnotated + ", metaTypeAnnotated="
				+ metaTypeAnnotated + ", originalMethodAnnotated=" + originalMethodAnnotated + ", metaMethodAnnotated="
				+ metaMethodAnnotated + ", discoveredTypes=" + discoveredTypes + "]";
	}
}