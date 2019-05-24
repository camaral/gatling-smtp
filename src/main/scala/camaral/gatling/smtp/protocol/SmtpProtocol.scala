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
import io.gatling.core.CoreComponents
import io.gatling.core.config.GatlingConfiguration
import io.gatling.core.protocol.{Protocol, ProtocolKey}

object SmtpProtocol {

  val SmtpProtocolKey: ProtocolKey[SmtpProtocol, SmtpComponents] = new ProtocolKey[SmtpProtocol, SmtpComponents] {
    override def protocolClass: Class[io.gatling.core.protocol.Protocol] = classOf[SmtpProtocol].asInstanceOf[Class[io.gatling.core.protocol.Protocol]]

    override def defaultProtocolValue(configuration: GatlingConfiguration): SmtpProtocol = throw new IllegalStateException("Can't provide a default value for SmtpProtocol")

    override def newComponents(coreComponents: CoreComponents): SmtpProtocol => SmtpComponents = {
      smtpProtocol => SmtpComponents(smtpProtocol)

    }
  }
}

case class SmtpProtocol(
                         host: String,
                         port: Integer,
                         ssl: Boolean,
                         credentials: Option[Credentials]
                       ) extends Protocol {
  type Components = SmtpComponents
}