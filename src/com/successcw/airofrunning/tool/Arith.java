package com.successcw.airofrunning.tool;

import java.math.BigDecimal;

/**
 * 由於Java的簡單類型不能夠精確的對浮點數進行運算，這個工具類提供精 確的浮點數運算，包括加減乘除和四捨五入。
 */
public class Arith {

	// 默認除法運算精度
	private static final int DEF_DIV_SCALE = 10;

	// 這個類不能實例化
	private Arith() {
	}

	/**
	 * 提供精確的加法運算。
	 * 
	 * @param v1
	 *            被加數
	 * @param v2
	 *            加數
	 * @return 兩個參數的和
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * 提供精確的減法運算。
	 * 
	 * @param v1
	 *            被減數
	 * @param v2
	 *            減數
	 * @return 兩個參數的差
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 提供精確的乘法運算。
	 * 
	 * @param v1
	 *            被乘數
	 * @param v2
	 *            乘數
	 * @return 兩個參數的積
	 */
	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 提供（相對）精確的除法運算，當發生除不盡的情況時，精確到 小數點以後10位元，以後的數字四捨五入。
	 * 
	 * @param v1
	 *            被除數
	 * @param v2
	 *            除數
	 * @return 兩個參數的商
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/**
	 * 提供（相對）精確的除法運算。當發生除不盡的情況時，由scale參數指 定精度，以後的數字四捨五入。
	 * 
	 * @param v1
	 *            被除數
	 * @param v2
	 *            除數
	 * @param scale
	 *            表示表示需要精確到小數點以後幾位。
	 * @return 兩個參數的商
	 */
	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 提供精確的小數位四捨五入處理。
	 * 
	 * @param v
	 *            需要四捨五入的數位
	 * @param scale
	 *            小數點後保留幾位
	 * @return 四捨五入後的結果
	 */
	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}