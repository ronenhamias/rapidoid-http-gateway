package io.scalecube.serialization;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import io.protostuff.JsonIOUtil;
import io.protostuff.Schema;


public class JsonMessageSerialization implements MessageSerialization {

  private static final RecyclableLinkedBuffer recyclableLinkedBuffer = new RecyclableLinkedBuffer();

  @Override
  public <T> T deserialize(byte[] data, Class<T> clazz) throws Exception {

    Schema<T> schema = SchemaCache.getOrCreate(clazz);
    T message = schema.newMessage();
    ByteBuf bb = Unpooled.copiedBuffer(data, 0, data.length);

    try {
      String dataStr = new String(data);
      JsonIOUtil.mergeFrom(data, message, schema, false);
    } catch (Exception e) {
      throw new DecoderException(e.getMessage(), e);
    }

    return message;
  }

  @Override
  public <T> byte[] serialize(T value, Class<T> clazz) throws Exception {

    Schema<T> schema = SchemaCache.getOrCreate(clazz);

    try (RecyclableLinkedBuffer rlb = recyclableLinkedBuffer.get()) {
      try {
        return JsonIOUtil.toByteArray(value, schema, false);
      } catch (Exception e) {
        throw new EncoderException(e.getMessage(), e);
      }
    }
  }


  public <T> byte[] serialize(Object value) throws Exception {
    return null;
  }

}

