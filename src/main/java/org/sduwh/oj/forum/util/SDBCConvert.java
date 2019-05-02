package org.sduwh.oj.forum.util;

public class SDBCConvert {

    /**
     * 全角对应于ASCII表的可见字符从！开始，偏移值为65281
     */
    static final char SBC_CHAR_START = 65281; // 全角！

    /**
     * 全角对应于ASCII表的可见字符到～结束，偏移值为65374
     */
    static final char SBC_CHAR_END = 65374; // 全角～

    /**
     * ASCII表中除空格外的可见字符与对应的全角字符的相对偏移
     */
    static final int CONVERT_STEP = 65248; // 全角半角转换间隔

    /**
     * 全角空格的值，它没有遵从与ASCII的相对偏移，必须单独处理
     */
    static final char SBC_SPACE = 12288; // 全角空格 12288

    /**
     * 半角空格的值，在ASCII中为32(Decimal)
     */
    static final char DBC_SPACE = ' '; // 半角空格

    /**
     * 全角转换半角
     *
     * @param src
     * @return
     */
    public static int qj2bj(char src) {
        int r = src;
        if (src >= SBC_CHAR_START && src <= SBC_CHAR_END) { // 如果位于全角！到全角～区间内
            r = src - CONVERT_STEP;
        } else if (src == SBC_SPACE) { // 如果是全角空格
            r = DBC_SPACE;
        }
        return r;
    }
}
