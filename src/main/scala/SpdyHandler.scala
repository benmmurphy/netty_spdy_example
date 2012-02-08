import org.jboss.netty.channel._
import org.jboss.netty.handler._
import org.jboss.netty.handler.codec.spdy._
import org.jboss.netty.handler.codec.http._
import org.jboss.netty.handler.ssl._
import org.jboss.netty.buffer._
import org.jboss.netty.util._

import org.slf4j._

class Handler extends SimpleChannelUpstreamHandler {
  val logger:Logger = LoggerFactory.getLogger(classOf[Handler]);  

  override def messageReceived(ctx: ChannelHandlerContext, e: MessageEvent) = {
    e.getMessage() match {
      case m : SpdySynStreamFrame => spdyResponse(m, e)
      case m : HttpRequest => httpResponse(m, e)
    }


  }

  def spdyResponse(frame : SpdySynStreamFrame, e: MessageEvent) = {
    logger.info("Creating spdy response")
    val response = new DefaultSpdySynReplyFrame(frame.getStreamID())
    response.setLast(false)
    response.addHeader("status", "200 OK")
    response.addHeader("version", "HTTP/1.1")
    e.getChannel().write(response)

    val data = new DefaultSpdyDataFrame(frame.getStreamID())
    data.setLast(true)
    data.setData(ChannelBuffers.copiedBuffer("Served from SPDY", CharsetUtil.UTF_8))
    e.getChannel().write(data)
  }

  def httpResponse(req: HttpRequest, e : MessageEvent) = {
    val response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK)
    response.setContent(ChannelBuffers.copiedBuffer("Served from HTTP", CharsetUtil.UTF_8))
    response.setHeader("Content-Length", response.getContent().readableBytes())
    response.setHeader("Alternate-Protocol", "443:npn-spdy/2")
    e.getChannel().write(response)
  
  }
}
