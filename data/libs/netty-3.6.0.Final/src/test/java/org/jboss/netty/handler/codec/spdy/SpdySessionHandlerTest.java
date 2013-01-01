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

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.embedder.DecoderEmbedder;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.jboss.netty.handler.codec.spdy.SpdyCodecUtil.*;
import static org.junit.Assert.*;

public class SpdySessionHandlerTest {

    private static final int closeSignal = SPDY_SETTINGS_MAX_ID;
    private static final SpdySettingsFrame closeMessage = new DefaultSpdySettingsFrame();

    static {
        closeMessage.setValue(closeSignal, 0);
    }

    private static void assertHeaderBlock(SpdyHeaderBlock received, SpdyHeaderBlock expected) {
        for (String name: expected.getHeaderNames()) {
            List<String> expectedValues = expected.getHeaders(name);
            List<String> receivedValues = received.getHeaders(name);
            assertTrue(receivedValues.containsAll(expectedValues));
            receivedValues.removeAll(expectedValues);
            assertTrue(receivedValues.isEmpty());
            received.removeHeader(name);
        }
        assertTrue(received.getHeaders().isEmpty());
    }

    private static void assertDataFrame(Object msg, int streamID, boolean last) {
        assertNotNull(msg);
        assertTrue(msg instanceof SpdyDataFrame);
        SpdyDataFrame spdyDataFrame = (SpdyDataFrame) msg;
        assertEquals(spdyDataFrame.getStreamId(), streamID);
        assertEquals(spdyDataFrame.isLast(), last);
    }

    private static void assertSynReply(Object msg, int streamID, boolean last, SpdyHeaderBlock headers) {
        assertNotNull(msg);
        assertTrue(msg instanceof SpdySynReplyFrame);
        SpdySynReplyFrame spdySynReplyFrame = (SpdySynReplyFrame) msg;
        assertEquals(spdySynReplyFrame.getStreamId(), streamID);
        assertEquals(spdySynReplyFrame.isLast(), last);
        assertHeaderBlock(spdySynReplyFrame, headers);
    }

    private static void assertRstStream(Object msg, int streamID, SpdyStreamStatus status) {
        assertNotNull(msg);
        assertTrue(msg instanceof SpdyRstStreamFrame);
        SpdyRstStreamFrame spdyRstStreamFrame = (SpdyRstStreamFrame) msg;
        assertEquals(spdyRstStreamFrame.getStreamId(), streamID);
        assertEquals(spdyRstStreamFrame.getStatus(), status);
    }

    private static void assertPing(Object msg, int id) {
        assertNotNull(msg);
        assertTrue(msg instanceof SpdyPingFrame);
        SpdyPingFrame spdyPingFrame = (SpdyPingFrame) msg;
        assertEquals(spdyPingFrame.getId(), id);
    }

    private static void assertGoAway(Object msg, int lastGoodStreamID) {
        assertNotNull(msg);
        assertTrue(msg instanceof SpdyGoAwayFrame);
        SpdyGoAwayFrame spdyGoAwayFrame = (SpdyGoAwayFrame) msg;
        assertEquals(spdyGoAwayFrame.getLastGoodStreamId(), lastGoodStreamID);
    }

    private static void assertHeaders(Object msg, int streamID, SpdyHeaderBlock headers) {
        assertNotNull(msg);
        assertTrue(msg instanceof SpdyHeadersFrame);
        SpdyHeadersFrame spdyHeadersFrame = (SpdyHeadersFrame) msg;
        assertEquals(spdyHeadersFrame.getStreamId(), streamID);
        assertHeaderBlock(spdyHeadersFrame, headers);
    }

    private static void testSpdySessionHandler(int version, boolean server) {
        DecoderEmbedder<Object> sessionHandler =
            new DecoderEmbedder<Object>(
                    new SpdySessionHandler(version, server), new EchoHandler(closeSignal, server));
        sessionHandler.pollAll();

        int localStreamID = server ? 1 : 2;
        int remoteStreamID = server ? 2 : 1;

        SpdyPingFrame localPingFrame = new DefaultSpdyPingFrame(localStreamID);
        SpdyPingFrame remotePingFrame = new DefaultSpdyPingFrame(remoteStreamID);

        SpdySynStreamFrame spdySynStreamFrame =
            new DefaultSpdySynStreamFrame(localStreamID, 0, (byte) 0);
        spdySynStreamFrame.setHeader("Compression", "test");

        SpdyDataFrame spdyDataFrame = new DefaultSpdyDataFrame(localStreamID);
        spdyDataFrame.setLast(true);

        // Check if session handler returns INVALID_STREAM if it receives
        // a data frame for a Stream-ID that is not open
        sessionHandler.offer(new DefaultSpdyDataFrame(localStreamID));
        assertRstStream(sessionHandler.poll(), localStreamID, SpdyStreamStatus.INVALID_STREAM);
        assertNull(sessionHandler.peek());

        // Check if session handler returns PROTOCOL_ERROR if it receives
        // a data frame for a Stream-ID before receiving a SYN_REPLY frame
        sessionHandler.offer(new DefaultSpdyDataFrame(remoteStreamID));
        assertRstStream(sessionHandler.poll(), remoteStreamID, SpdyStreamStatus.PROTOCOL_ERROR);
        assertNull(sessionHandler.peek());
        remoteStreamID += 2;

        // Check if session handler returns PROTOCOL_ERROR if it receives
        // multiple SYN_REPLY frames for the same active Stream-ID
        sessionHandler.offer(new DefaultSpdySynReplyFrame(remoteStreamID));
        assertNull(sessionHandler.peek());
        sessionHandler.offer(new DefaultSpdySynReplyFrame(remoteStreamID));
        assertRstStream(sessionHandler.poll(), remoteStreamID, SpdyStreamStatus.STREAM_IN_USE);
        assertNull(sessionHandler.peek());
        remoteStreamID += 2;

        // Check if frame codec correctly compresses/uncompresses headers
        sessionHandler.offer(spdySynStreamFrame);
        assertSynReply(sessionHandler.poll(), localStreamID, false, spdySynStreamFrame);
        assertNull(sessionHandler.peek());
        SpdyHeadersFrame spdyHeadersFrame = new DefaultSpdyHeadersFrame(localStreamID);
        spdyHeadersFrame.addHeader("HEADER","test1");
        spdyHeadersFrame.addHeader("HEADER","test2");
        sessionHandler.offer(spdyHeadersFrame);
        assertHeaders(sessionHandler.poll(), localStreamID, spdyHeadersFrame);
        assertNull(sessionHandler.peek());
        localStreamID += 2;

        // Check if session handler closed the streams using the number
        // of concurrent streams and that it returns REFUSED_STREAM
        // if it receives a SYN_STREAM frame it does not wish to accept
        spdySynStreamFrame.setStreamId(localStreamID);
        spdySynStreamFrame.setLast(true);
        spdySynStreamFrame.setUnidirectional(true);
        sessionHandler.offer(spdySynStreamFrame);
        assertRstStream(sessionHandler.poll(), localStreamID, SpdyStreamStatus.REFUSED_STREAM);
        assertNull(sessionHandler.peek());

        // Check if session handler drops active streams if it receives
        // a RST_STREAM frame for that Stream-ID
        sessionHandler.offer(new DefaultSpdyRstStreamFrame(remoteStreamID, 3));
        assertNull(sessionHandler.peek());
        remoteStreamID += 2;

        // Check if session handler honors UNIDIRECTIONAL streams
        spdySynStreamFrame.setLast(false);
        sessionHandler.offer(spdySynStreamFrame);
        assertNull(sessionHandler.peek());
        spdySynStreamFrame.setUnidirectional(false);

        // Check if session handler returns PROTOCOL_ERROR if it receives
        // multiple SYN_STREAM frames for the same active Stream-ID
        sessionHandler.offer(spdySynStreamFrame);
        assertRstStream(sessionHandler.poll(), localStreamID, SpdyStreamStatus.PROTOCOL_ERROR);
        assertNull(sessionHandler.peek());
        localStreamID += 2;

        // Check if session handler returns PROTOCOL_ERROR if it receives
        // a SYN_STREAM frame with an invalid Stream-ID
        spdySynStreamFrame.setStreamId(localStreamID - 1);
        sessionHandler.offer(spdySynStreamFrame);
        assertRstStream(sessionHandler.poll(), localStreamID - 1, SpdyStreamStatus.PROTOCOL_ERROR);
        assertNull(sessionHandler.peek());
        spdySynStreamFrame.setStreamId(localStreamID);

        // Check if session handler correctly limits the number of
        // concurrent streams in the SETTINGS frame
        SpdySettingsFrame spdySettingsFrame = new DefaultSpdySettingsFrame();
        spdySettingsFrame.setValue(SpdySettingsFrame.SETTINGS_MAX_CONCURRENT_STREAMS, 2);
        sessionHandler.offer(spdySettingsFrame);
        assertNull(sessionHandler.peek());
        sessionHandler.offer(spdySynStreamFrame);
        assertRstStream(sessionHandler.poll(), localStreamID, SpdyStreamStatus.REFUSED_STREAM);
        assertNull(sessionHandler.peek());
        spdySettingsFrame.setValue(SpdySettingsFrame.SETTINGS_MAX_CONCURRENT_STREAMS, 4);
        sessionHandler.offer(spdySettingsFrame);
        assertNull(sessionHandler.peek());
        sessionHandler.offer(spdySynStreamFrame);
        assertSynReply(sessionHandler.poll(), localStreamID, false, spdySynStreamFrame);
        assertNull(sessionHandler.peek());

        // Check if session handler rejects HEADERS for closed streams
        int testStreamID = spdyDataFrame.getStreamId();
        sessionHandler.offer(spdyDataFrame);
        assertDataFrame(sessionHandler.poll(), testStreamID, spdyDataFrame.isLast());
        assertNull(sessionHandler.peek());
        spdyHeadersFrame.setStreamId(testStreamID);
        sessionHandler.offer(spdyHeadersFrame);
        assertRstStream(sessionHandler.poll(), testStreamID, SpdyStreamStatus.INVALID_STREAM);
        assertNull(sessionHandler.peek());

        // Check if session handler returns PROTOCOL_ERROR if it receives
        // an invalid HEADERS frame
        spdyHeadersFrame.setStreamId(localStreamID);
        spdyHeadersFrame.setInvalid();
        sessionHandler.offer(spdyHeadersFrame);
        assertRstStream(sessionHandler.poll(), localStreamID, SpdyStreamStatus.PROTOCOL_ERROR);
        assertNull(sessionHandler.peek());

        // Check if session handler returns identical local PINGs
        sessionHandler.offer(localPingFrame);
        assertPing(sessionHandler.poll(), localPingFrame.getId());
        assertNull(sessionHandler.peek());

        // Check if session handler ignores un-initiated remote PINGs
        sessionHandler.offer(remotePingFrame);
        assertNull(sessionHandler.peek());

        // Check if session handler sends a GOAWAY frame when closing
        sessionHandler.offer(closeMessage);
        assertGoAway(sessionHandler.poll(), localStreamID);
        assertNull(sessionHandler.peek());
        localStreamID += 2;

        // Check if session handler returns REFUSED_STREAM if it receives
        // SYN_STREAM frames after sending a GOAWAY frame
        spdySynStreamFrame.setStreamId(localStreamID);
        sessionHandler.offer(spdySynStreamFrame);
        assertRstStream(sessionHandler.poll(), localStreamID, SpdyStreamStatus.REFUSED_STREAM);
        assertNull(sessionHandler.peek());

        // Check if session handler ignores Data frames after sending
        // a GOAWAY frame
        spdyDataFrame.setStreamId(localStreamID);
        sessionHandler.offer(spdyDataFrame);
        assertNull(sessionHandler.peek());

        sessionHandler.finish();
    }

    @Test
    public void testSpdyClientSessionHandler() {
        for (int version = SPDY_MIN_VERSION; version <= SPDY_MAX_VERSION; version ++) {
            testSpdySessionHandler(version, false);
        }
    }

    @Test
    public void testSpdyServerSessionHandler() {
        for (int version = SPDY_MIN_VERSION; version <= SPDY_MAX_VERSION; version ++) {
            testSpdySessionHandler(version, true);
        }
    }

    // Echo Handler opens 4 half-closed streams on session connection
    // and then sets the number of concurrent streams to 3
    private static class EchoHandler extends SimpleChannelUpstreamHandler {
        private final int closeSignal;
        private final boolean server;

        EchoHandler(int closeSignal, boolean server) {
            this.closeSignal = closeSignal;
            this.server = server;
        }

        @Override
        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
                throws Exception {

            // Initiate 4 new streams
            int streamID = server ? 2 : 1;
            SpdySynStreamFrame spdySynStreamFrame =
                new DefaultSpdySynStreamFrame(streamID, 0, (byte) 0);
            spdySynStreamFrame.setLast(true);
            Channels.write(e.getChannel(), spdySynStreamFrame);
            spdySynStreamFrame.setStreamId(spdySynStreamFrame.getStreamId() + 2);
            Channels.write(e.getChannel(), spdySynStreamFrame);
            spdySynStreamFrame.setStreamId(spdySynStreamFrame.getStreamId() + 2);
            Channels.write(e.getChannel(), spdySynStreamFrame);
            spdySynStreamFrame.setStreamId(spdySynStreamFrame.getStreamId() + 2);
            Channels.write(e.getChannel(), spdySynStreamFrame);

            // Limit the number of concurrent streams to 3
            SpdySettingsFrame spdySettingsFrame = new DefaultSpdySettingsFrame();
            spdySettingsFrame.setValue(SpdySettingsFrame.SETTINGS_MAX_CONCURRENT_STREAMS, 3);
            Channels.write(e.getChannel(), spdySettingsFrame);
        }

        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
                throws Exception {
            Object msg = e.getMessage();
            if (msg instanceof SpdyDataFrame ||
                msg instanceof SpdyPingFrame ||
                msg instanceof SpdyHeadersFrame) {

                Channels.write(e.getChannel(), msg, e.getRemoteAddress());
                return;
            }

            if (msg instanceof SpdySynStreamFrame) {

                SpdySynStreamFrame spdySynStreamFrame = (SpdySynStreamFrame) msg;

                int streamID = spdySynStreamFrame.getStreamId();
                SpdySynReplyFrame spdySynReplyFrame = new DefaultSpdySynReplyFrame(streamID);
                spdySynReplyFrame.setLast(spdySynStreamFrame.isLast());
                for (Map.Entry<String, String> entry: spdySynStreamFrame.getHeaders()) {
                    spdySynReplyFrame.addHeader(entry.getKey(), entry.getValue());
                }

                Channels.write(e.getChannel(), spdySynReplyFrame, e.getRemoteAddress());
                return;
            }

            if (msg instanceof SpdySettingsFrame) {

                SpdySettingsFrame spdySettingsFrame = (SpdySettingsFrame) msg;
                if (spdySettingsFrame.isSet(closeSignal)) {
                    Channels.close(e.getChannel());
                }
            }
        }
    }
}
