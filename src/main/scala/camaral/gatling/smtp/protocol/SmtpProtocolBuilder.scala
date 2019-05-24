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

package camaral.gatling.smtp.protocol

import io.gatling.commons.model.Credentials
import io.gatling.core.config.GatlingConfiguration

object SmtpProtocolBuilder {
  implicit def toHttpProtocol(builder: SmtpProtocolBuilder): SmtpProtocol = builder.build

  def apply(configuration: GatlingConfiguration): SmtpProtocolBuilder = SmtpProtocolBuilder()
}

case class SmtpProtocolBuilder(host: String = "localhost",
                               port: Integer = 25,
                               ssl: Boolean = false,
                               credentials: Option[Credentials] = None) {

  def host(host: String): SmtpProtocolBuilder = copy(host = host)

  def port(port: Integer): SmtpProtocolBuilder = copy(port = port)

  def useSsl(): SmtpProtocolBuilder = copy(ssl = true)

  def credentials(user: String, password: String): SmtpProtocolBuilder = copy(credentials = Some(Credentials(user, password)))

  def build = SmtpProtocol(
    host = this.host,
    port = this.port,
    ssl = this.ssl,
    credentials = this.credentials
  )
}
