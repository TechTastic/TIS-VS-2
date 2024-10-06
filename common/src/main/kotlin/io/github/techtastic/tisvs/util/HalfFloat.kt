/*
 * Copyright (C) 2019 The Android Open Source Project
 * Copyright (C) 2020 Know-Center GmbH Research Center for Data-Driven Business &amp; Big Data Analytics
 *                    This file has been modified as part of the HalfFloat project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */
/*
 * Taken from TIS Advanced by Zack Emmert
 * Code: https://gitlab.com/zack-emmert/tis-advanced/-/blob/1.20/src/main/java/cc/emmert/tisadvanced/util/HalfFloat.java?ref_type=heads
 * License: https://gitlab.com/zack-emmert/tis-advanced/-/blob/main/LICENSE?ref_type=heads
 */
package io.github.techtastic.tisvs.util

/**
 *
 * The `HalfFloat` class is a wrapper and a utility class to manipulate half-precision 16-bit
 * [IEEE 754](https://en.wikipedia.org/wiki/Half-precision_floating-point_format)
 * floating point data types (also called fp16 or binary16). A half-precision float can be
 * created from or converted to single-precision floats, and is stored in a short data type.
 *
 *
 * The IEEE 754 standard specifies an fp16 as having the following format:
 *
 *  * Sign bit: 1 bit
 *  * Exponent width: 5 bits
 *  * Significand: 10 bits
 *
 *
 *
 * The format is laid out as follows:
 * <pre>
 * 1   11111   1111111111
 * ^   --^--   -----^----
 * sign  |          |_______ significand
 * |
 * -- exponent
</pre> *
 *
 *
 * Half-precision floating points can be useful to save memory and/or
 * bandwidth at the expense of range and precision when compared to single-precision
 * floating points (fp32).
 *
 * To help you decide whether fp16 is the right storage type for you need, please
 * refer to the table below that shows the available precision throughout the range of
 * possible values. The *precision* column indicates the step size between two
 * consecutive numbers in a specific part of the range.
 *
 * <table summary="Precision of fp16 across the range">
 * <tr><th>Range start</th><th>Precision</th></tr>
 * <tr><td>0</td><td>1  16,777,216</td></tr>
 * <tr><td>1  16,384</td><td>1  16,777,216</td></tr>
 * <tr><td>1  8,192</td><td>1  8,388,608</td></tr>
 * <tr><td>1  4,096</td><td>1  4,194,304</td></tr>
 * <tr><td>1  2,048</td><td>1  2,097,152</td></tr>
 * <tr><td>1  1,024</td><td>1  1,048,576</td></tr>
 * <tr><td>1  512</td><td>1  524,288</td></tr>
 * <tr><td>1  256</td><td>1  262,144</td></tr>
 * <tr><td>1  128</td><td>1  131,072</td></tr>
 * <tr><td>1  64</td><td>1  65,536</td></tr>
 * <tr><td>1  32</td><td>1  32,768</td></tr>
 * <tr><td>1  16</td><td>1  16,384</td></tr>
 * <tr><td>1  8</td><td>1  8,192</td></tr>
 * <tr><td>1  4</td><td>1  4,096</td></tr>
 * <tr><td>1  2</td><td>1  2,048</td></tr>
 * <tr><td>1</td><td>1  1,024</td></tr>
 * <tr><td>2</td><td>1  512</td></tr>
 * <tr><td>4</td><td>1  256</td></tr>
 * <tr><td>8</td><td>1  128</td></tr>
 * <tr><td>16</td><td>1  64</td></tr>
 * <tr><td>32</td><td>1  32</td></tr>
 * <tr><td>64</td><td>1  16</td></tr>
 * <tr><td>128</td><td>1  8</td></tr>
 * <tr><td>256</td><td>1  4</td></tr>
 * <tr><td>512</td><td>1  2</td></tr>
 * <tr><td>1,024</td><td>1</td></tr>
 * <tr><td>2,048</td><td>2</td></tr>
 * <tr><td>4,096</td><td>4</td></tr>
 * <tr><td>8,192</td><td>8</td></tr>
 * <tr><td>16,384</td><td>16</td></tr>
 * <tr><td>32,768</td><td>32</td></tr>
</table> *
 *
 *
 * This table shows that numbers higher than 1024 lose all fractional precision.
 *
 * @hide
 */
object HalfFloat {
    /**
     * The number of bits used to represent a half-precision float value.
     */
    const val SIZE: Int = 16

    /**
     * Epsilon is the difference between 1.0 and the next value representable
     * by a half-precision floating-point.
     */
    const val EPSILON: Short = 0x1400.toShort()

    /**
     * Maximum exponent a finite half-precision float may have.
     */
    const val MAX_EXPONENT: Int = 15

    /**
     * Minimum exponent a normalized half-precision float may have.
     */
    const val MIN_EXPONENT: Int = -14

    /**
     * Smallest negative value a half-precision float may have.
     */
    const val LOWEST_VALUE: Short = 0xfbff.toShort()

    /**
     * Maximum positive finite value a half-precision float may have.
     */
    const val MAX_VALUE: Short = 0x7bff.toShort()

    /**
     * Smallest positive normal value a half-precision float may have.
     */
    const val MIN_NORMAL: Short = 0x0400.toShort()

    /**
     * Smallest positive non-zero value a half-precision float may have.
     */
    const val MIN_VALUE: Short = 0x0001.toShort()

    /**
     * A Not-a-Number representation of a half-precision float.
     */
    const val NaN: Short = 0x7e00.toShort()

    /**
     * Negative infinity of type half-precision float.
     */
    const val NEGATIVE_INFINITY: Short = 0xfc00.toShort()

    /**
     * Negative 0 of type half-precision float.
     */
    const val NEGATIVE_ZERO: Short = 0x8000.toShort()

    /**
     * Positive infinity of type half-precision float.
     */
    const val POSITIVE_INFINITY: Short = 0x7c00.toShort()

    /**
     * Positive 0 of type half-precision float.
     */
    const val POSITIVE_ZERO: Short = 0x0000.toShort()
    const val SIGN_SHIFT: Int = 15
    const val EXPONENT_SHIFT: Int = 10
    const val SIGN_MASK: Int = 0x8000
    const val SHIFTED_EXPONENT_MASK: Int = 0x1f
    const val SIGNIFICAND_MASK: Int = 0x3ff
    const val EXPONENT_SIGNIFICAND_MASK: Int = 0x7fff
    const val EXPONENT_BIAS: Int = 15
    private const val FP32_SIGN_SHIFT = 31
    private const val FP32_EXPONENT_SHIFT = 23
    private const val FP32_SHIFTED_EXPONENT_MASK = 0xff
    private const val FP32_SIGNIFICAND_MASK = 0x7fffff
    private const val FP32_EXPONENT_BIAS = 127
    private const val FP32_QNAN_MASK = 0x400000
    private const val FP32_DENORMAL_MAGIC = 126 shl 23
    private val FP32_DENORMAL_FLOAT = java.lang.Float.intBitsToFloat(FP32_DENORMAL_MAGIC)

    /**
     *
     * Compares the two specified half-precision float values. The following
     * conditions apply during the comparison:
     *
     *
     *  * [.NaN] is considered by this method to be equal to itself and greater
     * than all other half-precision float values (including `#POSITIVE_INFINITY`)
     *  * [.POSITIVE_ZERO] is considered by this method to be greater than
     * [.NEGATIVE_ZERO].
     *
     *
     * @param x The first half-precision float value to compare.
     * @param y The second half-precision float value to compare
     * @return The value `0` if `x` is numerically equal to `y`, a
     * value less than `0` if `x` is numerically less than `y`,
     * and a value greater than `0` if `x` is numerically greater
     * than `y`
     */
    fun compare(x: Short, y: Short): Int {
        if (less(x, y)) return -1
        if (greater(x, y)) return 1
        // Collapse NaNs, akin to halfToIntBits(), but we want to keep
        // (signed) short value types to preserve the ordering of -0.0
        // and +0.0
        val xBits = if (isNaN(x)) NaN else x
        val yBits = if (isNaN(y)) NaN else y
        return (xBits.compareTo(yBits))
    }

    /**
     * Returns the closest integral half-precision float value to the specified
     * half-precision float value. Special values are handled in the
     * following ways:
     *
     *  * If the specified half-precision float is NaN, the result is NaN
     *  * If the specified half-precision float is infinity (negative or positive),
     * the result is infinity (with the same sign)
     *  * If the specified half-precision float is zero (negative or positive),
     * the result is zero (with the same sign)
     *
     *
     * @param h A half-precision float value
     * @return The value of the specified half-precision float rounded to the nearest
     * half-precision float value
     */
    fun rint(h: Short): Short {
        val bits = h.toInt() and 0xffff
        val abs = bits and EXPONENT_SIGNIFICAND_MASK
        var result = bits
        if (abs < 0x3c00) {
            result = result and SIGN_MASK
            if (abs > 0x3800) {
                result = result or 0x3c00
            }
        } else if (abs < 0x6400) {
            val exp = 25 - (abs shr 10)
            val mask = (1 shl exp) - 1
            result += ((1 shl (exp - 1)) - ((abs shr exp).inv() and 1))
            result = result and mask.inv()
        }
        if (isNaN(result.toShort())) {
            // if result is NaN mask with qNaN
            // (i.e. mask the most significant mantissa bit with 1)
            // to comply with hardware implementations (ARM64, Intel, etc).
            result = result or NaN.toInt()
        }
        return result.toShort()
    }

    /**
     * Returns the smallest half-precision float value toward negative infinity
     * greater than or equal to the specified half-precision float value.
     * Special values are handled in the following ways:
     *
     *  * If the specified half-precision float is NaN, the result is NaN
     *  * If the specified half-precision float is infinity (negative or positive),
     * the result is infinity (with the same sign)
     *  * If the specified half-precision float is zero (negative or positive),
     * the result is zero (with the same sign)
     *
     *
     * @param h A half-precision float value
     * @return The smallest half-precision float value toward negative infinity
     * greater than or equal to the specified half-precision float value
     */
    fun ceil(h: Short): Short {
        val bits = h.toInt() and 0xffff
        var abs = bits and EXPONENT_SIGNIFICAND_MASK
        var result = bits
        if (abs < 0x3c00) {
            result = result and SIGN_MASK
            result = result or (0x3c00 and -((bits shr 15).inv() and (if (abs != 0) 1 else 0)))
        } else if (abs < 0x6400) {
            abs = 25 - (abs shr 10)
            val mask = (1 shl abs) - 1
            result += mask and ((bits shr 15) - 1)
            result = result and mask.inv()
        }
        if (isNaN(result.toShort())) {
            // if result is NaN mask with qNaN
            // (i.e. mask the most significant mantissa bit with 1)
            // to comply with hardware implementations (ARM64, Intel, etc).
            result = result or NaN.toInt()
        }
        return result.toShort()
    }

    /**
     * Returns the largest half-precision float value toward positive infinity
     * less than or equal to the specified half-precision float value.
     * Special values are handled in the following ways:
     *
     *  * If the specified half-precision float is NaN, the result is NaN
     *  * If the specified half-precision float is infinity (negative or positive),
     * the result is infinity (with the same sign)
     *  * If the specified half-precision float is zero (negative or positive),
     * the result is zero (with the same sign)
     *
     *
     * @param h A half-precision float value
     * @return The largest half-precision float value toward positive infinity
     * less than or equal to the specified half-precision float value
     */
    fun floor(h: Short): Short {
        val bits = h.toInt() and 0xffff
        var abs = bits and EXPONENT_SIGNIFICAND_MASK
        var result = bits
        if (abs < 0x3c00) {
            result = result and SIGN_MASK
            result = result or (0x3c00 and (if (bits > 0x8000) 0xffff else 0x0))
        } else if (abs < 0x6400) {
            abs = 25 - (abs shr 10)
            val mask = (1 shl abs) - 1
            result += mask and -(bits shr 15)
            result = result and mask.inv()
        }
        if (isNaN(result.toShort())) {
            // if result is NaN mask with qNaN
            // i.e. (Mask the most significant mantissa bit with 1)
            result = result or NaN.toInt()
        }
        return result.toShort()
    }

    /**
     * Returns the truncated half-precision float value of the specified
     * half-precision float value. Special values are handled in the following ways:
     *
     *  * If the specified half-precision float is NaN, the result is NaN
     *  * If the specified half-precision float is infinity (negative or positive),
     * the result is infinity (with the same sign)
     *  * If the specified half-precision float is zero (negative or positive),
     * the result is zero (with the same sign)
     *
     *
     * @param h A half-precision float value
     * @return The truncated half-precision float value of the specified
     * half-precision float value
     */
    fun trunc(h: Short): Short {
        val bits = h.toInt() and 0xffff
        var abs = bits and EXPONENT_SIGNIFICAND_MASK
        var result = bits
        if (abs < 0x3c00) {
            result = result and SIGN_MASK
        } else if (abs < 0x6400) {
            abs = 25 - (abs shr 10)
            val mask = (1 shl abs) - 1
            result = result and mask.inv()
        }
        return result.toShort()
    }

    /**
     * Returns the smaller of two half-precision float values (the value closest
     * to negative infinity). Special values are handled in the following ways:
     *
     *  * If either value is NaN, the result is NaN
     *  * [.NEGATIVE_ZERO] is smaller than [.POSITIVE_ZERO]
     *
     *
     * @param x The first half-precision value
     * @param y The second half-precision value
     * @return The smaller of the two specified half-precision values
     */
    fun min(x: Short, y: Short): Short {
        if (isNaN(x)) return NaN
        if (isNaN(y)) return NaN
        if ((x.toInt() and EXPONENT_SIGNIFICAND_MASK) == 0 && (y.toInt() and EXPONENT_SIGNIFICAND_MASK) == 0) {
            return if ((x.toInt() and SIGN_MASK) != 0) x else y
        }
        return if ((if ((x.toInt() and SIGN_MASK) != 0) 0x8000 - (x.toInt() and 0xffff) else x.toInt() and 0xffff) <
            (if ((y.toInt() and SIGN_MASK) != 0) 0x8000 - (y.toInt() and 0xffff) else y.toInt() and 0xffff)
        ) x else y
    }

    /**
     * Returns the larger of two half-precision float values (the value closest
     * to positive infinity). Special values are handled in the following ways:
     *
     *  * If either value is NaN, the result is NaN
     *  * [.POSITIVE_ZERO] is greater than [.NEGATIVE_ZERO]
     *
     *
     * @param x The first half-precision value
     * @param y The second half-precision value
     * @return The larger of the two specified half-precision values
     */
    fun max(x: Short, y: Short): Short {
        if (isNaN(x)) return NaN
        if (isNaN(y)) return NaN
        if ((x.toInt() and EXPONENT_SIGNIFICAND_MASK) == 0 && (y.toInt() and EXPONENT_SIGNIFICAND_MASK) == 0) {
            return if ((x.toInt() and SIGN_MASK) != 0) y else x
        }
        return if ((if ((x.toInt() and SIGN_MASK) != 0) 0x8000 - (x.toInt() and 0xffff) else x.toInt() and 0xffff) >
            (if ((y.toInt() and SIGN_MASK) != 0) 0x8000 - (y.toInt() and 0xffff) else y.toInt() and 0xffff)
        ) x else y
    }

    /**
     * Returns true if the first half-precision float value is less (smaller
     * toward negative infinity) than the second half-precision float value.
     * If either of the values is NaN, the result is false.
     *
     * @param x The first half-precision value
     * @param y The second half-precision value
     * @return True if x is less than y, false otherwise
     */
    fun less(x: Short, y: Short): Boolean {
        if (isNaN(x)) return false
        if (isNaN(y)) return false
        return (if ((x.toInt() and SIGN_MASK) != 0) 0x8000 - (x.toInt() and 0xffff) else x.toInt() and 0xffff) <
                (if ((y.toInt() and SIGN_MASK) != 0) 0x8000 - (y.toInt() and 0xffff) else y.toInt() and 0xffff)
    }

    /**
     * Returns true if the first half-precision float value is less (smaller
     * toward negative infinity) than or equal to the second half-precision
     * float value. If either of the values is NaN, the result is false.
     *
     * @param x The first half-precision value
     * @param y The second half-precision value
     * @return True if x is less than or equal to y, false otherwise
     */
    fun lessEquals(x: Short, y: Short): Boolean {
        if (isNaN(x)) return false
        if (isNaN(y)) return false
        return (if ((x.toInt() and SIGN_MASK) != 0) 0x8000 - (x.toInt() and 0xffff) else x.toInt() and 0xffff) <=
                (if ((y.toInt() and SIGN_MASK) != 0) 0x8000 - (y.toInt() and 0xffff) else y.toInt() and 0xffff)
    }

    /**
     * Returns true if the first half-precision float value is greater (larger
     * toward positive infinity) than the second half-precision float value.
     * If either of the values is NaN, the result is false.
     *
     * @param x The first half-precision value
     * @param y The second half-precision value
     * @return True if x is greater than y, false otherwise
     */
    fun greater(x: Short, y: Short): Boolean {
        if (isNaN(x)) return false
        if (isNaN(y)) return false
        return (if ((x.toInt() and SIGN_MASK) != 0) 0x8000 - (x.toInt() and 0xffff) else x.toInt() and 0xffff) >
                (if ((y.toInt() and SIGN_MASK) != 0) 0x8000 - (y.toInt() and 0xffff) else y.toInt() and 0xffff)
    }

    /**
     * Returns true if the first half-precision float value is greater (larger
     * toward positive infinity) than or equal to the second half-precision float
     * value. If either of the values is NaN, the result is false.
     *
     * @param x The first half-precision value
     * @param y The second half-precision value
     * @return True if x is greater than y, false otherwise
     */
    fun greaterEquals(x: Short, y: Short): Boolean {
        if (isNaN(x)) return false
        if (isNaN(y)) return false
        return (if ((x.toInt() and SIGN_MASK) != 0) 0x8000 - (x.toInt() and 0xffff) else x.toInt() and 0xffff) >=
                (if ((y.toInt() and SIGN_MASK) != 0) 0x8000 - (y.toInt() and 0xffff) else y.toInt() and 0xffff)
    }

    /**
     * Returns true if the two half-precision float values are equal.
     * If either of the values is NaN, the result is false. [.POSITIVE_ZERO]
     * and [.NEGATIVE_ZERO] are considered equal.
     *
     * @param x The first half-precision value
     * @param y The second half-precision value
     * @return True if x is equal to y, false otherwise
     */
    fun equals(x: Short, y: Short): Boolean {
        if (isNaN(x)) return false
        if (isNaN(y)) return false
        return x == y || ((x.toInt() or y.toInt()) and EXPONENT_SIGNIFICAND_MASK) == 0
    }

    /**
     * Returns true if the specified half-precision float value represents
     * infinity, false otherwise.
     *
     * @param h A half-precision float value
     * @return True if the value is positive infinity or negative infinity,
     * false otherwise
     */
    fun isInfinite(h: Short): Boolean {
        return (h.toInt() and EXPONENT_SIGNIFICAND_MASK) == POSITIVE_INFINITY.toInt()
    }

    /**
     * Returns true if the specified half-precision float value represents
     * a Not-a-Number, false otherwise.
     *
     * @param h A half-precision float value
     * @return True if the value is a NaN, false otherwise
     */
    fun isNaN(h: Short): Boolean {
        return (h.toInt() and EXPONENT_SIGNIFICAND_MASK) > POSITIVE_INFINITY
    }

    /**
     * Returns true if the specified half-precision float value is normalized
     * (does not have a subnormal representation). If the specified value is
     * [.POSITIVE_INFINITY], [.NEGATIVE_INFINITY],
     * [.POSITIVE_ZERO], [.NEGATIVE_ZERO], NaN or any subnormal
     * number, this method returns false.
     *
     * @param h A half-precision float value
     * @return True if the value is normalized, false otherwise
     */
    fun isNormalized(h: Short): Boolean {
        return (h.toInt() and POSITIVE_INFINITY.toInt()) != 0 && (h.toInt() and POSITIVE_INFINITY.toInt()) != POSITIVE_INFINITY.toInt()
    }

    /**
     *
     * Converts the specified half-precision float value into a
     * single-precision float value. The following special cases are handled:
     *
     *  * If the input is [.NaN], the returned value is [Float.NaN]
     *  * If the input is [.POSITIVE_INFINITY] or
     * [.NEGATIVE_INFINITY], the returned value is respectively
     * [Float.POSITIVE_INFINITY] or [Float.NEGATIVE_INFINITY]
     *  * If the input is 0 (positive or negative), the returned value is +/-0.0f
     *  * Otherwise, the returned value is a normalized single-precision float value
     *
     *
     * @param h The half-precision float value to convert to single-precision
     * @return A normalized single-precision float value
     */
    fun toFloat(h: Short): Float {
        val bits = h.toInt() and 0xffff
        val s = bits and SIGN_MASK
        val e = (bits ushr EXPONENT_SHIFT) and SHIFTED_EXPONENT_MASK
        val m = (bits) and SIGNIFICAND_MASK
        var outE = 0
        var outM = 0
        if (e == 0) { // Denormal or 0
            if (m != 0) {
                // Convert denorm fp16 into normalized fp32
                var o = java.lang.Float.intBitsToFloat(FP32_DENORMAL_MAGIC + m)
                o -= FP32_DENORMAL_FLOAT
                return if (s == 0) o else -o
            }
        } else {
            outM = m shl 13
            if (e == 0x1f) { // Infinite or NaN
                outE = 0xff
                if (outM != 0) { // SNaNs are quieted
                    outM = outM or FP32_QNAN_MASK
                }
            } else {
                outE = e - EXPONENT_BIAS + FP32_EXPONENT_BIAS
            }
        }
        val out = (s shl 16) or (outE shl FP32_EXPONENT_SHIFT) or outM
        return java.lang.Float.intBitsToFloat(out)
    }

    /**
     *
     * Converts the specified single-precision float value into a
     * half-precision float value. The following special cases are handled:
     *
     *  * If the input is NaN (see [Float.isNaN]), the returned
     * value is [.NaN]
     *  * If the input is [Float.POSITIVE_INFINITY] or
     * [Float.NEGATIVE_INFINITY], the returned value is respectively
     * [.POSITIVE_INFINITY] or [.NEGATIVE_INFINITY]
     *  * If the input is 0 (positive or negative), the returned value is
     * [.POSITIVE_ZERO] or [.NEGATIVE_ZERO]
     *  * If the input is a less than [.MIN_VALUE], the returned value
     * is flushed to [.POSITIVE_ZERO] or [.NEGATIVE_ZERO]
     *  * If the input is a less than [.MIN_NORMAL], the returned value
     * is a denorm half-precision float
     *  * Otherwise, the returned value is rounded to the nearest
     * representable half-precision float value
     *
     *
     * @param f The single-precision float value to convert to half-precision
     * @return A half-precision float value
     */
    fun toHalf(f: Float): Short {
        val bits = java.lang.Float.floatToRawIntBits(f)
        val s = (bits ushr FP32_SIGN_SHIFT)
        var e = (bits ushr FP32_EXPONENT_SHIFT) and FP32_SHIFTED_EXPONENT_MASK
        var m = (bits) and FP32_SIGNIFICAND_MASK
        var outE = 0
        var outM = 0
        if (e == 0xff) { // Infinite or NaN
            outE = 0x1f
            outM = if (m != 0) 0x200 else 0
        } else {
            e = e - FP32_EXPONENT_BIAS + EXPONENT_BIAS
            if (e >= 0x1f) { // Overflow
                outE = 0x1f
            } else if (e <= 0) { // Underflow
                if (e < -10) {
                    // The absolute fp32 value is less than MIN_VALUE, flush to +/-0
                } else {
                    // The fp32 value is a normalized float less than MIN_NORMAL,
                    // we convert to a denorm fp16
                    m = m or 0x800000
                    val shift = 14 - e
                    outM = m shr shift
                    val lowm = m and ((1 shl shift) - 1)
                    val hway = 1 shl (shift - 1)
                    // if above halfway or exactly halfway and outM is odd
                    if (lowm + (outM and 1) > hway) {
                        // Round to nearest even
                        // Can overflow into exponent bit, which surprisingly is OK.
                        // This increment relies on the +outM in the return statement below
                        outM++
                    }
                }
            } else {
                outE = e
                outM = m shr 13
                // if above halfway or exactly halfway and outM is odd
                if ((m and 0x1fff) + (outM and 0x1) > 0x1000) {
                    // Round to nearest even
                    // Can overflow into exponent bit, which surprisingly is OK.
                    // This increment relies on the +outM in the return statement below
                    outM++
                }
            }
        }
        // The outM is added here as the +1 increments for outM above can
        // cause an overflow in the exponent bit which is OK.
        return ((s shl SIGN_SHIFT) or ((outE shl EXPONENT_SHIFT) + outM)).toShort()
    }

    /**
     *
     * Returns a hexadecimal string representation of the specified half-precision
     * float value. If the value is a NaN, the result is `"NaN"`,
     * otherwise the result follows this format:
     *
     *  * If the sign is positive, no sign character appears in the result
     *  * If the sign is negative, the first character is `'-'`
     *  * If the value is infinity, the string is `"Infinity"`
     *  * If the value is 0, the string is `"0x0.0p0"`
     *  * If the value has a normalized representation, the exponent and
     * significand are represented in the string in two fields. The significand
     * starts with `"0x1."` followed by its lowercase hexadecimal
     * representation. Trailing zeroes are removed unless all digits are 0, then
     * a single zero is used. The significand representation is followed by the
     * exponent, represented by `"p"`, itself followed by a decimal
     * string of the unbiased exponent
     *  * If the value has a subnormal representation, the significand starts
     * with `"0x0."` followed by its lowercase hexadecimal
     * representation. Trailing zeroes are removed unless all digits are 0, then
     * a single zero is used. The significand representation is followed by the
     * exponent, represented by `"p-14"`
     *
     *
     * @param h A half-precision float value
     * @return A hexadecimal string representation of the specified value
     */
    fun toHexString(h: Short): String {
        val o = StringBuilder()
        val bits = h.toInt() and 0xffff
        val s = (bits ushr SIGN_SHIFT)
        val e = (bits ushr EXPONENT_SHIFT) and SHIFTED_EXPONENT_MASK
        val m = (bits) and SIGNIFICAND_MASK
        if (e == 0x1f) { // Infinite or NaN
            if (m == 0) {
                if (s != 0) o.append('-')
                o.append("Infinity")
            } else {
                o.append("NaN")
            }
        } else {
            if (s == 1) o.append('-')
            if (e == 0) {
                if (m == 0) {
                    o.append("0x0.0p0")
                } else {
                    o.append("0x0.")
                    val significand = Integer.toHexString(m)
                    o.append(significand.replaceFirst("0{2,}$".toRegex(), ""))
                    o.append("p-14")
                }
            } else {
                o.append("0x1.")
                val significand = Integer.toHexString(m)
                o.append(significand.replaceFirst("0{2,}$".toRegex(), ""))
                o.append('p')
                o.append((e - EXPONENT_BIAS).toString())
            }
        }
        return o.toString()
    }
}

