For spdy to work with chrome it must run the ssl server on port 443. However,
this is a big pain to do because of permissions. The ssl server is set to run on port 4567. You
must forward 4567 to 443 for spdy to work or change the ssl listen port in SpdyServer.scala.

Mac OSX command: sudo ipfw add 1443 forward 127.0.0.1,4567 ip from any to any 443 in

sbt run

http://localhost:4568

It should say, "Served from HTTP"

Refresh

It should say, "Served from SPDY"

chrome://net-internals/#spdy


