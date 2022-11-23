import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.oppblueprints.Board;
import org.oppblueprints.Cell;
import org.oppblueprints.Difficulty;

public class BoardTest {

    @Test
    void testBoardConstructorArraySize() {
        int rowLength = 5;
        int colLength = 5;

        Board board = new Board(new Difficulty(rowLength,colLength,0));
        Cell[][] boardArray = board.getBoardArray();
        Assertions.assertEquals(boardArray.length, rowLength);
        Assertions.assertEquals(boardArray[0].length, colLength);
    }


}
