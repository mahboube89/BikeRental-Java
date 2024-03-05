package org.mr.abschlussprojekt.bikeRental.logic;

/**
 * Singleton class to used to track the current active view within the application.
 */
public class AppState {

    private static AppState instance; // Singleton instance
    private String activeView;

    /**
     * Private constructor to prevent external instantiation.
     */
    private AppState() {
    }

    /**
     * Provides access to the singleton instance of AppState, creating it if it does not yet exist.
     *
     * @return The singleton instance of AppState.
     */
    public static synchronized AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
        }
        return instance;
    }


    /**
     * Retrieves the identifier of the currently active view in the application.
     *
     * @return The identifier of the active view.
     */
    public String getActiveView() {
        return activeView;
    }

    /**
     * Sets the identifier of the currently active view in the application.
     * This method can be used to switch views and update the application state accordingly.
     *
     * @param activeView The identifier of the new active view to set.
     */
    public void setActiveView(String activeView) {
        this.activeView = activeView;
    }
}
