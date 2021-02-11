package com.org.fplab.liveinfostream.betfair.betting.models

import io.circe._

/** All betting API requests are wrapped into JsonRPC */
final case class RpcRequest[A <: HasEncoded](
  method: String,
  params: A,
  id: Int = 1,
  jsonrpc: String = "2.0"
) {
  def getJson: Json = {
    val encoder = Encoder.forProduct4[RpcRequest[A], String, Json, Int, String]("method", "params", "id", "jsonrpc") {
      case RpcRequest(method, params, id, jsonrpc) => (method, params.encoded, id, jsonrpc)
    }
    encoder.apply(this)
  }
}

object RpcRequest {
  def apply[A <: HasEncoded](operation: String, request: A): RpcRequest[A] =
    RpcRequest(method = s"SportsAPING/v1.0/$operation", request)
}
