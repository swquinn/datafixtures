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
package com.extesla.fixtures.loader;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import com.extesla.fixtures.annotations.Fixture;

/**
 * A {@code DataFixture} is a class of objects which represents data to be
 * loaded into a persistent storage container, i.e. a database. Data fixtures
 * are representative of domain-objects and are one option for populating a
 * database with test, or base-line, data.
 *
 * @author Sean.Quinn
 * @since  1.0
 */
public class AnnotationLoader extends AbstractLoader {

	private final String[] packages;

	public AnnotationLoader(final String pkg) {
		this(new String[]{ pkg });
	}

	public AnnotationLoader(final String[] packages) {
		this.packages = packages;
	}

	@Override
	public void load() {
		final Set<Class<?>> classes = findFixtures(packages);
		for (final Class<?> clazz : classes) {
			addFixture(clazz);
		}
		orderFixtures();
	}

	public Set<Class<?>> findFixtures(final String[] packages) {
		final Set<URL> urls = new HashSet<URL>();
		for (final String pkg : packages) {
			urls.addAll(ClasspathHelper.forPackage(pkg));
		}

		final Reflections reflections = new Reflections(new ConfigurationBuilder()
				.setUrls(urls)
				.setScanners(new TypeAnnotationsScanner()));
		final Set<Class<?>> fixtures = reflections.getTypesAnnotatedWith(Fixture.class);
		return fixtures;
	}
}
