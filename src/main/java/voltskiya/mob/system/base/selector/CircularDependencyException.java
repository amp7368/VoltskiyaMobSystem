package voltskiya.mob.system.base.selector;

public class CircularDependencyException extends Exception {

    public CircularDependencyException(String message) {
        super(message);
    }
}
