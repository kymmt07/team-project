package view;

import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.logged_in.LoggedInViewModelListener;

import interface_adapter.grow_flower.GrowController;
import interface_adapter.plant_flower.PlantController;

import javax.swing.*;
import java.awt.*;

public class LoggedInView extends JPanel implements LoggedInViewModelListener {
    private static final int GRID_SIZE = 3;
    private static final int SQUARE_SIZE = 80;

    private final LoggedInViewModel loggedInViewModel;
    private final JButton[][] squares = new JButton[GRID_SIZE][GRID_SIZE];
    private final JButton galleryButton = new JButton("Gallery");
    private final JLabel numberPlants = new JLabel("Plants in Gallery: 0", SwingConstants.CENTER);

    private GrowController growController;
    private PlantController plantController;

    public LoggedInView(LoggedInViewModel loggedInViewModel) {
        this.loggedInViewModel = loggedInViewModel;
        this.loggedInViewModel.addListener(this);

        final JPanel grid = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE));
        for (int row = 0; row < GRID_SIZE; row++){
            for (int col = 0; col < GRID_SIZE; col++) {
                final int r = row;
                final int c = col;
                final JButton square = new JButton();
                square.setPreferredSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
                square.addActionListener(evt -> squareClicked(r,c));
                squares[row][col] = square;
                grid.add(square);
            }
        }

        final JPanel galleryInfo = new JPanel(new GridLayout(2, 1));
        galleryButton.addActionListener(evt -> galleryButtonClicked());
        galleryInfo.add(galleryButton);
        galleryInfo.add(numberPlants);

        setLayout(new BorderLayout());
        add(grid, BorderLayout.CENTER);
        add(galleryInfo, BorderLayout.EAST);
    }

    public void setGrowController(GrowController growController) {
        this.growController = growController;
    }

    public void setPlantController(PlantController plantController) {this.plantController = plantController;}

    // Just for convenience and that plotID can be only int, setting the plotID from 1-9.
    private int getPlotID(int row, int col) {
        return row * GRID_SIZE + (col + 1);
    }

    /**
     * Called when a square is clicked.
     * @param row the row of the clicked square
     * @param col the column of the clicked square
     */
    private void squareClicked(int row, int col) {
        final int plotID = getPlotID(row, col);
        final LoggedInState state = loggedInViewModel.getState();
        final String plotState = state.getPlotState(plotID);

        // Plant Actions
        if ("EMPTY".equals(plotState)) {
            final JPopupMenu menu = new JPopupMenu();
            final JMenuItem plantFlowerOption = new JMenuItem("click to plant 🌷");

            plantFlowerOption.addActionListener(evt -> plantController.execute(plotID));
            menu.add(plantFlowerOption);

            final JButton clickedSquare = squares[row][col];
            final int x = (clickedSquare.getWidth() - menu.getPreferredSize().width) / 2;
            final int y = (clickedSquare.getHeight() - menu.getPreferredSize().height) / 2;

            menu.show(clickedSquare, x, y);
        }

        // Grow Actions
        if ("SMALL".equals(plotState)) {
            final JPopupMenu menu = new JPopupMenu();
            final JMenuItem growItem = new JMenuItem("Grow");

            growItem.addActionListener(evt -> growController.execute(plotID));

            menu.add(growItem);

            final JButton clickedSquare = squares[row][col];
            final int x = (clickedSquare.getWidth() - menu.getPreferredSize().width) / 2;
            final int y = (clickedSquare.getHeight() - menu.getPreferredSize().height) / 2;

            menu.show(clickedSquare, x, y);
        }

        // TODO: CODE HARVEST ACTIONS
    }

    private void galleryButtonClicked(){
        // TODO: CODE GALLERY
    }

    @Override
    public void stateChanged() {
        final LoggedInState state = loggedInViewModel.getState();

        final Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JFrame) {
            ((JFrame) window).setTitle("Logged in as: " + state.getUsername());
        }

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                final int plotID = getPlotID(row, col);
                final String plotState = state.getPlotState(plotID);

                // TODO: Replace text labels with flower icons for SMALL and BIG.
                squares[row][col].setText(plotState);
            }
        }

        if (state.getPlotError() != null) {
            final String errorMessage = state.getPlotError();
            state.setPlotError(null);

            JOptionPane.showMessageDialog(this, errorMessage);
        }
    }

    // TODO: CODE PLANT COUNT
    // changeCountLabel.setText("Plants in Gallery: " + stat.getChangeCount());
}
