
import java.util.*;
public interface scheduler {

    void execute(List<Process> processes, int contextSwitchTime);
}
