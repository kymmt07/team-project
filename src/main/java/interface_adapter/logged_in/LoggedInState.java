package interface_adapter.logged_in;

import java.util.HashMap;
import java.util.Map;

public class LoggedInState {

    private String username = "";
    private final Map<Integer, String> plotStates = new HashMap<>();
    private String plotError;

    public String getUsername(){
        return this.username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPlotState(int plotID) {
        return plotStates.getOrDefault(plotID, "EMPTY");
    }

    public void setPlotState(int plotID, String state) {
        plotStates.put(plotID, state);
    }

    public String getPlotError() {
        return plotError;
    }

    public void setPlotError(String plotError) {
        this.plotError = plotError;
    }
}
