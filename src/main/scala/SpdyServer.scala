import org.jboss.netty.bootstrap._
import org.jboss.netty.channel.socket.nio._
import java.net._
import java.util.concurrent._


object SpdyServer {
   def main(args: Array[String]) {
    val bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
      Executors.newCachedThreadPool(),
      Executors.newCachedThreadPool()))

    bootstrap.setPipelineFactory(new SpdyServerPipelineFactory())
    bootstrap.bind(new InetSocketAddress(4567))
  }
}
