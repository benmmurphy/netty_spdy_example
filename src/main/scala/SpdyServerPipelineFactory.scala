import org.jboss.netty.channel._
import org.jboss.netty.channel.Channels._
import org.jboss.netty.handler.codec.spdy._
import org.jboss.netty.handler.codec.http._
import org.jboss.netty.handler.ssl._
import javax.net.ssl._
import java.security._
import java.io._

class SpdyServerPipelineFactory extends ChannelPipelineFactory {

  def initContext(context : SSLContext) {
    val ks = KeyStore.getInstance("PKCS12")
    val fs = new FileInputStream("server.pkcs12")
    try {
      ks.load(fs, "test123".toCharArray())
    } finally {
      fs.close()
    }
    val kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
    kmf.init(ks, "test123".toCharArray())
    context.init(kmf.getKeyManagers(), Array[TrustManager](), new SecureRandom())
  }
    
  def createSSLEngine() : SSLEngine = {
    val context = SSLContext.getInstance("TLS", new sslnpn.net.ssl.internal.ssl.Provider())
    initContext(context)

    val engine = context.createSSLEngine()
    val npnEngine = engine.asInstanceOf[sslnpn.ssl.SSLEngineImpl]
    npnEngine.setAdvertisedNextProtocols("spdy/2", "http/1.1")
    engine.setUseClientMode(false)
    engine
  }

  def getPipeline() : ChannelPipeline = {
    val engine = createSSLEngine()
    val pipe = pipeline()
 
    val sslHandler = new SslHandler(engine)
    sslHandler.setIssueHandshake(true)
    pipe.addLast("ssl", sslHandler)
    pipe.addLast("decoder", new HttpOrSpdyDecoder())
    pipe.addLast("spdy_encoder", new SpdyFrameEncoder())
    pipe.addLast("http_encoder", new HttpResponseEncoder())
    pipe.addLast("spdy_session_handler", new SpdySessionHandler(true))
    pipe.addLast("handler", new Handler())
    pipe
  }
}

