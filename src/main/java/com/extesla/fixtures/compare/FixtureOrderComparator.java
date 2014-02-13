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
package com.extesla.fixtures.compare;

import java.util.Comparator;

import com.extesla.fixtures.annotations.Fixture;

/**
 *
 * @author Sean.Quinn
 *
 */
public class FixtureOrderComparator implements Comparator<Class<?>>{

	@Override
	public int compare(final Class<?> clazz1, final Class<?> clazz2) {
		try {
			return Integer.compare(getOrder(clazz1), getOrder(clazz2));
		}
		catch (Exception ex) {
			// Empty. Swallow the exception.
		}
		return 0;
	}

	private final int getOrder(final Class<?> clazz) throws ClassNotFoundException {
		final Fixture fixture = clazz.getAnnotation(Fixture.class);
		if (fixture == null) {
			throw new ClassNotFoundException();
		}
		return fixture.order();
	}

}
