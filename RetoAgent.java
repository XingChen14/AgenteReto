
import java.util.Arrays;
import java.util.Random;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class RetoAgent extends Agent {

    private final int[] goalState = {
        1, 2, 3,
        4, 5, 6,
        7, 8, 0
    };

    private final Random random = new Random();

    @Override
    protected void setup() {
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                ACLMessage msg = receive();

                if (msg == null) {
                    block();
                    return;
                }

                if (msg.getPerformative() != ACLMessage.REQUEST) {
                    return;
                }

                // Reto solamente genera el tablero.
                int[] initialState = generateRandomBoard(30);

                // Responder a quien solicitó el reto.
                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.INFORM);
                reply.setContent(toText(initialState));

                send(reply);
            }
        });
    }

    private int[] generateRandomBoard(int moves) {
        int[] board = goalState.clone();
        int empty = 8;
        int previousEmpty = -1;

        for (int i = 0; i < moves; i++) {
            int row = empty / 3;
            int col = empty % 3;

            int[] candidates = new int[4];
            int count = 0;

            if (row > 0) candidates[count++] = empty - 3;
            if (row < 2) candidates[count++] = empty + 3;
            if (col > 0) candidates[count++] = empty - 1;
            if (col < 2) candidates[count++] = empty + 1;

            int next;

            do {
                next = candidates[random.nextInt(count)];
            } while (count > 1 && next == previousEmpty);

            int temp = board[empty];
            board[empty] = board[next];
            board[next] = temp;

            previousEmpty = empty;
            empty = next;
        }

        return board;
    }

    private String toText(int[] board) {
        return Arrays.toString(board)
                     .replace("[", "")
                     .replace("]", "")
                     .replace(" ", "");
    }
}
