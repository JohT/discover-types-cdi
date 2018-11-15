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
package org.discovertypes.cdi.example.type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.inject.Stereotype;

import org.discovertypes.cdi.Discoverable;

/**
 * Example custom Meta-Annotation that represents the
 * {@link OriginalTypeAnnotation} inside a project.
 * 
 * @author Johannes Troppacher
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Stereotype
@OriginalTypeAnnotation
@Discoverable(ignoreBean = true)
public @interface MetaTypeAnnotation {

}