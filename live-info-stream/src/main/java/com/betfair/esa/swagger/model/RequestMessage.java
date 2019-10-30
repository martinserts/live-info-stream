/*
 * Betfair: Exchange Streaming API
 * API to receive streamed updates. This is an ssl socket connection of CRLF delimited json messages (see RequestMessage & ResponseMessage)
 *
 * OpenAPI spec version: 1.0.1423
 * Contact: bdp@betfair.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package com.betfair.esa.swagger.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonTypeId;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * RequestMessage
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2019-10-07T12:34:48.137476+03:00[Europe/Riga]")

public class RequestMessage {
  @SerializedName("op")
  private String op = null;

  @SerializedName("id")
  private Integer id = null;

  public RequestMessage() {
    this.op = this.getClass().getSimpleName();
  }
  public RequestMessage op(String op) {
    this.op = op;
    return this;
  }

   /**
   * The operation type
   * @return op
  **/
  @Schema(description = "The operation type")
  public String getOp() {
    return op;
  }

  public void setOp(String op) {
    this.op = op;
  }

  public RequestMessage id(Integer id) {
    this.id = id;
    return this;
  }

   /**
   * Client generated unique id to link request with response (like json rpc)
   * @return id
  **/
  @Schema(description = "Client generated unique id to link request with response (like json rpc)")
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RequestMessage requestMessage = (RequestMessage) o;
    return Objects.equals(this.op, requestMessage.op) &&
        Objects.equals(this.id, requestMessage.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(op, id);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RequestMessage {\n");
    
    sb.append("    op: ").append(toIndentedString(op)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
