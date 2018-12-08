package org.smileframework.tool.debug;

import org.fusesource.jansi.Ansi;

import java.util.Formatter;
import java.util.Objects;

import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

/**
 * @author liuxin
 * @version Id: Console.java, v 0.1 2018/6/7 下午11:00 liuxin Exp $$
 */
public class Console {

    private Console() {
    }

    public static Console create() {
        return new Console();
    }

    /**
     * 红色
     *
     * @param str 文本
     */
    public void red(String str) {
        Ansi text = ansi().eraseScreen().fg(RED).a(str).reset();
        print(text);
    }

    /**
     * 黄色
     *
     * @param str 文本
     */
    public void yellow(String str) {
        Ansi text = ansi().eraseScreen().fg(YELLOW).a(str).reset();
        print(text);
    }

    /**
     * 蓝色
     *
     * @param str 文本
     */
    public void blue(String str) {
        Ansi text = ansi().eraseScreen().fg(BLUE).a(str).reset();
        print(text);
    }

    /**
     * 绿色
     *
     * @param str 文本
     */
    public void green(String str) {
        Ansi text = ansi().eraseScreen().fg(GREEN).a(str).reset();
        print(text);
    }

    /**
     * 青色
     *
     * @param str 文本
     */
    public void cyan(String str) {
        Ansi text = ansi().eraseScreen().fg(CYAN).a(str).reset();
        print(text);
    }

    /**
     * 品红色
     *
     * @param str 文本
     */
    public void magenta(String str) {
        Ansi text = ansi().eraseScreen().fg(MAGENTA).a(str).reset();
        print(text);
    }

    private static void print(Object plainText) {
        System.out.println(plainText);
    }

    public static void warn(Object object) {
        error(object);
    }

    public static void error(Object object) {
        Ansi text = ansi().eraseScreen().fg(RED).a("[错误]: ").reset();
        System.out.print(text);
        print(object);
    }

    public static void log(Object object) {
        Ansi text = ansi().eraseScreen().fg(GREEN).a("[result]: ").reset();
        System.out.print(text);
        print(object.toString());
    }

    public static void log(String format, Object... args) {
        Formatter formatter = null;
        try {
            formatter = new Formatter();
            log(formatter.format(format, args).toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (formatter != null) {
                formatter.close();
            }
        }
    }

    /**
     * 正常的
     *
     * @param title   左前缀
     * @param message 打印信息
     */
    public static void customerNormal(String title, Object message) {
        Objects.requireNonNull(title, "title not is null");
        Ansi text = ansi().eraseScreen().fg(GREEN).a("[" + title + "]: ").reset();
        System.out.print(text);
        print(message.toString());
    }

    /**
     * 不正常的
     *
     * @param title   左前缀
     * @param message 打印信息
     */
    public static void customerAbnormal(String title, Object message) {
        Objects.requireNonNull(title, "title not is null");
        Ansi text = ansi().eraseScreen().fg(YELLOW).a("[" + title + "]: ").reset();
        System.out.print(text);
        print(message.toString());
    }


    public static void normal(Object message) {
        customerNormal("正常", message);
    }

    public static void abnormal(Object message) {
        customerAbnormal("错误", message);
    }

    public static void main(String[] args) {
        normal("你好");
    }
}
