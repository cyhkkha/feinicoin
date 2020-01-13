package name.feinimouse.utils;

import name.feinimouse.lambda.OrdExcRunner;
import name.feinimouse.lambda.RetExcRunner;

import java.util.LinkedList;
import java.util.List;

/**
 * Create by 菲尼莫斯 on 2019/6/30
 * Email: cyhkkha@gmail.com
 * File name: LoopUtils
 * Program : feinicoin
 * Description :
 */
public class LoopUtils {
    
    public static void loopExec(int i, long interval, OrdExcRunner runner) throws Exception {
        try {
            runner.run();
        } catch (Exception e) {
            if (i > 0) {
                Thread.sleep(interval);
                loopExec(i - 1, interval, runner);
            } else {
                throw e;
            }
        }
    }

    public static<T> T loopExec(int i, long interval, RetExcRunner<T> runner) throws Exception {
        try {
            return runner.run();
        } catch (Exception e) {
            if (i > 0) {
                Thread.sleep(interval);
                return loopExec(i - 1, interval, runner);
            } else {
                throw e;
            }
        }
    }
    
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

    public static void loopBreak(int i, LoopIndexClassFunction<?> lf) {
        for(var j = 0; j < i; j++) {
            var res = lf.doLoop(j);
            if (res == null || !(Boolean) res) {
                break;
            }
        }
    }
    public static void loopBreak(int i, LoopClassFunction<?> lf) {
        for(var j = 0; j < i; j++) {
            var res = lf.doLoop();
            if (res == null || !(Boolean) res) {
                break;
            }
        }
    }
    
    public static <T> LinkedList<T> loopToList(int i, LoopClassFunction<T> lf) {
        LinkedList<T> list = new LinkedList<>();
        for(var j = 0; j < i; j++) {
            T t = lf.doLoop();
            list.add(t);
        }
        return list;
    }

    public static <T> LinkedList<T> loopToList(int i, LoopIndexClassFunction<T> lf) {
        LinkedList<T> list = new LinkedList<>();
        for(var j = 0; j < i; j++) {
            T t = lf.doLoop(j);
            list.add(t);
        }
        return list;
    }

    public static <T> LinkedList<T> loopToListBreak(int i, LoopClassFunction<T> lf) {
        LinkedList<T> list = new LinkedList<>();
        for(var j = 0; j < i; j++) {
            T t = lf.doLoop();
            if (t == null) {
                break;
            }
            list.add(t);
        }
        return list;
    }

    public static <T> LinkedList<T> loopToListBreak(int i, LoopIndexClassFunction<T> lf) {
        LinkedList<T> list = new LinkedList<>();
        for(var j = 0; j < i; j++) {
            T t = lf.doLoop(j);
            if (t == null) {
                break;
            }
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
