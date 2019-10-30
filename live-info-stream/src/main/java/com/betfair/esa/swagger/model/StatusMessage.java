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
 * StatusMessage
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2019-10-07T12:34:48.137476+03:00[Europe/Riga]")
public class StatusMessage extends ResponseMessage {
  @SerializedName("errorMessage")
  private String errorMessage = null;

  /**
   * The type of error in case of a failure
   */
  @JsonAdapter(ErrorCodeEnum.Adapter.class)
  public enum ErrorCodeEnum {
    NO_APP_KEY("NO_APP_KEY"),
    INVALID_APP_KEY("INVALID_APP_KEY"),
    NO_SESSION("NO_SESSION"),
    INVALID_SESSION_INFORMATION("INVALID_SESSION_INFORMATION"),
    NOT_AUTHORIZED("NOT_AUTHORIZED"),
    INVALID_INPUT("INVALID_INPUT"),
    INVALID_CLOCK("INVALID_CLOCK"),
    UNEXPECTED_ERROR("UNEXPECTED_ERROR"),
    TIMEOUT("TIMEOUT"),
    SUBSCRIPTION_LIMIT_EXCEEDED("SUBSCRIPTION_LIMIT_EXCEEDED"),
    INVALID_REQUEST("INVALID_REQUEST"),
    CONNECTION_FAILED("CONNECTION_FAILED"),
    MAX_CONNECTION_LIMIT_EXCEEDED("MAX_CONNECTION_LIMIT_EXCEEDED");

    private String value;

    ErrorCodeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static ErrorCodeEnum fromValue(String text) {
      for (ErrorCodeEnum b : ErrorCodeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<ErrorCodeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ErrorCodeEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public ErrorCodeEnum read(final JsonReader jsonReader) throws IOException {
        String value = jsonReader.nextString();
        return ErrorCodeEnum.fromValue(String.valueOf(value));
      }
    }
  }  @SerializedName("errorCode")
  private ErrorCodeEnum errorCode = null;

  @SerializedName("connectionId")
  private String connectionId = null;

  @SerializedName("connectionClosed")
  private Boolean connectionClosed = null;

  /**
   * The status of the last request
   */
  @JsonAdapter(StatusCodeEnum.Adapter.class)
  public enum StatusCodeEnum {
    SUCCESS("SUCCESS"),
    FAILURE("FAILURE");

    private String value;

    StatusCodeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static StatusCodeEnum fromValue(String text) {
      for (StatusCodeEnum b : StatusCodeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<StatusCodeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final StatusCodeEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public StatusCodeEnum read(final JsonReader jsonReader) throws IOException {
        String value = jsonReader.nextString();
        return StatusCodeEnum.fromValue(String.valueOf(value));
      }
    }
  }  @SerializedName("statusCode")
  private StatusCodeEnum statusCode = null;

  public StatusMessage errorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
    return this;
  }

   /**
   * Additional message in case of a failure
   * @return errorMessage
  **/
  @Schema(description = "Additional message in case of a failure")
  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public StatusMessage errorCode(ErrorCodeEnum errorCode) {
    this.errorCode = errorCode;
    return this;
  }

   /**
   * The type of error in case of a failure
   * @return errorCode
  **/
  @Schema(description = "The type of error in case of a failure")
  public ErrorCodeEnum getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(ErrorCodeEnum errorCode) {
    this.errorCode = errorCode;
  }

  public StatusMessage connectionId(String connectionId) {
    this.connectionId = connectionId;
    return this;
  }

   /**
   * The connection id
   * @return connectionId
  **/
  @Schema(description = "The connection id")
  public String getConnectionId() {
    return connectionId;
  }

  public void setConnectionId(String connectionId) {
    this.connectionId = connectionId;
  }

  public StatusMessage connectionClosed(Boolean connectionClosed) {
    this.connectionClosed = connectionClosed;
    return this;
  }

   /**
   * Is the connection now closed
   * @return connectionClosed
  **/
  @Schema(description = "Is the connection now closed")
  public Boolean isConnectionClosed() {
    return connectionClosed;
  }

  public void setConnectionClosed(Boolean connectionClosed) {
    this.connectionClosed = connectionClosed;
  }

  public StatusMessage statusCode(StatusCodeEnum statusCode) {
    this.statusCode = statusCode;
    return this;
  }

   /**
   * The status of the last request
   * @return statusCode
  **/
  @Schema(description = "The status of the last request")
  public StatusCodeEnum getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(StatusCodeEnum statusCode) {
    this.statusCode = statusCode;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StatusMessage statusMessage = (StatusMessage) o;
    return Objects.equals(this.errorMessage, statusMessage.errorMessage) &&
        Objects.equals(this.errorCode, statusMessage.errorCode) &&
        Objects.equals(this.connectionId, statusMessage.connectionId) &&
        Objects.equals(this.connectionClosed, statusMessage.connectionClosed) &&
        Objects.equals(this.statusCode, statusMessage.statusCode) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(errorMessage, errorCode, connectionId, connectionClosed, statusCode, super.hashCode());
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StatusMessage {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    errorMessage: ").append(toIndentedString(errorMessage)).append("\n");
    sb.append("    errorCode: ").append(toIndentedString(errorCode)).append("\n");
    sb.append("    connectionId: ").append(toIndentedString(connectionId)).append("\n");
    sb.append("    connectionClosed: ").append(toIndentedString(connectionClosed)).append("\n");
    sb.append("    statusCode: ").append(toIndentedString(statusCode)).append("\n");
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
