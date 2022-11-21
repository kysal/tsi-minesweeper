import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.oppblueprints.Cell;
import org.oppblueprints.GameResult;

public class CellTest {

    @Test
    public void testConstructorNoMine() {
        Cell cell = new Cell();
        Assertions.assertFalse(cell.isCleared());
        Assertions.assertFalse(cell.hasMine());

        Cell cell1 = new Cell(false);
        Assertions.assertFalse(cell1.isCleared());
        Assertions.assertFalse(cell1.hasMine());
    }

    @Test
    public void testConstructorHasMine() {
        Cell mineCell = new Cell(true);
        Assertions.assertFalse(mineCell.isCleared());
        Assertions.assertTrue(mineCell.hasMine());
    }

    @Test
    public void testRevealNoMine() {
        Cell cell = new Cell();

        GameResult result = cell.reveal();
        Assertions.assertTrue(cell.isCleared());
        Assertions.assertFalse(result.isGameLost());
    }
}
