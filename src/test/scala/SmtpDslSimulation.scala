/*
 * Copyright 2019 camaral(https://github.com/camaral)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import camaral.gatling.smtp.Predef._
import com.typesafe.scalalogging.Logger
import io.gatling.core.Predef._


class SmtpDslSimulation extends Simulation {
  val smtpProtocol = smtp
    .host("localhost")
    .port(1025)

  val scn = scenario("Simple scenario")
    .exec(smtp("My First Request")
        .from("edson.pele@example.com")
        .to("diego.maradona@example.com")
      .subject("Pele > Maradona")
      .body("Here goes the body"))

  setUp(scn.inject(atOnceUsers(1))).protocols(smtpProtocol)
}