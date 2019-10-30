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
import java.util.ArrayList;
import java.util.List;
import org.threeten.bp.OffsetDateTime;
/**
 * MarketDefinition
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2019-10-07T12:34:48.137476+03:00[Europe/Riga]")
public class MarketDefinition {
  @SerializedName("venue")
  private String venue = null;

  @SerializedName("raceType")
  private String raceType = null;

  @SerializedName("settledTime")
  private OffsetDateTime settledTime = null;

  @SerializedName("timezone")
  private String timezone = null;

  @SerializedName("eachWayDivisor")
  private Double eachWayDivisor = null;

  @SerializedName("regulators")
  private List<String> regulators = null;

  @SerializedName("marketType")
  private String marketType = null;

  @SerializedName("marketBaseRate")
  private Double marketBaseRate = null;

  @SerializedName("numberOfWinners")
  private Integer numberOfWinners = null;

  @SerializedName("countryCode")
  private String countryCode = null;

  @SerializedName("lineMaxUnit")
  private Double lineMaxUnit = null;

  @SerializedName("inPlay")
  private Boolean inPlay = null;

  @SerializedName("betDelay")
  private Integer betDelay = null;

  @SerializedName("bspMarket")
  private Boolean bspMarket = null;

  /**
   * Gets or Sets bettingType
   */
  @JsonAdapter(BettingTypeEnum.Adapter.class)
  public enum BettingTypeEnum {
    ODDS("ODDS"),
    LINE("LINE"),
    RANGE("RANGE"),
    ASIAN_HANDICAP_DOUBLE_LINE("ASIAN_HANDICAP_DOUBLE_LINE"),
    ASIAN_HANDICAP_SINGLE_LINE("ASIAN_HANDICAP_SINGLE_LINE");

    private String value;

    BettingTypeEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static BettingTypeEnum fromValue(String text) {
      for (BettingTypeEnum b : BettingTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<BettingTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final BettingTypeEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public BettingTypeEnum read(final JsonReader jsonReader) throws IOException {
        String value = jsonReader.nextString();
        return BettingTypeEnum.fromValue(String.valueOf(value));
      }
    }
  }  @SerializedName("bettingType")
  private BettingTypeEnum bettingType = null;

  @SerializedName("numberOfActiveRunners")
  private Integer numberOfActiveRunners = null;

  @SerializedName("lineMinUnit")
  private Double lineMinUnit = null;

  @SerializedName("eventId")
  private String eventId = null;

  @SerializedName("crossMatching")
  private Boolean crossMatching = null;

  @SerializedName("runnersVoidable")
  private Boolean runnersVoidable = null;

  @SerializedName("turnInPlayEnabled")
  private Boolean turnInPlayEnabled = null;

  @SerializedName("priceLadderDefinition")
  private PriceLadderDefinition priceLadderDefinition = null;

  @SerializedName("keyLineDefinition")
  private KeyLineDefinition keyLineDefinition = null;

  @SerializedName("suspendTime")
  private OffsetDateTime suspendTime = null;

  @SerializedName("discountAllowed")
  private Boolean discountAllowed = null;

  @SerializedName("persistenceEnabled")
  private Boolean persistenceEnabled = null;

  @SerializedName("runners")
  private List<RunnerDefinition> runners = null;

  @SerializedName("version")
  private Long version = null;

  @SerializedName("eventTypeId")
  private String eventTypeId = null;

  @SerializedName("complete")
  private Boolean complete = null;

  @SerializedName("openDate")
  private OffsetDateTime openDate = null;

  @SerializedName("marketTime")
  private OffsetDateTime marketTime = null;

  @SerializedName("bspReconciled")
  private Boolean bspReconciled = null;

  @SerializedName("lineInterval")
  private Double lineInterval = null;

  /**
   * Gets or Sets status
   */
  @JsonAdapter(StatusEnum.Adapter.class)
  public enum StatusEnum {
    INACTIVE("INACTIVE"),
    OPEN("OPEN"),
    SUSPENDED("SUSPENDED"),
    CLOSED("CLOSED");

    private String value;

    StatusEnum(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    public static StatusEnum fromValue(String text) {
      for (StatusEnum b : StatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
    public static class Adapter extends TypeAdapter<StatusEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final StatusEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public StatusEnum read(final JsonReader jsonReader) throws IOException {
        String value = jsonReader.nextString();
        return StatusEnum.fromValue(String.valueOf(value));
      }
    }
  }  @SerializedName("status")
  private StatusEnum status = null;

  public MarketDefinition venue(String venue) {
    this.venue = venue;
    return this;
  }

   /**
   * Get venue
   * @return venue
  **/
  @Schema(description = "")
  public String getVenue() {
    return venue;
  }

  public void setVenue(String venue) {
    this.venue = venue;
  }

  public MarketDefinition raceType(String raceType) {
    this.raceType = raceType;
    return this;
  }

   /**
   * Get raceType
   * @return raceType
  **/
  @Schema(description = "")
  public String getRaceType() {
    return raceType;
  }

  public void setRaceType(String raceType) {
    this.raceType = raceType;
  }

  public MarketDefinition settledTime(OffsetDateTime settledTime) {
    this.settledTime = settledTime;
    return this;
  }

   /**
   * Get settledTime
   * @return settledTime
  **/
  @Schema(description = "")
  public OffsetDateTime getSettledTime() {
    return settledTime;
  }

  public void setSettledTime(OffsetDateTime settledTime) {
    this.settledTime = settledTime;
  }

  public MarketDefinition timezone(String timezone) {
    this.timezone = timezone;
    return this;
  }

   /**
   * Get timezone
   * @return timezone
  **/
  @Schema(description = "")
  public String getTimezone() {
    return timezone;
  }

  public void setTimezone(String timezone) {
    this.timezone = timezone;
  }

  public MarketDefinition eachWayDivisor(Double eachWayDivisor) {
    this.eachWayDivisor = eachWayDivisor;
    return this;
  }

   /**
   * Get eachWayDivisor
   * @return eachWayDivisor
  **/
  @Schema(description = "")
  public Double getEachWayDivisor() {
    return eachWayDivisor;
  }

  public void setEachWayDivisor(Double eachWayDivisor) {
    this.eachWayDivisor = eachWayDivisor;
  }

  public MarketDefinition regulators(List<String> regulators) {
    this.regulators = regulators;
    return this;
  }

  public MarketDefinition addRegulatorsItem(String regulatorsItem) {
    if (this.regulators == null) {
      this.regulators = new ArrayList<String>();
    }
    this.regulators.add(regulatorsItem);
    return this;
  }

   /**
   * The market regulators.
   * @return regulators
  **/
  @Schema(description = "The market regulators.")
  public List<String> getRegulators() {
    return regulators;
  }

  public void setRegulators(List<String> regulators) {
    this.regulators = regulators;
  }

  public MarketDefinition marketType(String marketType) {
    this.marketType = marketType;
    return this;
  }

   /**
   * Get marketType
   * @return marketType
  **/
  @Schema(description = "")
  public String getMarketType() {
    return marketType;
  }

  public void setMarketType(String marketType) {
    this.marketType = marketType;
  }

  public MarketDefinition marketBaseRate(Double marketBaseRate) {
    this.marketBaseRate = marketBaseRate;
    return this;
  }

   /**
   * Get marketBaseRate
   * @return marketBaseRate
  **/
  @Schema(description = "")
  public Double getMarketBaseRate() {
    return marketBaseRate;
  }

  public void setMarketBaseRate(Double marketBaseRate) {
    this.marketBaseRate = marketBaseRate;
  }

  public MarketDefinition numberOfWinners(Integer numberOfWinners) {
    this.numberOfWinners = numberOfWinners;
    return this;
  }

   /**
   * Get numberOfWinners
   * @return numberOfWinners
  **/
  @Schema(description = "")
  public Integer getNumberOfWinners() {
    return numberOfWinners;
  }

  public void setNumberOfWinners(Integer numberOfWinners) {
    this.numberOfWinners = numberOfWinners;
  }

  public MarketDefinition countryCode(String countryCode) {
    this.countryCode = countryCode;
    return this;
  }

   /**
   * Get countryCode
   * @return countryCode
  **/
  @Schema(description = "")
  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public MarketDefinition lineMaxUnit(Double lineMaxUnit) {
    this.lineMaxUnit = lineMaxUnit;
    return this;
  }

   /**
   * For Handicap and Line markets, the maximum value for the outcome, in market units for this market (eg 100 runs).
   * @return lineMaxUnit
  **/
  @Schema(description = "For Handicap and Line markets, the maximum value for the outcome, in market units for this market (eg 100 runs).")
  public Double getLineMaxUnit() {
    return lineMaxUnit;
  }

  public void setLineMaxUnit(Double lineMaxUnit) {
    this.lineMaxUnit = lineMaxUnit;
  }

  public MarketDefinition inPlay(Boolean inPlay) {
    this.inPlay = inPlay;
    return this;
  }

   /**
   * Get inPlay
   * @return inPlay
  **/
  @Schema(description = "")
  public Boolean isInPlay() {
    return inPlay;
  }

  public void setInPlay(Boolean inPlay) {
    this.inPlay = inPlay;
  }

  public MarketDefinition betDelay(Integer betDelay) {
    this.betDelay = betDelay;
    return this;
  }

   /**
   * Get betDelay
   * @return betDelay
  **/
  @Schema(description = "")
  public Integer getBetDelay() {
    return betDelay;
  }

  public void setBetDelay(Integer betDelay) {
    this.betDelay = betDelay;
  }

  public MarketDefinition bspMarket(Boolean bspMarket) {
    this.bspMarket = bspMarket;
    return this;
  }

   /**
   * Get bspMarket
   * @return bspMarket
  **/
  @Schema(description = "")
  public Boolean isBspMarket() {
    return bspMarket;
  }

  public void setBspMarket(Boolean bspMarket) {
    this.bspMarket = bspMarket;
  }

  public MarketDefinition bettingType(BettingTypeEnum bettingType) {
    this.bettingType = bettingType;
    return this;
  }

   /**
   * Get bettingType
   * @return bettingType
  **/
  @Schema(description = "")
  public BettingTypeEnum getBettingType() {
    return bettingType;
  }

  public void setBettingType(BettingTypeEnum bettingType) {
    this.bettingType = bettingType;
  }

  public MarketDefinition numberOfActiveRunners(Integer numberOfActiveRunners) {
    this.numberOfActiveRunners = numberOfActiveRunners;
    return this;
  }

   /**
   * Get numberOfActiveRunners
   * @return numberOfActiveRunners
  **/
  @Schema(description = "")
  public Integer getNumberOfActiveRunners() {
    return numberOfActiveRunners;
  }

  public void setNumberOfActiveRunners(Integer numberOfActiveRunners) {
    this.numberOfActiveRunners = numberOfActiveRunners;
  }

  public MarketDefinition lineMinUnit(Double lineMinUnit) {
    this.lineMinUnit = lineMinUnit;
    return this;
  }

   /**
   * For Handicap and Line markets, the minimum value for the outcome, in market units for this market (eg 0 runs).
   * @return lineMinUnit
  **/
  @Schema(description = "For Handicap and Line markets, the minimum value for the outcome, in market units for this market (eg 0 runs).")
  public Double getLineMinUnit() {
    return lineMinUnit;
  }

  public void setLineMinUnit(Double lineMinUnit) {
    this.lineMinUnit = lineMinUnit;
  }

  public MarketDefinition eventId(String eventId) {
    this.eventId = eventId;
    return this;
  }

   /**
   * Get eventId
   * @return eventId
  **/
  @Schema(description = "")
  public String getEventId() {
    return eventId;
  }

  public void setEventId(String eventId) {
    this.eventId = eventId;
  }

  public MarketDefinition crossMatching(Boolean crossMatching) {
    this.crossMatching = crossMatching;
    return this;
  }

   /**
   * Get crossMatching
   * @return crossMatching
  **/
  @Schema(description = "")
  public Boolean isCrossMatching() {
    return crossMatching;
  }

  public void setCrossMatching(Boolean crossMatching) {
    this.crossMatching = crossMatching;
  }

  public MarketDefinition runnersVoidable(Boolean runnersVoidable) {
    this.runnersVoidable = runnersVoidable;
    return this;
  }

   /**
   * Get runnersVoidable
   * @return runnersVoidable
  **/
  @Schema(description = "")
  public Boolean isRunnersVoidable() {
    return runnersVoidable;
  }

  public void setRunnersVoidable(Boolean runnersVoidable) {
    this.runnersVoidable = runnersVoidable;
  }

  public MarketDefinition turnInPlayEnabled(Boolean turnInPlayEnabled) {
    this.turnInPlayEnabled = turnInPlayEnabled;
    return this;
  }

   /**
   * Get turnInPlayEnabled
   * @return turnInPlayEnabled
  **/
  @Schema(description = "")
  public Boolean isTurnInPlayEnabled() {
    return turnInPlayEnabled;
  }

  public void setTurnInPlayEnabled(Boolean turnInPlayEnabled) {
    this.turnInPlayEnabled = turnInPlayEnabled;
  }

  public MarketDefinition priceLadderDefinition(PriceLadderDefinition priceLadderDefinition) {
    this.priceLadderDefinition = priceLadderDefinition;
    return this;
  }

   /**
   * Get priceLadderDefinition
   * @return priceLadderDefinition
  **/
  @Schema(description = "")
  public PriceLadderDefinition getPriceLadderDefinition() {
    return priceLadderDefinition;
  }

  public void setPriceLadderDefinition(PriceLadderDefinition priceLadderDefinition) {
    this.priceLadderDefinition = priceLadderDefinition;
  }

  public MarketDefinition keyLineDefinition(KeyLineDefinition keyLineDefinition) {
    this.keyLineDefinition = keyLineDefinition;
    return this;
  }

   /**
   * Get keyLineDefinition
   * @return keyLineDefinition
  **/
  @Schema(description = "")
  public KeyLineDefinition getKeyLineDefinition() {
    return keyLineDefinition;
  }

  public void setKeyLineDefinition(KeyLineDefinition keyLineDefinition) {
    this.keyLineDefinition = keyLineDefinition;
  }

  public MarketDefinition suspendTime(OffsetDateTime suspendTime) {
    this.suspendTime = suspendTime;
    return this;
  }

   /**
   * Get suspendTime
   * @return suspendTime
  **/
  @Schema(description = "")
  public OffsetDateTime getSuspendTime() {
    return suspendTime;
  }

  public void setSuspendTime(OffsetDateTime suspendTime) {
    this.suspendTime = suspendTime;
  }

  public MarketDefinition discountAllowed(Boolean discountAllowed) {
    this.discountAllowed = discountAllowed;
    return this;
  }

   /**
   * Get discountAllowed
   * @return discountAllowed
  **/
  @Schema(description = "")
  public Boolean isDiscountAllowed() {
    return discountAllowed;
  }

  public void setDiscountAllowed(Boolean discountAllowed) {
    this.discountAllowed = discountAllowed;
  }

  public MarketDefinition persistenceEnabled(Boolean persistenceEnabled) {
    this.persistenceEnabled = persistenceEnabled;
    return this;
  }

   /**
   * Get persistenceEnabled
   * @return persistenceEnabled
  **/
  @Schema(description = "")
  public Boolean isPersistenceEnabled() {
    return persistenceEnabled;
  }

  public void setPersistenceEnabled(Boolean persistenceEnabled) {
    this.persistenceEnabled = persistenceEnabled;
  }

  public MarketDefinition runners(List<RunnerDefinition> runners) {
    this.runners = runners;
    return this;
  }

  public MarketDefinition addRunnersItem(RunnerDefinition runnersItem) {
    if (this.runners == null) {
      this.runners = new ArrayList<RunnerDefinition>();
    }
    this.runners.add(runnersItem);
    return this;
  }

   /**
   * Get runners
   * @return runners
  **/
  @Schema(description = "")
  public List<RunnerDefinition> getRunners() {
    return runners;
  }

  public void setRunners(List<RunnerDefinition> runners) {
    this.runners = runners;
  }

  public MarketDefinition version(Long version) {
    this.version = version;
    return this;
  }

   /**
   * Get version
   * @return version
  **/
  @Schema(description = "")
  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public MarketDefinition eventTypeId(String eventTypeId) {
    this.eventTypeId = eventTypeId;
    return this;
  }

   /**
   * The Event Type the market is contained within.
   * @return eventTypeId
  **/
  @Schema(description = "The Event Type the market is contained within.")
  public String getEventTypeId() {
    return eventTypeId;
  }

  public void setEventTypeId(String eventTypeId) {
    this.eventTypeId = eventTypeId;
  }

  public MarketDefinition complete(Boolean complete) {
    this.complete = complete;
    return this;
  }

   /**
   * Get complete
   * @return complete
  **/
  @Schema(description = "")
  public Boolean isComplete() {
    return complete;
  }

  public void setComplete(Boolean complete) {
    this.complete = complete;
  }

  public MarketDefinition openDate(OffsetDateTime openDate) {
    this.openDate = openDate;
    return this;
  }

   /**
   * Get openDate
   * @return openDate
  **/
  @Schema(description = "")
  public OffsetDateTime getOpenDate() {
    return openDate;
  }

  public void setOpenDate(OffsetDateTime openDate) {
    this.openDate = openDate;
  }

  public MarketDefinition marketTime(OffsetDateTime marketTime) {
    this.marketTime = marketTime;
    return this;
  }

   /**
   * Get marketTime
   * @return marketTime
  **/
  @Schema(description = "")
  public OffsetDateTime getMarketTime() {
    return marketTime;
  }

  public void setMarketTime(OffsetDateTime marketTime) {
    this.marketTime = marketTime;
  }

  public MarketDefinition bspReconciled(Boolean bspReconciled) {
    this.bspReconciled = bspReconciled;
    return this;
  }

   /**
   * Get bspReconciled
   * @return bspReconciled
  **/
  @Schema(description = "")
  public Boolean isBspReconciled() {
    return bspReconciled;
  }

  public void setBspReconciled(Boolean bspReconciled) {
    this.bspReconciled = bspReconciled;
  }

  public MarketDefinition lineInterval(Double lineInterval) {
    this.lineInterval = lineInterval;
    return this;
  }

   /**
   * For Handicap and Line markets, the lines available on this market will be between the range of lineMinUnit and lineMaxUnit, in increments of the lineInterval value. e.g. If unit is runs, lineMinUnit&#x3D;10, lineMaxUnit&#x3D;20 and lineInterval&#x3D;0.5, then valid lines include 10, 10.5, 11, 11.5 up to 20 runs.
   * @return lineInterval
  **/
  @Schema(description = "For Handicap and Line markets, the lines available on this market will be between the range of lineMinUnit and lineMaxUnit, in increments of the lineInterval value. e.g. If unit is runs, lineMinUnit=10, lineMaxUnit=20 and lineInterval=0.5, then valid lines include 10, 10.5, 11, 11.5 up to 20 runs.")
  public Double getLineInterval() {
    return lineInterval;
  }

  public void setLineInterval(Double lineInterval) {
    this.lineInterval = lineInterval;
  }

  public MarketDefinition status(StatusEnum status) {
    this.status = status;
    return this;
  }

   /**
   * Get status
   * @return status
  **/
  @Schema(description = "")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
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
    MarketDefinition marketDefinition = (MarketDefinition) o;
    return Objects.equals(this.venue, marketDefinition.venue) &&
        Objects.equals(this.raceType, marketDefinition.raceType) &&
        Objects.equals(this.settledTime, marketDefinition.settledTime) &&
        Objects.equals(this.timezone, marketDefinition.timezone) &&
        Objects.equals(this.eachWayDivisor, marketDefinition.eachWayDivisor) &&
        Objects.equals(this.regulators, marketDefinition.regulators) &&
        Objects.equals(this.marketType, marketDefinition.marketType) &&
        Objects.equals(this.marketBaseRate, marketDefinition.marketBaseRate) &&
        Objects.equals(this.numberOfWinners, marketDefinition.numberOfWinners) &&
        Objects.equals(this.countryCode, marketDefinition.countryCode) &&
        Objects.equals(this.lineMaxUnit, marketDefinition.lineMaxUnit) &&
        Objects.equals(this.inPlay, marketDefinition.inPlay) &&
        Objects.equals(this.betDelay, marketDefinition.betDelay) &&
        Objects.equals(this.bspMarket, marketDefinition.bspMarket) &&
        Objects.equals(this.bettingType, marketDefinition.bettingType) &&
        Objects.equals(this.numberOfActiveRunners, marketDefinition.numberOfActiveRunners) &&
        Objects.equals(this.lineMinUnit, marketDefinition.lineMinUnit) &&
        Objects.equals(this.eventId, marketDefinition.eventId) &&
        Objects.equals(this.crossMatching, marketDefinition.crossMatching) &&
        Objects.equals(this.runnersVoidable, marketDefinition.runnersVoidable) &&
        Objects.equals(this.turnInPlayEnabled, marketDefinition.turnInPlayEnabled) &&
        Objects.equals(this.priceLadderDefinition, marketDefinition.priceLadderDefinition) &&
        Objects.equals(this.keyLineDefinition, marketDefinition.keyLineDefinition) &&
        Objects.equals(this.suspendTime, marketDefinition.suspendTime) &&
        Objects.equals(this.discountAllowed, marketDefinition.discountAllowed) &&
        Objects.equals(this.persistenceEnabled, marketDefinition.persistenceEnabled) &&
        Objects.equals(this.runners, marketDefinition.runners) &&
        Objects.equals(this.version, marketDefinition.version) &&
        Objects.equals(this.eventTypeId, marketDefinition.eventTypeId) &&
        Objects.equals(this.complete, marketDefinition.complete) &&
        Objects.equals(this.openDate, marketDefinition.openDate) &&
        Objects.equals(this.marketTime, marketDefinition.marketTime) &&
        Objects.equals(this.bspReconciled, marketDefinition.bspReconciled) &&
        Objects.equals(this.lineInterval, marketDefinition.lineInterval) &&
        Objects.equals(this.status, marketDefinition.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(venue, raceType, settledTime, timezone, eachWayDivisor, regulators, marketType, marketBaseRate, numberOfWinners, countryCode, lineMaxUnit, inPlay, betDelay, bspMarket, bettingType, numberOfActiveRunners, lineMinUnit, eventId, crossMatching, runnersVoidable, turnInPlayEnabled, priceLadderDefinition, keyLineDefinition, suspendTime, discountAllowed, persistenceEnabled, runners, version, eventTypeId, complete, openDate, marketTime, bspReconciled, lineInterval, status);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MarketDefinition {\n");
    
    sb.append("    venue: ").append(toIndentedString(venue)).append("\n");
    sb.append("    raceType: ").append(toIndentedString(raceType)).append("\n");
    sb.append("    settledTime: ").append(toIndentedString(settledTime)).append("\n");
    sb.append("    timezone: ").append(toIndentedString(timezone)).append("\n");
    sb.append("    eachWayDivisor: ").append(toIndentedString(eachWayDivisor)).append("\n");
    sb.append("    regulators: ").append(toIndentedString(regulators)).append("\n");
    sb.append("    marketType: ").append(toIndentedString(marketType)).append("\n");
    sb.append("    marketBaseRate: ").append(toIndentedString(marketBaseRate)).append("\n");
    sb.append("    numberOfWinners: ").append(toIndentedString(numberOfWinners)).append("\n");
    sb.append("    countryCode: ").append(toIndentedString(countryCode)).append("\n");
    sb.append("    lineMaxUnit: ").append(toIndentedString(lineMaxUnit)).append("\n");
    sb.append("    inPlay: ").append(toIndentedString(inPlay)).append("\n");
    sb.append("    betDelay: ").append(toIndentedString(betDelay)).append("\n");
    sb.append("    bspMarket: ").append(toIndentedString(bspMarket)).append("\n");
    sb.append("    bettingType: ").append(toIndentedString(bettingType)).append("\n");
    sb.append("    numberOfActiveRunners: ").append(toIndentedString(numberOfActiveRunners)).append("\n");
    sb.append("    lineMinUnit: ").append(toIndentedString(lineMinUnit)).append("\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    crossMatching: ").append(toIndentedString(crossMatching)).append("\n");
    sb.append("    runnersVoidable: ").append(toIndentedString(runnersVoidable)).append("\n");
    sb.append("    turnInPlayEnabled: ").append(toIndentedString(turnInPlayEnabled)).append("\n");
    sb.append("    priceLadderDefinition: ").append(toIndentedString(priceLadderDefinition)).append("\n");
    sb.append("    keyLineDefinition: ").append(toIndentedString(keyLineDefinition)).append("\n");
    sb.append("    suspendTime: ").append(toIndentedString(suspendTime)).append("\n");
    sb.append("    discountAllowed: ").append(toIndentedString(discountAllowed)).append("\n");
    sb.append("    persistenceEnabled: ").append(toIndentedString(persistenceEnabled)).append("\n");
    sb.append("    runners: ").append(toIndentedString(runners)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    eventTypeId: ").append(toIndentedString(eventTypeId)).append("\n");
    sb.append("    complete: ").append(toIndentedString(complete)).append("\n");
    sb.append("    openDate: ").append(toIndentedString(openDate)).append("\n");
    sb.append("    marketTime: ").append(toIndentedString(marketTime)).append("\n");
    sb.append("    bspReconciled: ").append(toIndentedString(bspReconciled)).append("\n");
    sb.append("    lineInterval: ").append(toIndentedString(lineInterval)).append("\n");
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