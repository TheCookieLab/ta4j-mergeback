/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017-2023 Ta4j Organization & respective
 * authors (see AUTHORS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.ta4j.core.indicators.starc;

import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.ATRIndicator;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.indicators.helpers.TransformIndicator;
import org.ta4j.core.num.Num;

/**
 * STARC Bands Lower Indicator.
 * <p>
 * The Lower STARC Band is calculated by subtracting a multiple of the Average
 * True Range (ATR) from the middle STARC band (which is a Simple Moving
 * Average).
 *
 * @see <a href="https://www.stockmaniacs.net/starc-bands-indicator/">STARC
 *      Bands Indicator</a>
 */
public class StarcBandsLowerIndicator extends CachedIndicator<Num> {

    private final StarcBandsMiddleIndicator starcBandsMiddleIndicator;
    private final Indicator<Num> atr;
    private final Num atrMultiplier;
    private final int barCount;

    /**
     * Constructor.
     *
     * @param middle        the middle STARC Band indicator
     * @param barCount      the bar count for the ATR calculation
     * @param atrMultiplier the multiplier for the ATR value
     */
    public StarcBandsLowerIndicator(StarcBandsMiddleIndicator middle, int barCount, Number atrMultiplier) {
        super(middle.getBarSeries());
        this.starcBandsMiddleIndicator = middle;
        this.atr = TransformIndicator.multiply(new ATRIndicator(getBarSeries(), barCount), atrMultiplier);
        this.atrMultiplier = numOf(atrMultiplier);
        this.barCount = barCount;
    }

    @Override
    protected Num calculate(int index) {
        // The lower band is the middle band minus a multiple of the ATR
        return starcBandsMiddleIndicator.getValue(index).minus(atr.getValue(index));
    }

    @Override
    public int getUnstableBars() {
        return this.barCount;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " barCount: " + this.barCount;
    }
}
