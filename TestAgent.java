import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class TestAgent extends Agent {

    @Override
    protected void setup() {
        // Enviar solicitud de un nuevo reto al agente llamado "reto"
        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.addReceiver(new AID("reto", AID.ISLOCALNAME));
        request.setContent("generar_reto");

        send(request);
        //System.out.println("Solicitud enviada a RetoAgent.");

        // Comportamiento para recibir la respuesta del RetoAgent
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                ACLMessage reply = receive();

                if (reply == null) {
                    block();
                    return;
                }

                System.out.println("\n===== RESPUESTA RECIBIDA DE RETOAGENT =====");
                System.out.println("Tablero generado: " + reply.getContent());

                // Finalizar el agente de prueba una vez recibido el resultado
                if (reply.getPerformative() == ACLMessage.INFORM) {
                    doDelete();
                }
            }
        });
    }

    
}