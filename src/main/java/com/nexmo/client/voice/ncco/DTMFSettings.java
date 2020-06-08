package com.nexmo.client.voice.ncco;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * DTMF(Dial Tone Multi Frequency) settings for Input Actions that will be added to a NCCO object.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class DTMFSettings {
    private Integer timeOut;
    private Integer maxDigits;
    private Boolean submitOnHash;

    public Integer getTimeOut() {
        return timeOut;
    }

    /**
     * @param timeOut The result of the callee's activity is sent to the eventUrl webhook endpoint timeOut seconds
     *                after the last action. The default value is 3. Max is 10.
     */
    public void setTimeOut(Integer timeOut) {
        this.timeOut = timeOut;
    }


    public Integer getMaxDigits() {
        return maxDigits;
    }

    /**
     * @param maxDigits The number of digits the user can press. The maximum value is 20, the default is 4 digits.
     */
    public void setMaxDigits(Integer maxDigits) {
        this.maxDigits = maxDigits;
    }

    public Boolean isSubmitOnHash() {
        return submitOnHash;
    }

    /**
     * @param submitOnHash Set to true so the callee's activity is sent to your webhook endpoint at eventUrl after
     *                     he or she presses #. If # is not pressed the result is submitted after timeOut seconds.
     *                     The default value is false. That is, the result is sent to your webhook endpoint after
     *                     timeOut seconds.
     */
    public void setSubmitOnHash(Boolean submitOnHash) {
        this.submitOnHash = submitOnHash;
    }
}
