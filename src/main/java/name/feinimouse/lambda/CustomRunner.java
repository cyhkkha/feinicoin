package name.feinimouse.lambda;

public interface CustomRunner<I, O> {
    O run(I input);
}
