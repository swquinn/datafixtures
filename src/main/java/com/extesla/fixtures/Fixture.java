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

/**
 * A {@code DataFixture} is a class of objects which represents data to be
 * loaded into a persistent storage container, i.e. a database. Data fixtures
 * are representative of domain-objects and are one option for populating a
 * database with test, or base-line, data.
 *
 * @author Sean.Quinn
 * @since  1.0
 */
public interface Fixture<T> {

	/**
	 * Generates the object, {@code T}, described by this {@link Fixture}.
	 *
	 * @return the object generated by this data fixture.
	 */
	T generate();

	/**
	 * Returns the object, {@code T}, that is being generated by this
	 * {@link Fixture}.
	 *
	 * @return the object generated by this data fixture.
	 */
	T getObject();

	/**
	 * Check if an object is stored by reference; look up the reference by name.
	 *
	 * @param name the name of the reference.
	 * @return <tt>true</tt> if the fixture has a reference to an object by the
	 * 		<tt>name</tt> passed; otherwise <tt>false</tt>.
	 */
	boolean hasReference(String name);

	/**
	 * Returns an object stored by reference; looking up the reference by name.
	 *
	 * @param name the name of the reference.
	 * @return the object stored by reference.
	 */
	Object getReference(String name);

	/**
	 * Adds a reference to this {@code DataFixture}.
	 *
	 * @param name the name of the reference being added.
	 * @param object the {@code Object}.
	 */
	void addReference(String name, Object object);
}
