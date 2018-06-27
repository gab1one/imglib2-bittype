
package org.knime.knip;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.basictypeaccess.array.ArrayDataAccess;
import net.imglib2.img.basictypeaccess.array.LongArray;
import net.imglib2.type.logic.BitType;

import org.junit.Test;

public class ArrayImgOversizeTest {

	@Test
	public void testOverSizedBitImageExplicitStorageArray() {

		final int numLongs = (int) Math.ceil(1000 * 1000 / 64);

		final ArrayImg<BitType, LongArray> img = ArrayImgs.bits(new LongArray(
			new long[numLongs]), 1000, 1000);
		final Random r = new Random(42l);
		img.forEach(p -> {
			p.set(r.nextBoolean());
		});

		final long[] storage = (long[]) ((ArrayDataAccess<?>) img.update(null))
			.getCurrentStorageArray();

		final long[] sizes = new long[img.numDimensions()];
		img.dimensions(sizes);

		assertEquals(storage.length, numLongs);

	}

	@Test
	public void testOverSizedBitImageImgFactory() {
		final ArrayImgFactory<BitType> factory = new ArrayImgFactory<>(
			new BitType());

		final int numLongs = (int) Math.ceil(1000 * 1000 / 64);

		ArrayImg<BitType, ?> img = factory.create(1000, 1000);

		final Random r = new Random(42l);
		img.forEach(p -> {
			p.set(r.nextBoolean());
		});

		final long[] storage = (long[]) ((ArrayDataAccess<?>) img.update(null))
			.getCurrentStorageArray();

		final long[] sizes = new long[img.numDimensions()];
		img.dimensions(sizes);

		assertEquals(numLongs, storage.length);

	}

	@Test
	public void testOverSizedBitImageArrayImgs() {

		final int numLongs = (int) Math.ceil(1000 * 1000 / 64);

		ArrayImg<BitType, LongArray> img = ArrayImgs.bits(1000, 1000);

		final Random r = new Random(42l);
		img.forEach(p -> {
			p.set(r.nextBoolean());
		});

		final long[] storage = (long[]) ((ArrayDataAccess<?>) img.update(null))
			.getCurrentStorageArray();

		final long[] sizes = new long[img.numDimensions()];
		img.dimensions(sizes);

		assertEquals(numLongs, storage.length);
	}
}
