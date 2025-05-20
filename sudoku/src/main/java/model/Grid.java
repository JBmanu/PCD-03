package model;

import de.sfuhrm.sudoku.Creator;
import de.sfuhrm.sudoku.GameMatrix;

public interface Grid {

    static Grid create(final Settings settings) {
        return new GridImpl(settings);
    }


    class GridImpl implements Grid {
        private final Settings settings;
        private final GameMatrix solution;
        private final GameMatrix startGrid;
        private final GameMatrix currentGrid;

        public GridImpl(final Settings settings) {
            final int maxNumbersToClear = settings.difficulty().computeMaxNumbersToClear(settings.size());

            this.settings = settings;
            this.solution = Creator.createFull(settings.size().schema());
            this.startGrid = Creator.createRiddle(this.solution, maxNumbersToClear);
            this.currentGrid = this.startGrid;
        }

    }
}
