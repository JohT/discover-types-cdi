CDI extension to discover types
==============

This project contains a simple CDI Extension that discovers types annotated with @Discoverable.
The CDI-Extension tracks all ProcessAnnotatedType events annotated (directly and indirectly) with @Discoverable, 
and provides the results through the injectable Bean DiscoveredTypes.

Before using this module, try if you can get the same results using either:
<li> @Inject @Any Instance<MyType> allOfMyInterfaces;
<li> @Inject @MyQualifier Instance<MyType> allInstances;
<li> @Inject @Any Instance<Object> allInstances;

This CDI-Extension is suitable, 
<li> if the use of Instance<> is to slow (its not cached)
<li> if it is not possible to filter by qualifier or by type (e.g. just by a "normal" annotation)
<li> if you get type errors during iterating
<li> if you want to use meta-annotations
<li> if you want to integrate non JavaEE libraries and frameworks

The types are discovered using the following CDI-event inside the extension during startup: <br>
<code> @Observes @WithAnnotations({ Discoverable.class }) ProcessAnnotatedType<T> event</code>

# Features
<li> Stores the discovered types cached (only once during startup) and indexed (in a Map) by annotation
<li> Supports meta-annotations
<li> Provides the discovered annotation and the location it had been discovered (type, field, method,...).
<li> Beans can be disabled (CDI veto) using <code>@Discoverable(ignoreBean = true)</code>

# Discovers
<li> classes annotated with <code>@Discoverable</code>
<li> classes with constructors annotated with <code>@Discoverable</code>
<li> classes with constructor parameters annotated with <code>@Discoverable</code>
<li> classes with fields annotated with <code>@Discoverable</code>
<li> classes with methods annotated with <code>@Discoverable</code>
<li> classes with method parameters annotated with <code>@Discoverable</code>
<li> subclasses of classes annotated with <code>@Discoverable</code>
<li> all these also counts for meta-annotations annotated with <code>@Discoverable</code>  

# Ignored annotations
<li> @Target
<li> @Retention
<li> @Stereotype
<li> @Documented

# About meta annotations and architectural thoughts
If you need to group annotations into a single composed annotation, you can use CDI stereotypes. 
These are meta-annotations, but only for CDI.
Other frameworks/libraries also support meta-annotations. They may need to be registered though.

An advantage of meta-annotations is the ability to get all those framework- and library-
(annotation-)dependencies out of you business/domain code. If a library changes, it is possible that
the annotations (spread all over your code) need to be changed too. If you want your business code
to be as independent of frameworks as possible, then meta-annotations can help to get just
a little bit more decoupling. The runtime behavior is unfortunately not decoupled that way.

Meta-annotations may also help if the annotations-providing library has no separate api/annotations module
and you want to restrict the visibility to all the other (not needed) library-features to get
a clearly defined view of what you actually use of that library. This is helpful, if you want to know,
if a change in that library is relevant for you or not.

Last but not least multiple annotations can be combined to one meta-annotation, making the code
cleaner and easier to read ("annotation-hell"). It is less error prone to write a new type annotated
by a single meta-annotation instead of a bunch of annotations, that no one really knows what they are for.

# License

```text
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
````