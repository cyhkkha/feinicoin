package name.feinimouse.lambda;

public interface InOutRunner<I, O> {
    O run(I input);
}
