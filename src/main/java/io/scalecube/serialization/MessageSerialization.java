package io.scalecube.serialization;

public interface MessageSerialization {

  public <T> T deserialize(byte[] data, Class<T> clazz) throws Exception;

  public <T> byte[] serialize(T value, Class<T> clazz) throws Exception;

  /**
   * empty message seriazliation returns the original byte array data.
   * 
   * @return original byte[] data.
   */
  public static MessageSerialization empty() {
    return new MessageSerialization() {

      @Override
      public <T> T deserialize(byte[] data, Class<T> clazz) throws Exception {
        return (T) data;
      }

      @Override
      public <T> byte[] serialize(T value, Class<T> clazz) throws Exception {
        if (value instanceof byte[]) {
          return (byte[]) value;
        } else {
          throw new UnsupportedOperationException("Empty serialization accept only byte[] type");
        }
      }
    };
  }
}
