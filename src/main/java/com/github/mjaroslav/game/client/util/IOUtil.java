package com.github.mjaroslav.game.client.util;

import lombok.experimental.UtilityClass;
import lombok.val;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;

import static org.lwjgl.BufferUtils.createByteBuffer;
import static org.lwjgl.system.MemoryUtil.memSlice;

@UtilityClass
public class IOUtil {
    private ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

    public ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
        ByteBuffer buffer;
        try (val source = IOUtil.class.getClassLoader().getResourceAsStream(resource)) {
            if (source == null)
                throw new IOException(String.format("Resource %s not found", resource));
            try (val rbc = Channels.newChannel(source)) {
                buffer = createByteBuffer(bufferSize);
                while (true) {
                    int bytes = rbc.read(buffer);
                    if (bytes == -1)
                        break;
                    if (buffer.remaining() == 0)
                        buffer = resizeBuffer(buffer, buffer.capacity() * 3 / 2); // 50%
                }
            }
        }
        buffer.flip();
        return memSlice(buffer);
    }
}
