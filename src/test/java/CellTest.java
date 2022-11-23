import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.oppblueprints.Cell;
import org.oppblueprints.Game;
import org.oppblueprints.GameResult;
import org.oppblueprints.ResultErrorType;

class CellTest {

    @Test
    void testConstructorNoMine() {
        Cell cell = new Cell();
        Assertions.assertFalse(cell.isCleared());
        Assertions.assertFalse(cell.hasMine());

        Cell cell1 = new Cell(false);
        Assertions.assertFalse(cell1.isCleared());
        Assertions.assertFalse(cell1.hasMine());
    }

    @Test
    void testConstructorHasMine() {
        Cell mineCell = new Cell(true);
        Assertions.assertFalse(mineCell.isCleared());
        Assertions.assertTrue(mineCell.hasMine());
    }

    @Test
    void testRevealNoMine() {
        Cell cell = new Cell();
        GameResult result = cell.reveal();
        Assertions.assertTrue(cell.isCleared());
        Assertions.assertFalse(result.isGameLost());
        Assertions.assertEquals(result.getError(), ResultErrorType.NONE);
    }

    @Test
    void testRevealHasMine() {
        Cell cell = new Cell(true);
        GameResult result = cell.reveal();
        Assertions.assertTrue(cell.isCleared());
        Assertions.assertTrue(result.isGameLost());
        Assertions.assertEquals(result.getError(), ResultErrorType.NONE);
    }

    @Test
    void testAddFlagCell() {
        Cell cell = new Cell();
        Assertions.assertFalse(cell.isFlagged());
        cell.flag();
        Assertions.assertTrue(cell.isFlagged());
    }

    @Test
    void testRemoveFlagCell() {
        Cell cell = new Cell();
        Assertions.assertFalse(cell.isFlagged());
        cell.flag();
        Assertions.assertTrue(cell.isFlagged());
        cell.flag();
        Assertions.assertFalse(cell.isFlagged());
    }

    @Test
    void testUnableToRevealFlaggedCell() {
        Cell cell = new Cell();
        cell.flag();
        Assertions.assertTrue(cell.isFlagged());
        GameResult result = cell.reveal();
        Assertions.assertEquals(result.getError(), ResultErrorType.FLAGGED);
    }

    @Test
    void testUnableToRevealClearedCell() {
        Cell cell = new Cell();
        cell.reveal();
        Assertions.assertTrue(cell.isCleared());
        GameResult result = cell.reveal();
        Assertions.assertEquals(result.getError(), ResultErrorType.ALREADY_CLEARED);
    }

}
