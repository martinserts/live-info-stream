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

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.IOException;
/**
 * AllResponseTypesExample
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2019-10-07T12:34:48.137476+03:00[Europe/Riga]")
public class AllResponseTypesExample {
  /**
   * Gets or Sets opTypes
   */
  @JsonAdapter(OpTypesEnum.Adapter.class)
  public enum OpTypesEnum {
    CONNECTION("connection"),
    STATUS("status"),
    MCM("mcm"),
    OCM("ocm");

    private String value;

    OpTypesEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static OpTypesEnum fromValue(String text) {
      for (OpTypesEnum b : OpTypesEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<OpTypesEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final OpTypesEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public OpTypesEnum read(final JsonReader jsonReader) throws IOException {
        String value = jsonReader.nextString();
        return OpTypesEnum.fromValue(String.valueOf(value));
      }
    }
  }  @SerializedName("opTypes")
  private OpTypesEnum opTypes = null;

  @SerializedName("marketChangeMessage")
  private MarketChangeMessage marketChangeMessage = null;

  @SerializedName("connection")
  private ConnectionMessage connection = null;

  @SerializedName("orderChangeMessage")
  private OrderChangeMessage orderChangeMessage = null;

  @SerializedName("status")
  private StatusMessage status = null;

  public AllResponseTypesExample opTypes(OpTypesEnum opTypes) {
    this.opTypes = opTypes;
    return this;
  }

   /**
   * Get opTypes
   * @return opTypes
  **/
  @Schema(description = "")
  public OpTypesEnum getOpTypes() {
    return opTypes;
  }

  public void setOpTypes(OpTypesEnum opTypes) {
    this.opTypes = opTypes;
  }

  public AllResponseTypesExample marketChangeMessage(MarketChangeMessage marketChangeMessage) {
    this.marketChangeMessage = marketChangeMessage;
    return this;
  }

   /**
   * Get marketChangeMessage
   * @return marketChangeMessage
  **/
  @Schema(description = "")
  public MarketChangeMessage getMarketChangeMessage() {
    return marketChangeMessage;
  }

  public void setMarketChangeMessage(MarketChangeMessage marketChangeMessage) {
    this.marketChangeMessage = marketChangeMessage;
  }

  public AllResponseTypesExample connection(ConnectionMessage connection) {
    this.connection = connection;
    return this;
  }

   /**
   * Get connection
   * @return connection
  **/
  @Schema(description = "")
  public ConnectionMessage getConnection() {
    return connection;
  }

  public void setConnection(ConnectionMessage connection) {
    this.connection = connection;
  }

  public AllResponseTypesExample orderChangeMessage(OrderChangeMessage orderChangeMessage) {
    this.orderChangeMessage = orderChangeMessage;
    return this;
  }

   /**
   * Get orderChangeMessage
   * @return orderChangeMessage
  **/
  @Schema(description = "")
  public OrderChangeMessage getOrderChangeMessage() {
    return orderChangeMessage;
  }

  public void setOrderChangeMessage(OrderChangeMessage orderChangeMessage) {
    this.orderChangeMessage = orderChangeMessage;
  }

  public AllResponseTypesExample status(StatusMessage status) {
    this.status = status;
    return this;
  }

   /**
   * Get status
   * @return status
  **/
  @Schema(description = "")
  public StatusMessage getStatus() {
    return status;
  }

  public void setStatus(StatusMessage status) {
    this.status = status;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AllResponseTypesExample allResponseTypesExample = (AllResponseTypesExample) o;
    return Objects.equals(this.opTypes, allResponseTypesExample.opTypes) &&
        Objects.equals(this.marketChangeMessage, allResponseTypesExample.marketChangeMessage) &&
        Objects.equals(this.connection, allResponseTypesExample.connection) &&
        Objects.equals(this.orderChangeMessage, allResponseTypesExample.orderChangeMessage) &&
        Objects.equals(this.status, allResponseTypesExample.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(opTypes, marketChangeMessage, connection, orderChangeMessage, status);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AllResponseTypesExample {\n");
    
    sb.append("    opTypes: ").append(toIndentedString(opTypes)).append("\n");
    sb.append("    marketChangeMessage: ").append(toIndentedString(marketChangeMessage)).append("\n");
    sb.append("    connection: ").append(toIndentedString(connection)).append("\n");
    sb.append("    orderChangeMessage: ").append(toIndentedString(orderChangeMessage)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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