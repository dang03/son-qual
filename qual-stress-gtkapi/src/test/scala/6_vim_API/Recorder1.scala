package vimAPI

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class GetVims1 extends Simulation {

import scala.util.parsing.json._

    val httpProtocol = http
		.baseURL("http://sp.int3.sonata-nfv.eu:32001")
		.inferHtmlResources()
		.acceptHeader("*/*")
		.userAgentHeader("curl/7.35.0")

    val uri1 = "http://sp.int3.sonata-nfv.eu:32001/api/v2/vims"

    val sessionHeaders = Map("Authorization" -> "Bearer ${accessToken}",
                                    "Content-Type" -> "application/json")

	val scn = scenario("GetVims1")
	    .exec(
            http("requesting_access_token")
            .post("http://sp.int3.sonata-nfv.eu:32001/api/v2/sessions")
            .headers(Map(
                "Accept" -> "application/json, */*; q=0.01",
                "Content-Type" -> "application/json"
                )
            )
            //.body(RawFileBody("test-credentials.txt"))
            .body(StringBody(session =>
                                s"""
                                   |{
                                   |    "username":"user01",
                                   |    "password":"1234"
                                   |}
                                """.stripMargin)).asJSON
            .check(status.is(200))
            .check(jsonPath("$..access_token").saveAs("accessToken"))
        )
		.exec(http("vims_1")
			.get("/api/v2/vims")
		    .headers(sessionHeaders)
		)

	setUp(scn.inject(nothingFor(5 seconds),atOnceUsers(1000))).protocols(httpProtocol)
}
