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
package org.discovertypes.cdi.extension;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;

import org.discovertypes.cdi.Discoverable;
import org.discovertypes.cdi.DiscoveredType;
import org.discovertypes.cdi.DiscoveredTypes;

/**
 * This CDI-Extension tracks all {@link ProcessAnnotatedType}-Events of types
 * directly or indirectly annotated with {@link Discoverable}, and initializes
 * the injectable {@link DiscoveredTypes} with these.
 * <p>
 * {@link DiscoverableTypesCdiExtension} is suitable, if the use of
 * {@link Instance} is to slow (not cached), if you have no qualifier or type to
 * filter (e.g. just an "normal" annotation), or if you get type-errors during
 * iterating.
 * <p>
 * Before using this extension, try if you can get the same results using
 * either:
 * <ul>
 * <li><code>@Inject @Any Instance<MyInterface> allOfMyInterfaces; </code>
 * <li><code>@Inject @MyQualifier Instance<Object> allInstances;</code>
 * <li><code>@Inject @Any Instance<Object> allInstances; </code>
 * </ul>
 * 
 * @author Johannes Troppacher
 */
public class DiscoverableTypesCdiExtension implements Extension {

	private static final Logger LOGGER = Logger.getLogger(DiscoverableTypesCdiExtension.class.getName());

	private List<DiscoveredType> discoveredTypes = new ArrayList<>();

	<T> void processDiscoverable(@Observes @WithAnnotations({ Discoverable.class }) ProcessAnnotatedType<T> event) {
		DiscoveredType discoveredType = DiscoveredType.of(event.getAnnotatedType());
		discoveredTypes.add(discoveredType);
		if (discoveredType.isIgnoredBean()) {
			event.veto();
		}
		LOGGER.finer("discovered: " + event.getAnnotatedType().getJavaClass());
	}

	void afterDeploymentValidation(@Observes AfterDeploymentValidation event, BeanManager beanManager) {
		DiscoveredTypes discovered = lookupBean(DiscoveredTypes.class, beanManager);
		discovered.initializeWith(discoveredTypes);
		LOGGER.finer("all discovered types added to " + discovered);
	}

	@SuppressWarnings("unchecked")
	private static <T> T lookupBean(Class<T> type, BeanManager beanManager) {
		Bean<T> bean = (Bean<T>) beanManager.getBeans(type).iterator().next();
		CreationalContext<T> creationalContext = beanManager.createCreationalContext(bean);
		return (T) beanManager.getReference(bean, type, creationalContext);
	}

	@Override
	public String toString() {
		return "DiscoverableTypesCdiExtension [discoveredTypes=" + discoveredTypes + "]";
	}
}