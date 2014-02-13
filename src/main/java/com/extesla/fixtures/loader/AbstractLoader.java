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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.extesla.fixtures.compare.FixtureOrderComparator;

/**
 *
 * @author Sean.Quinn
 *
 */
abstract public class AbstractLoader implements Loader {

	protected final static char PACKAGE_SEPARATOR = '.';
	protected final static char DIR_SEPARATOR = '.';
	protected final static String CLASS_SUFFIX= ".class";

	/**
	 * The fixtures loaded by this abstract loader.
	 */
	private final Map<String, Class<?>> fixtures = new HashMap<String, Class<?>>();

	/**
	 * List of fixtures, ordered by their sort priorty.
	 */
	private List<Class<?>> orderedFixtures;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getFixture(final Class<?> clazz) {
		return fixtures.get(clazz.getName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Class<?>> getFixtures() {
		return new HashSet<Class<?>>(fixtures.values());
	}

	/**
	 * Returns the ordered list of fixtures.
	 * @return the ordered list of fixtures.
	 */
	public List<Class<?>> getOrderedFixtures() {
		return orderedFixtures;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasFixture(final Class<?> clazz) {
		return fixtures.containsKey(clazz.getName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addFixture(final Class<?> clazz) {
		fixtures.put(clazz.getName(), clazz);
	}

	/**
	 *
	 */
	protected void orderFixtures() {
		final List<Class<?>> orderedFixtures = new ArrayList<Class<?>>(getFixtures());
		Collections.sort(orderedFixtures, new FixtureOrderComparator());
		this.orderedFixtures = orderedFixtures;
	}
}
