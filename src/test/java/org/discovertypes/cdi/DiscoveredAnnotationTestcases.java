package org.discovertypes.cdi;

import static org.discovertypes.cdi.AnnotationLocation.TYPE;

import javax.inject.Named;

import org.junit.Ignore;

@Ignore
enum DiscoveredAnnotationTestcases {

	DEPRECATED {
		@Override
		public DiscoveredAnnotation build() {
			return new DiscoveredAnnotation(deprecatedAnnotation(), TYPE);
		}

	},
	NAMED {
		@Override
		public DiscoveredAnnotation build() {
			return new DiscoveredAnnotation(namedAnnotation(), TYPE);
		}
	},
	IGNORE {
		@Override
		public DiscoveredAnnotation build() {
			return new DiscoveredAnnotation(ignoreAnnotation(), TYPE);
		}
	},

	;
	public abstract DiscoveredAnnotation build();

	private static Deprecated deprecatedAnnotation() {
		return DiscoveredAnnotationTestcases.SuppressRawtypesAnnotatedClass.class.getAnnotation(Deprecated.class);
	}

	private static Named namedAnnotation() {
		return DiscoveredAnnotationTestcases.NamedAnnotatedClass.class.getAnnotation(Named.class);
	}

	private static Ignore ignoreAnnotation() {
		return DiscoveredAnnotationTestcases.class.getAnnotation(Ignore.class);
	}

	@Deprecated
	private static class SuppressRawtypesAnnotatedClass implements Comparable<Object> {

		@Override
		public int compareTo(Object o) {
			return 0;
		}
	}

	@Named("testname")
	private static class NamedAnnotatedClass {
	}
}