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

import java.util.Set;

/**
 * Fixture loader contract.
 *
 * @author Sean.Quinn
 * @since  1.0
 */
public interface Loader {

	/**
	 * Adds a fixture {@link Class} to the map of fixtures.
	 * @param clazz the {@code Class}.
	 */
	void addFixture(final Class<?> clazz);

	/**
	 *
	 * @param clazz
	 * @return
	 */
	Class<?> getFixture(final Class<?> clazz);

	/**
	 * Returns all of the fixtures as a {@link Set} of {@link Class Classes}.
	 * @return all of the fixtures as a {@code Set} of {@code Classes}.
	 */
	Set<Class<?>> getFixtures();

	/**
	 * Checks to see if the fixture, {@link Class}, is present in the loaded
	 * fixtures.
	 *
	 * @param clazz the {@code Class}.
	 * @return <tt>true</tt> if the fixture is in the collection of loaded
	 * 		fixtures; otherwise <tt>false</tt>.
	 */
	boolean hasFixture(final Class<?> clazz);

	void load();

}
