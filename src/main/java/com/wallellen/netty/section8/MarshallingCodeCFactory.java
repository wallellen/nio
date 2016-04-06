package com.wallellen.netty.section8;

import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

/**
 * Created by <a href="mailto:wallellen@hotmail.com">WALLE</a> on 4/6/16.
 */
public class MarshallingCodeCFactory {
    public static ChannelHandler buildMarshallingDecoder() {
        final MarshallerFactory marshallerFactory = Marshalling.getMarshallerFactory(
                "serial", MarshallingCodeCFactory.class.getClassLoader());
        final MarshallingConfiguration marshallingConfiguration =
                new MarshallingConfiguration();
        marshallingConfiguration.setVersion(5);
        UnmarshallerProvider provider =
                new DefaultUnmarshallerProvider(marshallerFactory, marshallingConfiguration);
        return new MarshallingDecoder(provider, 1024);
    }

    public static ChannelHandler buildMarshallingEncoder() {
        final MarshallerFactory marshallerFactory = Marshalling.getMarshallerFactory(
                "serial", MarshallingCodeCFactory.class.getClassLoader());
        final MarshallingConfiguration marshallingConfiguration =
                new MarshallingConfiguration();
        marshallingConfiguration.setVersion(5);
        MarshallerProvider provider =
                new DefaultMarshallerProvider(marshallerFactory, marshallingConfiguration);
        return new MarshallingEncoder(provider);
    }
}
