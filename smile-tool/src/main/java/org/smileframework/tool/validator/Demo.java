package org.smileframework.tool.validator;

import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

/**
 * @Package: org.smileframework.tool.validator
 * @Description: ${todo}
 * @date: 2018/5/16 下午2:42
 * @author: liuxin
 */
public class Demo {
    public static void main(String[] args) {
        SmileValidator.validate(new Test());
        SmileValidator.validate(new Test("你好我是法师"));
    }

    static class Test {
        @NotEmpty(message = "不能为空")
        @NotNull(message = "不能为空")
        @MaxLength(value = 5, message = "不成超过5个字符")
        private String name;

        public Test(){}
        public Test(String name){
            this.name=name;
        }
    }
}
