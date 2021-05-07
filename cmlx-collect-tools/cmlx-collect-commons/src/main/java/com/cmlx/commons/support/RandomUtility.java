package com.cmlx.commons.support;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.Random;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 18:48
 * @Desc -> 随机数工具类
 **/
@UtilityClass
public class RandomUtility {

    private final Random RANDOMGEN = new Random();
    private final char[] NUMBERSANDLETTERS = ("0123456789abcdefghijklmnopqrstuvwxyz" + "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
    private final int[] NUMBERS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    /**
     * 在指定范围内，随机生成经度或纬度
     *
     * @param minNum
     * @param maxNum
     * @return
     */
    public double randomLngOrLat(double minNum, double maxNum) {
        BigDecimal digit = BigDecimal.valueOf(1000000);

        BigDecimal mind = BigDecimal.valueOf(minNum).multiply(digit);
        BigDecimal maxd = BigDecimal.valueOf(maxNum).multiply(digit);

        // min+Math.random()*(max-min+1)
        BigDecimal random = maxd.subtract(mind).add(BigDecimal.valueOf(1)).multiply(BigDecimal.valueOf(Math.random())).add(mind);
        BigDecimal result = random.divide(BigDecimal.valueOf(1000000), 6, BigDecimal.ROUND_DOWN);
        return result.doubleValue();
    }

    /**
     * 在指定范围内，随机生成数字字符串
     *
     * @param minNum
     * @param maxNum
     * @return
     */
    public String randomNumberString(Long minNum, Long maxNum) {

        BigDecimal mind = BigDecimal.valueOf(minNum);
        BigDecimal maxd = BigDecimal.valueOf(maxNum);

        // min+Math.random()*(max-min+1)
        BigDecimal random = maxd.subtract(mind).add(BigDecimal.valueOf(1)).multiply(BigDecimal.valueOf(Math.random())).add(mind);
        return random.setScale(0, BigDecimal.ROUND_DOWN).toString();
    }

    /**
     * 在指定范围内，随机生成一个long类型数字
     *
     * @param minNum
     * @param maxNum
     * @return
     */
    public long randomLong(Long minNum, Long maxNum) {

        BigDecimal mind = BigDecimal.valueOf(minNum);
        BigDecimal maxd = BigDecimal.valueOf(maxNum);

        // min+Math.random()*(max-min+1)
        BigDecimal random = maxd.subtract(mind).add(BigDecimal.valueOf(1)).multiply(BigDecimal.valueOf(Math.random())).add(mind);
        return random.setScale(0, BigDecimal.ROUND_DOWN).longValue();
    }

    /**
     * 在指定范围内，随机生成一个int类型数字
     *
     * @param minNum
     * @param maxNum
     * @return
     */
    public int randomInt(int minNum, int maxNum) {
        // min+Math.random()*(max-min+1)
        Double random = minNum + Math.random()*(maxNum-minNum+1);
        return random.intValue();
    }

    /**
     * 生成一个指定长度的字符串，包含数字，大小写字母
     *
     * @param length
     * @return
     */
    public String randomString(int length) {
        if (length < 1) {
            return null;
        }
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = NUMBERSANDLETTERS[RANDOMGEN.nextInt(71)];
        }
        return new String(randBuffer);
    }

    /**
     * 随机生成短信验证码
     *
     * @param length
     * @return
     */
    public String randomSmsCode(int length) {
        if (length < 1) {
            return null;
        }
        StringBuffer code = new StringBuffer();
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            int index = rand.nextInt(10);
            code.append(NUMBERS[index]);
        }
        return code.toString();
    }


    /**
     * 生成数组
     *
     * @param length
     * @return
     */
    public int[] generateNumberArray(int length) {
        int[] numbers = new int[length];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = i + 1;
        }
        return numbers;
    }

    /**
     * 在指定长度数组内，随机选择
     *
     * @param size
     * @param length
     * @return
     */
    public int[] generateRandomNumberArray(int size, int length) {
        int[] numbers = generateNumberArray(length);
        int[] result = new int[size];
        int n = length;
        for (int i = 0; i < result.length; i++) {
            int r = (int) (Math.random() * n);
            result[i] = numbers[r];
            numbers[r] = numbers[n - 1];
            n--;
        }
        return result;
    }

    /**
     * 生成指定范围内随机数
     * @param rang
     * @return
     */
    public int randomNextInt(int rang){
        Random rand = new Random();
        int index = rand.nextInt(rang);
        return index;
    }


    public static void main(String[] args) {
        int random = randomInt(0,5);
        System.out.println(random);
    }
}

