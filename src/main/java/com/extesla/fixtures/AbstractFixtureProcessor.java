/* Copyright 2014 Extesla Digital Entertainment, Ltd. All rights reserved.
 *
 * Licensed under the MIT License (http://opensource.org/licenses/MIT)
 *
 * Permission is hereby granted, free of charge, to any
 * person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice
 * shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 * KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.extesla.fixtures;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.extesla.fixtures.annotations.Dependencies;
import com.extesla.fixtures.annotations.Fixture;
import com.extesla.fixtures.annotations.FixtureCache;
import com.extesla.fixtures.annotations.FixtureGenerate;
import com.extesla.fixtures.annotations.FixtureReference;
import com.extesla.fixtures.exceptions.RecursiveDependencyException;
import com.extesla.fixtures.loader.AnnotationLoader;
import com.extesla.fixtures.loader.Loader;

/**
 *
 * @author Sean.Quinn
 * @since  1.0
 */
abstract public class AbstractFixtureProcessor implements FixtureProcessor {

	/**
	 * Cache of fixtures; for looking up references.
	 */
	private final Map<Class<?>, Object> fixtureCache = new HashMap<Class<?>, Object>();

	private final Map<Class<?>, Object> processedFixtures = new HashMap<Class<?>, Object>();

	/**
	 * The {@link Fixture} loader.
	 */
	private Loader loader;

	/**
	 * The Java packages that will be scanned for {@link Fixture}s.
	 */
	private final String[] packages;

	/**
	 * The facade through which logging will be done.
	 */
	private final Logger logger = LoggerFactory.getLogger(AbstractFixtureProcessor.class);

	protected AbstractFixtureProcessor(final String[] packages) {
		this.packages = packages;
	}

	@Override
	public void run() {
		final AnnotationLoader loader = new AnnotationLoader(packages);

		loader.load();
		final List<Class<?>> fixtures = loader.getOrderedFixtures();

		// initialize the loader / get the data fixtures
		// process each of the data fixtures -> if a data fixture has dependencies process the dependecies first (recursively), do not support cyclic dependencies?

		for (Class<?> fixture : fixtures) {
			generate(fixture);
		}
	}

	/**
	 *
	 * @param clazz
	 */
	private void generate(final Class<?> clazz) {
		final Set<Class<?>> chain = new HashSet<Class<?>>();
		chain.add(clazz);

		generate(clazz, chain);
	}

	protected Logger getLogger() {
		return logger;
	}

	/**
	 *
	 * @param clazz
	 * @param chain
	 */
	@SuppressWarnings("unchecked")
	private void generate(final Class<?> clazz, final Set<Class<?>> chain) {
		if (processedFixtures.containsKey(clazz)) {
			getLogger().info("The data fixture: {} has already been processed.", clazz.getName());
			return;
		}

		final Dependencies dependencies = clazz.getAnnotation(Dependencies.class);
		if (dependencies != null) {
			for (final Class<?> depend : dependencies.value()) {
				if (chain.contains(depend)) {
					throw new RecursiveDependencyException();
				}
				chain.add(depend);
				generate(depend, chain);
			}
		}

		try {
			final Object obj = clazz.newInstance();
			getReferences(obj);

			// ** Execute the generate method...
			final Set<Method> methods = ReflectionUtils.getAllMethods(clazz,
					ReflectionUtils.withAnnotation(FixtureGenerate.class),
					ReflectionUtils.withParametersCount(0));
			for (final Method method : methods) {
				final boolean accessible = method.isAccessible();
				try {
					method.setAccessible(true);
					Object data = method.invoke(obj, (Object[]) null);
					if (data != null) {
						persist(data, true);
					}

				}
				catch (final InvocationTargetException ex) {
					throw new RuntimeException(ex);
				}
				finally {
					method.setAccessible(accessible);
				}
				cache(clazz, obj);
				markProcessed(clazz, null);
			}
		}
		catch (final Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Finds all of the data references marked by the {@link FixtureReference}
	 * annotation and instatiates the field references.
	 *
	 * If a fixture references data from another fixture that has not yet been
	 * run, the fixture is run immediately.
	 *
	 * If a reference cannot be found, an error will be thrown.
	 *
	 * @param clazz the {@code Class}.
	 */
	@SuppressWarnings("unchecked")
	protected void getReferences(final Object object) {
		final Class<?> clazz = object.getClass();
		final Set<Field> fields = ReflectionUtils.getAllFields(clazz,
				ReflectionUtils.withAnnotation(FixtureReference.class));
		for (final Field field : fields) {
			final FixtureReference metadata = field.getAnnotation(FixtureReference.class);
			final Class<?> type = metadata.type();
			final boolean accessible = field.isAccessible();
			try {
				field.setAccessible(true);
				if (!fixtureCache.containsKey(type)) {
					if (loader.hasFixture(type)) {
						generate(type);
					}
				}

				final Object fixture = fixtureCache.get(type);
				final Object data = lookup(metadata.value(), fixture);
				field.set(object, data);
			}
			catch (final Exception ex) {
				throw new RuntimeException(ex);
			}
			finally {
				field.setAccessible(accessible);
			}
		}
	}

	/**
	 *
	 * @param key
	 * @param object
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Object lookup(final String key, final Object object) {
		final Class<?> clazz = object.getClass();
		final Set<Field> fields = ReflectionUtils.getAllFields(clazz,
				ReflectionUtils.withAnnotation(FixtureCache.class),
				ReflectionUtils.withTypeAssignableTo(Map.class));
		for (final Field field : fields) {
			final boolean accessible = field.isAccessible();
			try {
				field.setAccessible(true);
				final Map<?, ?> cache = (Map<?, ?>) field.get(object);
				if (cache.containsKey(key)) {
					return cache.get(key);
				}
			}
			catch (final Exception ex) {
				throw new RuntimeException(ex);
			}
			finally {
				field.setAccessible(accessible);
			}
		}
		return null;
	}

	/**
	 *
	 * @param clazz
	 * @param data
	 */
	abstract protected void persist(final Object data, final boolean andFlush);

	private void markProcessed(final Class<?> clazz, final Object data) {
		processedFixtures.put(clazz, data);
	}

	private void cache(final Class<?> clazz, final Object fixture) {
		fixtureCache.put(clazz, fixture);
	}
}
