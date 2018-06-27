# Imglib2 bittype storage 

The unit test in this project demonstrates an issue with the default construction of `BitType` images. When creating an `ArrayImg<BitType>` using either  `ArrayImgFactory.create(..)` or `ArrayImgs.bits(dims...)` the resulting image will be backed by an `LongArray`, with the bits of the pixels tightly packed into these longs (64 per `long`), however the backing `LongArray` will have as many `long` elements as there are pixels in the image (most of them `0`).

When using the following code the issue is not present:   
```java
final int numLongs = (int) Math.ceil(1000 * 1000 / 64);
final ArrayImg<BitType, LongArray> img = ArrayImgs.bits(new LongArray(new long[numLongs]), 1000, 1000);
```
The reason is, that the called function sets the `entitiesPerPixel` to 1/64 explitly:
	
```java
final static public < A extends LongAccess > ArrayImg< BitType, A > bits( final A access, final long... dim )
{
  final ArrayImg< BitType, A > img = new ArrayImg<>( access, dim, new Fraction( 1, 64 ) );
	final BitType t = new BitType( img );
	img.setLinkedType( t );
	return img;
}
```
The likely cause is that `BitType` should indicate that it only uses 1/64th of a long for each pixel in it's `getEntitiesPerPixel()` method: https://github.com/imglib/imglib2/blob/master/src/main/java/net/imglib2/type/logic/BitType.java#L329
