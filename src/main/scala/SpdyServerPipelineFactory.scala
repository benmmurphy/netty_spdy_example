import org.jboss.netty.channel._
import org.jboss.netty.channel.Channels._
import org.jboss.netty.handler.codec.spdy._
import org.jboss.netty.handler.ssl._
import javax.net.ssl.SSLEngine

class SpdyServerPipelineFactory extends ChannelPipelineFactory {

  def createSSLEngine() : SSLEngine = {
    null
  }

  def getPipeline() : ChannelPipeline = {
    val engine = createSSLEngine()
    val pipe = pipeline()
  
    pipe.addLast("ssl", new SslHandler(engine))
    pipe.addLast("decoder", new SpdyFrameDecoder())
    pipe.addLast("encoder", new SpdyFrameEncoder())
    pipe.addLast("handler", new SpdySessionHandler(true))
    pipe.addLast("handler", new SpdyHandler())
    pipe
  }
}

