/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.jboss.netty.handler.codec.spdy;

import org.jboss.netty.channel.ChannelDownstreamHandler;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelUpstreamHandler;

/**
 * A combination of {@link SpdyFrameDecoder} and {@link SpdyFrameEncoder}.
 * @apiviz.has org.jboss.netty.handler.codec.spdy.SpdyFrameDecoder
 * @apiviz.has org.jboss.netty.handler.codec.spdy.SpdyFrameEncoder
 */
public class SpdyFrameCodec implements ChannelUpstreamHandler,
       ChannelDownstreamHandler {

    private final SpdyFrameDecoder decoder;
    private final SpdyFrameEncoder encoder;

    /**
     * Creates a new instance with the default decoder and encoder options
     * ({@code version (2)}, {@code maxChunkSize (8192)},
     * {@code maxHeaderSize (16384)}, {@code compressionLevel (6)},
     * {@code windowBits (15)}, and {@code memLevel (8)}).
     */
    @Deprecated
    public SpdyFrameCodec() {
        this(2);
    }

    /**
     * Creates a new instance with the specified {@code version} and
     * the default decoder and encoder options
     * ({@code maxChunkSize (8192)}, {@code maxHeaderSize (16384)},
     * {@code compressionLevel (6)}, {@code windowBits (15)},
     * and {@code memLevel (8)}).
     */
    public SpdyFrameCodec(int version) {
        this(version, 8192, 16384, 6, 15, 8);
    }

    /**
     * Creates a new instance with the specified decoder and encoder options.
     */
    public SpdyFrameCodec(
            int version, int maxChunkSize, int maxHeaderSize,
            int compressionLevel, int windowBits, int memLevel) {
        decoder = new SpdyFrameDecoder(version, maxChunkSize, maxHeaderSize);
        encoder = new SpdyFrameEncoder(version, compressionLevel, windowBits, memLevel);
    }

    public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
            throws Exception {
        decoder.handleUpstream(ctx, e);
    }

    public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e)
            throws Exception {
        encoder.handleDownstream(ctx, e);
    }
}
