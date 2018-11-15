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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.inject.Stereotype;

/**
 * Marks a type, an annotation, or an annotation combined of other annotations
 * ("meta-annotation") that it (static type and annotations) can be queried by
 * injecting the bean {@link DiscoveredTypes}.
 * <p>
 * The CDI Extension in this module discovers all beans directly annotated with
 * {@link @Discoverable}, or beans, that are indirectly annotated with
 * annotations, that contain {@link @Discoverable} (e.g. {@link Stereotype}s).
 * <p>
 * It also discovered beans that contain methods with {@link @Discoverable}
 * annotations. After startup, all the annotated classes and their contained
 * annotations (including their settings) can be queried using {@link @Inject}
 * {@link DiscoveredTypes}.
 * 
 * @author Johannes Troppacher
 */
@Documented
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER,
		ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Discoverable {

	/**
	 * If the annotations is not meant to mark a cdi-bean, {@link #ignoreBean()}
	 * should be set to <code>false</code>. Default is <code>true</code>, so
	 * {@link Discoverable}-Beans get registered.
	 * 
	 * @return <code>true</code> if the bean should not be registered.
	 */
	boolean ignoreBean() default false;
}
