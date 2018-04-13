package org.smileframework.tool.serialization;

import java.io.Serializable;

/**
 * @Package: com.netty
 * @Description:
 * @author: liuxin
 * @date: 2018/3/16 下午4:57
 */
public class HeartBeatPackage{
    private int main;
    private int sub;

    public int getMain() {
        return main;
    }

    public void setMain(int main) {
        this.main = main;
    }

    public int getSub() {
        return sub;
    }

    public void setSub(int sub) {
        this.sub = sub;
    }

    @Override
    public String toString() {
        return "HeartBeatPackage{" +
                "main=" + main +
                ", sub=" + sub +
                '}';
    }
}
