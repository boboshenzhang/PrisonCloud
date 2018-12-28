package top.ibase4j.core.util;

import java.math.BigInteger;
import java.util.Random;
import java.util.UUID;


/*********************************
 * @Description: 获取编号工具类<br/>
 * @author HuangLongPu
 * @date 2017年12月22日 下午2:41:44
 * @version v1.0
 * @updater HuangLongPu-2018年04月16日 下午2:41:44
 * @updateReason
 * @Company:JianXin
 * @Copyright: Copyright (c) 2010-2020
 *********************************/
public class NumberGenerator {
	
	private NumberGenerator() {}

	/**
	 * 获取类型为BigInteger，位数30位
	 * @return
	 */
	public static BigInteger getNumber() {
		
		Integer randomNumber = new Random().nextInt(100);
		Integer uuidInt = UUID.randomUUID().hashCode();
		if (uuidInt < 0) {
			uuidInt = 0 - uuidInt;
		}

		return new BigInteger(DateConvert.formatToCurTimeString()
				+ completePrefixZero(randomNumber.toString(), 2)
				+ completePrefixZero(uuidInt.toString(), 11));
	}

	private static String completePrefixZero(String value, int completeSize) {

		int lenth = value.length();
		StringBuffer s = new StringBuffer(value);
		if(lenth > completeSize) {
			s.deleteCharAt(completeSize);
			return s.toString();
		}
		int offset = completeSize - lenth;
		for (int i = 0; i < offset; i++) {
			// 随机加一个10以内的正整数
			s.insert(0, new Random().nextInt(10));
		}
		
		return s.toString();
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 1; i++) {
			System.out.println(getNumber());
		}
	}
}