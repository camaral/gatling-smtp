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

package camaral.gatling.smtp.action

import akka.actor.{Actor, Props}
import camaral.gatling.smtp.protocol.SmtpProtocol
import camaral.gatling.smtp.request.SendMailRequest
import io.gatling.core.action.{Action, ChainableAction}
import io.gatling.core.session.{Expression, Session}
import io.gatling.core.stats.StatsEngine

object SmtpAction {
  def props(requestName: String,
            from: String,
            to: String,
            subject: Expression[String],
            body: Expression[String],
            statsEngine: StatsEngine,
            next: Action,
            protocol: SmtpProtocol) =
    Props(new SmtpAction(requestName, from, to, subject, body, statsEngine, next, protocol))
}

class SmtpAction(requestName: String,
                 from: String,
                 to: String,
                 subject: Expression[String],
                 body: Expression[String],
                 val statsEngine: StatsEngine,
                 val next: Action,
                 protocol: SmtpProtocol) extends ChainableAction with Actor {

  val smtpHandler = context.actorOf(SmtpHandler.props())

  val name = "sendMail"

  def execute(session: Session) {
    smtpHandler ! generateSendMailRequest(session)
  }

  private def generateSendMailRequest(session: Session) = {
    def readSession(variableName: String) = session(variableName).as[String]

    for {
      resolvedBody <- body(session).map(String.valueOf)
      resolvedSubject <- subject(session).map(String.valueOf)
    } yield {

      SendMailRequest(
        session,
        protocol.host,
        protocol.port,
        protocol.ssl,
        from,
        to,
        resolvedBody,
        resolvedSubject,
        None) // TODO: come from Feeder

    }

    SendMailRequest(
      session,
      protocol.host,
      protocol.port,
      protocol.ssl,
      "",
      "",
      "",
      "",
      None) // TODO: come from Feeder
  }

  override def receive: Receive = {
    case session: Session => execute(session)
    case executionReport: ExecutionReport =>
      statsEngine.logResponse(executionReport.session, requestName, executionReport.responseTimings.startTimestamp, executionReport.responseTimings.endTimestamp,
        executionReport.status, None, executionReport.errorMessage)
      next ! executionReport.session
    case msg => logger.error(s"Unexpected message $msg")
  }
}
