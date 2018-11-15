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
package org.discovertypes.cdi.example;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;

import org.discovertypes.cdi.example.configuration.ExampleConfiguration;
import org.discovertypes.cdi.example.method.MetaAnnotatedMethodBean;
import org.discovertypes.cdi.example.type.MetaAnnotatedType;
import org.discovertypes.cdi.example.type.OriginalAnnotatedType;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.Test;

/**
 * "Unit-Integration-Test" using a embedded CDI container to test the discovery
 * of original- and meta-annotated types.
 * 
 * @author Johannes Troppacher
 */
public class ExampleConfigurationTest {

	@Test
	public void startUpConfigurationExample() {
		try (WeldContainer container = new Weld().initialize()) {
			ExampleConfiguration configuration = container.select(ExampleConfiguration.class).get();
			checkTypeAnnotated(configuration);
			checkMethodAnnotated(configuration);
		}
	}

	private void checkTypeAnnotated(ExampleConfiguration configuration) {
		assertThat(configuration.getOriginalTypeAnnotated(), hasItem(OriginalAnnotatedType.class));
		assertThat(configuration.getOriginalTypeAnnotated(), hasItem(MetaAnnotatedType.class));
		assertThat(configuration.getMetaTypeAnnotated(), hasItem(OriginalAnnotatedType.class));
		assertThat(configuration.getMetaTypeAnnotated(), hasItem(MetaAnnotatedType.class));
	}

	private void checkMethodAnnotated(ExampleConfiguration configuration) {
		// Method-Annotations do no mark their type as "discoverable".
		// Only the meta-annotated and thereby as discoverable marked bean
		// is discovered. But it is also discovered/registered under the name of the
		// original annotation.
		// This is because the CDI extension only discovers "Discoverable" annotated
		// types. Without filtering/restriction negative performance effects may be
		// possible. It is always possible to annotate the type containing the methods
		// though (workaround).
		assertThat(configuration.getOriginalMethodAnnotated(), hasItem(MetaAnnotatedMethodBean.class));
		assertThat(configuration.getMetaMethodAnnotated(), hasItem(MetaAnnotatedMethodBean.class));
	}
}