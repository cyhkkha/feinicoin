package name.feinimouse.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by 菲尼莫斯 on 2019/6/30
 * Email: cyhkkha@gmail.com
 * File name: LoopUtils
 * Program : feinicoin
 * Description :
 */
public class LoopUtils {
    public static void loop(int i, LoopFunction lf) {
        for(var j = 0; j < i; j++) {
            lf.doLoop();
        }
    }
    public static void loop(int i, LoopIndexFunction lf) {
        for(var j = 0; j < i; j++) {
            lf.doLoop(j);
        }
    }
    public static <T> List<T> loopToList(int i, LoopClassFunction<T> lf) {
        List<T> list = new ArrayList<>();
        for(var j = 0; j < i; j++) {
            T t = lf.doLoop();
            list.add(t);
        }
        return list;
    }

    public static <T> List<T> loopToList(int i, LoopIndexClassFunction<T> lf) {
        List<T> list = new ArrayList<>();
        for(var j = 0; j < i; j++) {
            T t = lf.doLoop(j);
            list.add(t);
        }
        return list;
    }
    
    public interface LoopFunction {
        void doLoop();
    }
    public interface  LoopClassFunction <T> {
        T doLoop();
    }
    public interface LoopIndexFunction {
        void doLoop(int index);
    }
    public interface  LoopIndexClassFunction <T> {
        T doLoop(int index);
    }
}
