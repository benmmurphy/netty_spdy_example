import org.jboss.netty.channel._
import org.jboss.netty.handler._
import org.jboss.netty.handler.codec.spdy._
import org.slf4j._

class SpdyHandler  extends SimpleChannelUpstreamHandler {
  val logger:Logger = LoggerFactory.getLogger(classOf[SpdyHandler]);  

  override def  messageReceived(ctx: ChannelHandlerContext, e: MessageEvent) = {
    val frame = e.getMessage().asInstanceOf[SpdySynStreamFrame]
    logger.debug("received syn stream", frame)
    /* we fall off here when we block */

  }

}
