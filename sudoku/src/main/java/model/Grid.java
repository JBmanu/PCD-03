package model;

import de.sfuhrm.sudoku.Creator;
import de.sfuhrm.sudoku.GameMatrix;

public interface Grid {

    class GameGridImpl implements Grid {
        private final GameMatrix solution;
        private final GameMatrix gameMatrix;

        public GameGridImpl(final Settings settings) {
            final Settings.Size size = settings.size();
            final int maxNumbersToClear = settings.difficulty().computeMaxNumbersToClear(size);

            this.solution = Creator.createFull(size.schema());
            this.gameMatrix = Creator.createRiddle(this.solution, maxNumbersToClear);
        }


    }


}
