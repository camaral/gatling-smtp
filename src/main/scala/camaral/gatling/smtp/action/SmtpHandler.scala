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

import javax.mail.internet.InternetAddress
import akka.actor.{ActorRef, Props}
import camaral.gatling.smtp.request.SendMailRequest
import io.gatling.commons.stats.{KO, OK}
import io.gatling.core.Predef.Status
import io.gatling.core.akka.BaseActor
import io.gatling.core.session.Session
import io.gatling.core.stats.message.ResponseTimings

import scala.util.{Failure, Success}

object SmtpHandler {
  def props(): Props = Props(new SmtpHandler)
}

class SmtpHandler extends BaseActor {
  override def receive: Receive = {
    case sendMailRequest: SendMailRequest => sendMail(sendMailRequest, sender)
    case msg => logger.error(s"received unexpected message $msg")
  }

  def waitCallback(sender: ActorRef): Receive = {
    case executionReport: ExecutionReport =>
      sender ! executionReport
      context.stop(self)
    case msg => logger.error(s"received unexpected message while expecting response $msg")
  }

  def sendMail(sendMailRequest: SendMailRequest, requestOrigin: ActorRef) = {
    import courier.{Mailer, Text, Envelope}
    val baseMailer = Mailer(sendMailRequest.host, sendMailRequest.port)
      .startTls(sendMailRequest.ssl)

    val mailer = sendMailRequest.credentials
      .map(value => baseMailer.auth(true)
        .as(value.login, value.password))
      .getOrElse(baseMailer.auth(false))()

    val requestStart = System.currentTimeMillis()

    val future = mailer(Envelope.from(new InternetAddress(sendMailRequest.from))
      .to(new InternetAddress(sendMailRequest.to))
      .subject(sendMailRequest.subject)
      .content(Text(sendMailRequest.body)))

    future.onComplete {
      case Success(_) => requestOrigin ! GoodExecutionReport(computeResponseTimings(requestStart), sendMailRequest.session)
      case Failure(e) =>
        logger.error("Exception caught while sending mail", e)
        requestOrigin ! BadExecutionReport(e.getMessage, computeResponseTimings(requestStart), sendMailRequest.session)
    }
  }

  private def computeResponseTimings(reqStart: Long) = {
    val requestEnd = System.currentTimeMillis()
    ResponseTimings(reqStart, requestEnd)
  }
}

case class Credentials(login: String, password: String)

trait ExecutionReport {
  def errorMessage: Option[String]

  def status: Status

  def responseTimings: ResponseTimings

  def session: Session
}

case class GoodExecutionReport(responseTimings: ResponseTimings, session: Session) extends ExecutionReport {
  override def errorMessage: Option[String] = None

  override def status: Status = OK
}

case class BadExecutionReport(message: String, responseTimings: ResponseTimings, override val session: Session) extends ExecutionReport {
  override def errorMessage: Option[String] = Some(message)

  override def status: Status = KO
}