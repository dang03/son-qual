package otherAPI

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class UMLog extends Simulation {

	val httpProtocol = http
		.baseURL("http://sp.int3.sonata-nfv.eu:5600")
		.inferHtmlResources()
		.acceptHeader("*/*")
		.userAgentHeader("curl/7.35.0")

    val uri1 = "http://sp.int3.sonata-nfv.eu:5600/admin/log"

	val scn = scenario("UMLog")
		.exec(http("um_log")
			.get("/admin/log"))

	//setUp(scn.inject(nothingFor(5 seconds),atOnceUsers(1000))).protocols(httpProtocol)
	setUp(scn.inject(nothingFor(5 seconds),rampUsers(100) over (5 seconds))).protocols(httpProtocol)
}

