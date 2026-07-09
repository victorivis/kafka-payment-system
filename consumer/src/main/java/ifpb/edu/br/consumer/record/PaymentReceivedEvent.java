package ifpb.edu.br.consumer.record;

import java.util.UUID;

public record PaymentReceivedEvent(
        UUID id,
        String name,
        Integer value
) {}