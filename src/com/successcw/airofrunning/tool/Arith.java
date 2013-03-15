package com.successcw.airofrunning.tool;

import java.math.BigDecimal;

/**
 * ���Java�ĺ�����Ͳ��܉򾫴_�Č����c���M���\�㣬�@��������ṩ�� �_�ĸ��c���\�㣬�����Ӝp�˳����Ē����롣
 */
public class Arith {

	// Ĭ�J�����\�㾫��
	private static final int DEF_DIV_SCALE = 10;

	// �@����܌�����
	private Arith() {
	}

	/**
	 * �ṩ���_�ļӷ��\�㡣
	 * 
	 * @param v1
	 *            ���Ӕ�
	 * @param v2
	 *            �Ӕ�
	 * @return �ɂ������ĺ�
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * �ṩ���_�Ĝp���\�㡣
	 * 
	 * @param v1
	 *            ���p��
	 * @param v2
	 *            �p��
	 * @return �ɂ������Ĳ�
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * �ṩ���_�ĳ˷��\�㡣
	 * 
	 * @param v1
	 *            ���˔�
	 * @param v2
	 *            �˔�
	 * @return �ɂ������ķe
	 */
	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * �ṩ�����������_�ĳ����\�㣬���l�������M����r�r�����_�� С���c����10λԪ������Ĕ����Ē����롣
	 * 
	 * @param v1
	 *            ������
	 * @param v2
	 *            ����
	 * @return �ɂ���������
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/**
	 * �ṩ�����������_�ĳ����\�㡣���l�������M����r�r����scale����ָ �����ȣ�����Ĕ����Ē����롣
	 * 
	 * @param v1
	 *            ������
	 * @param v2
	 *            ����
	 * @param scale
	 *            ��ʾ��ʾ��Ҫ���_��С���c�����λ��
	 * @return �ɂ���������
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
	 * �ṩ���_��С��λ�Ē�����̎��
	 * 
	 * @param v
	 *            ��Ҫ�Ē�����Ĕ�λ
	 * @param scale
	 *            С���c�ᱣ���λ
	 * @return �Ē�������ĽY��
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