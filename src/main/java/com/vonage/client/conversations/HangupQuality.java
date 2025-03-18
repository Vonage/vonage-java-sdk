package com.vonage.client.conversations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;

/**
 * Represents the {@code quality} field in the body of a hangup event.
 *
 * @since 8.20.0
 */
public class HangupQuality extends JsonableBaseObject {
    private Integer mosScore, qualityPercentage, flawTotal, packetCnt, packetLossPerc,
            jitterMinVar, jitterMaxVar, jitterLossRate, jitterBurstRate;

    HangupQuality() {
    }

    /**
     * Gets the MOS score.
     *
     * @return The MOS score.
     */
    @JsonProperty("mos_score")
    public Integer getMosScore() {
        return mosScore;
    }

    /**
     * Gets the quality percentage.
     *
     * @return The quality percentage.
     */
    @JsonProperty("quality_percentage")
    public Integer getQualityPercentage() {
        return qualityPercentage;
    }

    /**
     * Gets the minimum jitter variance.
     *
     * @return The minimum jitter variance.
     */
    @JsonProperty("jitter_min_var")
    public Integer getJitterMinVar() {
        return jitterMinVar;
    }

    /**
     * Gets the maximum jitter variance.
     *
     * @return The maximum jitter variance.
     */
    @JsonProperty("jitter_max_var")
    public Integer getJitterMaxVar() {
        return jitterMaxVar;
    }

    /**
     * Gets the jitter loss rate.
     *
     * @return The jitter loss rate.
     */
    @JsonProperty("jitter_loss_rate")
    public Integer getJitterLossRate() {
        return jitterLossRate;
    }

    /**
     * Gets the jitter burst rate.
     *
     * @return The jitter burst rate.
     */
    @JsonProperty("jitter_burst_rate")
    public Integer getJitterBurstRate() {
        return jitterBurstRate;
    }

    /**
     * Gets the total number of flaws.
     *
     * @return The total number of flaws.
     */
    @JsonProperty("flaw_total")
    public Integer getFlawTotal() {
        return flawTotal;
    }

    /**
     * Gets the packet count.
     *
     * @return The packet count.
     */
    @JsonProperty("packet_cnt")
    public Integer getPacketCnt() {
        return packetCnt;
    }

    /**
     * Gets the packet loss percentage.
     *
     * @return The packet loss percentage.
     */
    @JsonProperty("packet_loss_perc")
    public Integer getPacketLossPerc() {
        return packetLossPerc;
    }
}
