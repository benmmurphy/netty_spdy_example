import org.jboss.netty.channel._
import org.jboss.netty.handler._
import org.jboss.netty.handler.codec.spdy._
import org.jboss.netty.handler.codec.http._
import org.jboss.netty.handler.ssl._

import org.slf4j._

class HttpOrSpdyDecoder extends ChannelUpstreamHandler {
  val logger:Logger = LoggerFactory.getLogger(classOf[HttpOrSpdyDecoder]);  

  private val spdyFrameDecoder : SpdyFrameDecoder = new SpdyFrameDecoder()
  private val httpRequestDecoder : HttpRequestDecoder = new HttpRequestDecoder()

  override def handleUpstream(ctx: ChannelHandlerContext, e: ChannelEvent) {
    val engine = ctx.getPipeline().get(classOf[SslHandler]).getEngine().asInstanceOf[sslnpn.ssl.SSLEngineImpl]

    val protocol = engine.getNegotiatedNextProtocol()
    logger.info("decoding event: {} based on protocol: {} ", e, protocol)
    if ("spdy/2".equals(protocol)) {
      spdyFrameDecoder.handleUpstream(ctx, e)
    } else {
      httpRequestDecoder.handleUpstream(ctx, e)
    }

  }

}
