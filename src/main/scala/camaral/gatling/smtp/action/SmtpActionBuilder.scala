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

import camaral.gatling.smtp.protocol.{SmtpComponents, SmtpProtocol}
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.action.{Action, ExitableActorDelegatingAction}
import io.gatling.core.session.Expression
import io.gatling.core.structure.ScenarioContext
import io.gatling.core.util.NameGen

case class SmtpActionBuilder(requestName: String,
                             _from: String,
                             _to: String,
                             _subject: Expression[String],
                             _body: Expression[String]) extends ActionBuilder with NameGen {

  def from(from: String): SmtpActionBuilder = copy(_from, from)

  def to(to: String): SmtpActionBuilder = copy(_to, to)

  def subject(subject: Expression[String]) = copy(_subject = subject)

  def body(body: Expression[String]) = copy(_body = body)

  override def build(ctx: ScenarioContext, next: Action): Action = {
    val components: SmtpComponents = ctx.protocolComponentsRegistry.components(SmtpProtocol.SmtpProtocolKey)

    val smtpProps = SmtpAction.props(requestName, _from, _to, _subject, _body,
      ctx.coreComponents.statsEngine, next, components.smtpProtocol)
    val actionActor = ctx.coreComponents.actorSystem.actorOf(smtpProps)
    new ExitableActorDelegatingAction(genName(requestName), ctx.coreComponents.statsEngine, ctx.coreComponents.clock, next, actionActor)
  }
}