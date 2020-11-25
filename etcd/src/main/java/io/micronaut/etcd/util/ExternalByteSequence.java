package io.micronaut.etcd.util;

import com.google.protobuf.ByteString;
import java.nio.charset.Charset;
import javax.inject.Singleton;

/**
 * Utility class to expose the the io.etcd.jetcd.ByteSequence static methods.
 */
@Singleton
public class ExternalByteSequence {

  public static final ExternalByteSequence EMPTY = new ExternalByteSequence(ByteString.EMPTY);

  private io.etcd.jetcd.ByteSequence byteSequence;
  private ByteString byteString;

  public ExternalByteSequence(ByteString byteString) {
    this.byteString = byteString;
    byteSequence = ExternalByteSequence.from(byteString);
  }

  public boolean startsWith(ExternalByteSequence prefix) {
    return byteSequence.startsWith(ExternalByteSequence.from(prefix.getBytes()));
  }

  public byte[] getBytes() {
    return byteSequence.getBytes();
  }

  /**
   *
   * @param source
   * @param charset
   * @return
   */
  public static io.etcd.jetcd.ByteSequence from(String source, Charset charset) {
    return io.etcd.jetcd.ByteSequence.from(source, charset);
  }

  public static io.etcd.jetcd.ByteSequence from(ByteString source) {
    return io.etcd.jetcd.ByteSequence.from(source);
  }

  public static io.etcd.jetcd.ByteSequence from(byte[] source) {
    return io.etcd.jetcd.ByteSequence.from(source);
  }

}
