package cartesianTree.nodes;

import caching.serializers.AttemptSerializeNullException;
import caching.serializers.ISerializer;
import caching.serializers.InvalidLengthOfArrayException;
import caching.serializers.InvalidOffsetOfArrayException;

import java.nio.ByteBuffer;

public class CartesianTreeNodeSerializer implements ISerializer<CartesianTreeNode>
{
	private final int size = 40;

	@Override
	public byte[] serialize(CartesianTreeNode node)
	{
		byte[] array = new byte[size];

		serialize(node, array, 0);

		return array;
	}

	@Override
	public CartesianTreeNode deserialize(byte[] array)
	{
		if (array.length != size)
			throw new InvalidLengthOfArrayException(size, array.length);

		return deserialize(array, 0);
	}

	@Override
	public int size()
	{
		return size;
	}

	@Override
	public CartesianTreeNode deserialize(byte[] array, int offset)
	{
		if (offset + size > array.length)
			throw new InvalidOffsetOfArrayException();

		ByteBuffer byteBuffer = ByteBuffer.wrap(array, offset, size);
		long number = byteBuffer.getLong();
		long left = byteBuffer.getLong();
		long right = byteBuffer.getLong();
		long value = byteBuffer.getLong();
		long count = byteBuffer.getLong();
		return new CartesianTreeNode(number, left, right, value, count);
	}

	@Override
	public void serialize(CartesianTreeNode node, byte[] array, int offset)
	{
		if (node == null)
			throw new AttemptSerializeNullException();

		if (offset + size > array.length)
			throw new InvalidOffsetOfArrayException();

		ByteBuffer byteBuffer = ByteBuffer.wrap(array, offset, size);
		byteBuffer.putLong(node.number);
		byteBuffer.putLong(node.left);
		byteBuffer.putLong(node.right);
		byteBuffer.putLong(node.value);
		byteBuffer.putLong(node.count);
	}
}
