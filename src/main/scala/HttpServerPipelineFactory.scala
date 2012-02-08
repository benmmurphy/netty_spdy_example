import org.jboss.netty.channel._
import org.jboss.netty.channel.Channels._
import org.jboss.netty.handler.codec.spdy._
import org.jboss.netty.handler.codec.http._
import org.jboss.netty.handler.ssl._
import javax.net.ssl._
import java.security._
import java.io._

class HttpServerPipelineFactory extends ChannelPipelineFactory {

  def getPipeline() : ChannelPipeline = {
    val pipe = pipeline()
 
    pipe.addLast("decoder", new HttpRequestDecoder())
    pipe.addLast("http_encoder", new HttpResponseEncoder())
    pipe.addLast("handler", new Handler())
    pipe
  }
}

