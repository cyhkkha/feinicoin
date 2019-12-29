package name.feinimouse.feinicoinplus.core.lambda;

public interface InOutRunner<I, O> {
    O run(I input);
}
